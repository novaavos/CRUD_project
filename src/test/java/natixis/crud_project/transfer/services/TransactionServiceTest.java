package natixis.crud_project.transfer.services;

import natixis.crud_project.transfer.exceptions.BusinessException;
import natixis.crud_project.transfer.models.Transaction;
import natixis.crud_project.transfer.repositories.TransactionRepository;
import natixis.crud_project.transfer.utils.TaxCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository repo;

    @InjectMocks
    private TransactionService service;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    @DisplayName("create() should set createdAt, calculate fee and save (same-day small amount)")
    void create_shouldSetCreatedAtCalculateFeeAndSave() {
        Transaction toCreate = new Transaction();
        toCreate.setOriginAccount("PT123");
        toCreate.setDestinationAccount("PT456");
        // amount <= 1000 requires scheduledDate == today according to TaxCalculator rules
        toCreate.setAmount(BigDecimal.valueOf(500));
        toCreate.setScheduledDate(LocalDate.now()); // same day -> valid for tax rule A

        when(repo.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(1L);
            return t;
        });

        Transaction saved = service.create(toCreate);

        verify(repo, times(1)).save(transactionCaptor.capture());
        Transaction passed = transactionCaptor.getValue();

        assertThat(passed.getCreatedAt()).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);

        try {
            BigDecimal expectedFee = TaxCalculator.calculateFee(passed.getAmount(), passed.getScheduledDate());
            assertThat(saved.getFee()).isEqualByComparingTo(expectedFee);
        } catch (BusinessException e) {
            fail("TaxCalculator threw BusinessException unexpectedly: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("create() should propagate BusinessException for unsupported inputs")
    void create_shouldPropagateBusinessException() {
        Transaction toCreate = new Transaction();
        toCreate.setOriginAccount("PTX");
        toCreate.setDestinationAccount("PTY");
        // amount small but scheduledDate in future -> should throw per your TaxCalculator rules
        toCreate.setAmount(BigDecimal.valueOf(500));
        toCreate.setScheduledDate(LocalDate.now().plusDays(2)); // invalid for amount <=1000

        assertThatThrownBy(() -> service.create(toCreate))
                .isInstanceOf(BusinessException.class);

        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("listAll() should return repository results")
    void listAll_shouldReturnRepositoryResults() {
        Transaction t1 = new Transaction();
        t1.setId(1L);
        Transaction t2 = new Transaction();
        t2.setId(2L);

        when(repo.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Transaction> all = service.listAll();

        assertThat(all).hasSize(2).containsExactly(t1, t2);
        verify(repo, times(1)).findAll();
    }

    @Test
    @DisplayName("findById() should return optional from repository")
    void findById_shouldReturnOptionalFromRepository() {
        Transaction t = new Transaction();
        t.setId(10L);
        when(repo.findById(10L)).thenReturn(Optional.of(t));

        Optional<Transaction> opt = service.findById(10L);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(10L);
        verify(repo, times(1)).findById(10L);
    }

    @Test
    @DisplayName("update() should update existing entity and return saved (use valid tax input)")
    void update_existing_shouldUpdateAndSave() {
        Transaction existing = new Transaction();
        existing.setId(5L);
        existing.setOriginAccount("OLD");
        existing.setDestinationAccount("OLD_DST");
        existing.setAmount(BigDecimal.valueOf(100));
        existing.setScheduledDate(LocalDate.now());
        existing.setCreatedAt(LocalDateTime.now().minusDays(1));

        Transaction updated = new Transaction();
        updated.setOriginAccount("NEW");
        updated.setDestinationAccount("NEW_DST");
        // keep amount small and scheduledDate == today to avoid BusinessException
        updated.setAmount(BigDecimal.valueOf(150));
        updated.setScheduledDate(LocalDate.now()); // valid for amount <= 1000

        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = service.update(5L, updated);

        assertThat(result).isNotNull();
        assertThat(result.getOriginAccount()).isEqualTo("NEW");
        assertThat(result.getDestinationAccount()).isEqualTo("NEW_DST");
        assertThat(result.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(result.getScheduledDate()).isEqualTo(updated.getScheduledDate());

        try {
            BigDecimal expectedFee = TaxCalculator.calculateFee(result.getAmount(), result.getScheduledDate());
            assertThat(result.getFee()).isEqualByComparingTo(expectedFee);
        } catch (BusinessException e) {
            fail("TaxCalculator threw BusinessException unexpectedly: " + e.getMessage());
        }

        verify(repo, times(1)).findById(5L);
        verify(repo, times(1)).save(existing);
    }

    @Test
    @DisplayName("update() should return null when entity not found")
    void update_nonExisting_shouldReturnNull() {
        Transaction updated = new Transaction();
        updated.setOriginAccount("X");
        when(repo.findById(99L)).thenReturn(Optional.empty());

        Transaction result = service.update(99L, updated);

        assertThat(result).isNull();
        verify(repo, times(1)).findById(99L);
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("delete() should return true when entity exists and delete it")
    void delete_existing_shouldReturnTrueAndDelete() {
        when(repo.existsById(7L)).thenReturn(true);
        doNothing().when(repo).deleteById(7L);

        boolean result = service.delete(7L);

        assertThat(result).isTrue();
        verify(repo, times(1)).existsById(7L);
        verify(repo, times(1)).deleteById(7L);
    }

    @Test
    @DisplayName("delete() should return false when entity does not exist")
    void delete_nonExisting_shouldReturnFalse() {
        when(repo.existsById(8L)).thenReturn(false);

        boolean result = service.delete(8L);

        assertThat(result).isFalse();
        verify(repo, times(1)).existsById(8L);
        verify(repo, never()).deleteById(anyLong());
    }
}

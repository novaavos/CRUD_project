package natixis.crud_project.transfer.services;

import natixis.crud_project.transfer.exceptions.BusinessException;
import natixis.crud_project.transfer.models.Transaction;
import natixis.crud_project.transfer.repositories.TransactionRepository;
import natixis.crud_project.transfer.utils.TaxCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public Transaction create(Transaction t) {
        t.setCreatedAt(LocalDateTime.now());


        try {
            t.setFee(TaxCalculator.calculateFee(t.getAmount(), t.getScheduledDate()));
        } catch (BusinessException ex) {
            throw ex;
        }

        return repo.save(t);
    }

    public List<Transaction> listAll() {
        return repo.findAll();
    }

    public Optional<Transaction> findById(Long id) {
        return repo.findById(id);
    }

    public Transaction update(Long id, Transaction updated) {

        return repo.findById(id).map(existing -> {

            existing.setOriginAccount(updated.getOriginAccount());
            existing.setDestinationAccount(updated.getDestinationAccount());
            existing.setAmount(updated.getAmount());
            existing.setScheduledDate(updated.getScheduledDate());

            // recalcular fee
            existing.setFee(
                    TaxCalculator.calculateFee(existing.getAmount(), existing.getScheduledDate())
            );

            return repo.save(existing);

        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }


}

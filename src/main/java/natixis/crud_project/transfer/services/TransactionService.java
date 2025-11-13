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
}

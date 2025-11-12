package natixis.crud_project.transfer.repositories;


import natixis.crud_project.transfer.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

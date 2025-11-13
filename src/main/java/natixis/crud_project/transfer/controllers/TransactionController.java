package natixis.crud_project.transfer.controllers;

import natixis.crud_project.transfer.models.Transaction;
import natixis.crud_project.transfer.repositories.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionRepository repo;

    public TransactionController(TransactionRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction t) {
        t.setCreatedAt(java.time.LocalDateTime.now());
        Transaction saved = repo.save(t);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> list() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
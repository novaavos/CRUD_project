package natixis.crud_project.transfer.controllers;

import natixis.crud_project.transfer.models.Transaction;
import natixis.crud_project.transfer.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction t) {
        Transaction saved = service.create(t);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> list() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

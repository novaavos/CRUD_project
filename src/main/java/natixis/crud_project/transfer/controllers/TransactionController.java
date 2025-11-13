package natixis.crud_project.transfer.controllers;

import natixis.crud_project.transfer.dto.TransactionRequest;
import natixis.crud_project.transfer.dto.TransactionResponse;
import natixis.crud_project.transfer.models.Transaction;
import natixis.crud_project.transfer.services.TransactionService;
import natixis.crud_project.transfer.utils.TransactionMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@RequestBody @Valid TransactionRequest request) {
        Transaction entity = TransactionMapper.toEntity(request);
        Transaction saved = service.create(entity);
        return ResponseEntity.ok(TransactionMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> list() {
        List<TransactionResponse> result =
                service.listAll().stream()
                        .map(TransactionMapper::toResponse)
                        .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> get(@PathVariable Long id) {
        return service.findById(id)
                .map(TransactionMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

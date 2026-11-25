package com.example.purchase_order_api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderRepository repository;

    public PurchaseOrderController(PurchaseOrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return repository.findAll();
    }

    @GetMapping ("/{id}")
    public ResponseEntity<PurchaseOrder> getById(@PathVariable  Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public PurchaseOrder create(@RequestBody PurchaseOrder purchaseOrder) {
        return repository.save(purchaseOrder);
    }
}
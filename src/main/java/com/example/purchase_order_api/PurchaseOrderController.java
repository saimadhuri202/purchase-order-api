package com.example.purchase_order_api;

import com.example.purchase_order_api.dto.PurchaseOrderRequest;
import com.example.purchase_order_api.dto.PurchaseOrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public List<PurchaseOrderResponse> getAll() {
        return repository.findAll().stream()
                .map(PurchaseOrderResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(PurchaseOrderResponse::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PurchaseOrderResponse create(@RequestBody PurchaseOrderRequest request) {
        PurchaseOrder order = new PurchaseOrder();
        order.setPoNumber(request.poNumber());
        order.setVendorName(request.vendorName());
        order.setOrderDate(request.orderDate());
        order.setStatus(request.status());
        order.setTotalAmount(request.totalAmount());

        if (request.lines() != null) {
            for (PurchaseOrderRequest.LineRequest lr : request.lines()) {
                PurchaseOrderLine line = new PurchaseOrderLine();
                line.setItem(lr.item());
                line.setQuantity(lr.quantity());
                line.setUnitPrice(lr.unitPrice());
                line.setPurchaseOrder(order);
                order.getLines().add(line);
            }
        }
        return PurchaseOrderResponse.from(repository.save(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseOrderResponse> update(@PathVariable Long id, @RequestBody PurchaseOrder updated) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setPoNumber(updated.getPoNumber());
                    existing.setVendorName(updated.getVendorName());
                    existing.setOrderDate(updated.getOrderDate());
                    existing.setStatus(updated.getStatus());
                    existing.setTotalAmount(updated.getTotalAmount());
                    return ResponseEntity.ok(PurchaseOrderResponse.from(repository.save(existing)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
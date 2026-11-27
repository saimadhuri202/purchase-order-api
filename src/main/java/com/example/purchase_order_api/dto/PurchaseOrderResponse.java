package com.example.purchase_order_api.dto;

import com.example.purchase_order_api.PurchaseOrder;

import java.time.LocalDate;
import java.util.List;

public record PurchaseOrderResponse(
        Long id,
        String poNumber,
        String vendorName,
        LocalDate orderDate,
        String status,
        double totalAmount,
        List<LineResponse> lines
) {
    public record LineResponse(Long id, String item, int quantity, double unitPrice) {}

    public static PurchaseOrderResponse from(PurchaseOrder order) {
        List<LineResponse> lines = order.getLines().stream()
                .map(line -> new LineResponse(
                        line.getId(),
                        line.getItem(),
                        line.getQuantity(),
                        line.getUnitPrice()))
                .toList();

        return new PurchaseOrderResponse(
                order.getId(),
                order.getPoNumber(),
                order.getVendorName(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                lines);
    }
}
package com.example.purchase_order_api.dto;

import java.time.LocalDate;
import java.util.List;

public record PurchaseOrderRequest(
        String poNumber,
        String vendorName,
        LocalDate orderDate,
        String status,
        double totalAmount,
        List<LineRequest> lines
) {
    public record LineRequest(String item, int quantity, double unitPrice) {}
}
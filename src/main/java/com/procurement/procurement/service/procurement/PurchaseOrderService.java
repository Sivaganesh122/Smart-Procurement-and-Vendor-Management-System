package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    // ===================== CREATE =====================
    public PurchaseOrder createPurchaseOrder(PurchaseOrder purchaseOrder) {
        purchaseOrder.setCreatedAt(LocalDateTime.now());
        purchaseOrder.setUpdatedAt(LocalDateTime.now());
        if (purchaseOrder.getItems() != null) {
            for (com.procurement.procurement.entity.procurement.PurchaseOrderItem item : purchaseOrder.getItems()) {
                item.setPurchaseOrder(purchaseOrder);
            }
        }
        return purchaseOrderRepository.save(purchaseOrder);
    }

    // ===================== UPDATE =====================
    public PurchaseOrder updatePurchaseOrder(Long id, PurchaseOrder updatedPO) {

        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with id: " + id));

        po.setVendor(updatedPO.getVendor());
        po.setItems(updatedPO.getItems());
        po.setStatus(updatedPO.getStatus());
        po.setUpdatedAt(LocalDateTime.now());

        return purchaseOrderRepository.save(po);
    }

    // ===================== GET ALL =====================
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    // ===================== GET BY VENDOR =====================
    public List<PurchaseOrder> getPurchaseOrdersByVendor(Vendor vendor) {
        return purchaseOrderRepository.findByVendor(vendor);
    }

    // ===================== GET BY STATUS =====================
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    // ===================== GET BY PO NUMBER =====================
    public PurchaseOrder getPurchaseOrderByPoNumber(String poNumber) {
        return purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with PO number: " + poNumber));
    }
}
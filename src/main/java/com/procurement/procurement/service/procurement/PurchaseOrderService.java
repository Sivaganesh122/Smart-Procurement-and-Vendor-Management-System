package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.procurement.ApprovalRepository;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ApprovalRepository approvalRepository;

    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository,
                                ApprovalRepository approvalRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.approvalRepository = approvalRepository;
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
        List<PurchaseOrder> orders = purchaseOrderRepository.findAll();
        for (PurchaseOrder po : orders) {
            syncPurchaseOrderStatusWithApproval(po);
        }
        return orders;
    }

    // ===================== GET BY VENDOR =====================
    public List<PurchaseOrder> getPurchaseOrdersByVendor(Vendor vendor) {
        List<PurchaseOrder> orders = purchaseOrderRepository.findByVendor(vendor);
        for (PurchaseOrder po : orders) {
            syncPurchaseOrderStatusWithApproval(po);
        }
        return orders;
    }

    // ===================== GET BY STATUS =====================
    public List<PurchaseOrder> getPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    // ===================== GET BY PO NUMBER =====================
    public PurchaseOrder getPurchaseOrderByPoNumber(String poNumber) {
        PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() ->
                        new RuntimeException("Purchase Order not found with PO number: " + poNumber));
        syncPurchaseOrderStatusWithApproval(po);
        return po;
    }

    private void syncPurchaseOrderStatusWithApproval(PurchaseOrder po) {
        if (po != null && po.getId() != null) {
            List<Approval> approvals = approvalRepository.findByPurchaseOrderId(po.getId());
            if (approvals != null && !approvals.isEmpty()) {
                Approval latestApproval = approvals.get(approvals.size() - 1);
                if (latestApproval.getStatus() != null && !latestApproval.getStatus().equalsIgnoreCase(po.getStatus())) {
                    po.setStatus(latestApproval.getStatus());
                    po.setUpdatedAt(LocalDateTime.now());
                    purchaseOrderRepository.save(po);
                }
            }
        }
    }
}
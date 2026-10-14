package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.procurement.Requisition;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.procurement.ApprovalRepository;
import com.procurement.procurement.repository.procurement.PurchaseOrderRepository;
import com.procurement.procurement.repository.procurement.RequisitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final RequisitionRepository requisitionRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public ApprovalService(ApprovalRepository approvalRepository,
                           RequisitionRepository requisitionRepository,
                           PurchaseOrderRepository purchaseOrderRepository) {
        this.approvalRepository = approvalRepository;
        this.requisitionRepository = requisitionRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    public Approval createApproval(Approval approval) {
        approval.setStatus("PENDING");
        approval.setApprovedAt(null);
        return approvalRepository.save(approval);
    }

    public Approval updateApprovalStatus(Long approvalId, String status, String comments, User approver) {

        Approval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() ->
                        new RuntimeException("Approval not found with id: " + approvalId));

        approval.setStatus(status);

        if (comments != null && !comments.trim().isEmpty()) {
            approval.setComments(comments.trim());
        }

        if (approver != null) {
            approval.setApprover(approver);
        }

        if ("APPROVED".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
            approval.setApprovedAt(LocalDateTime.now());
        }

        // Sync status with linked Requisition
        if (approval.getRequisition() != null) {
            Requisition req = approval.getRequisition();
            req.setStatus(status);
            req.setUpdatedAt(LocalDateTime.now());
            requisitionRepository.save(req);
        }

        // Sync status with linked PurchaseOrder
        if (approval.getPurchaseOrder() != null) {
            PurchaseOrder po = approval.getPurchaseOrder();
            po.setStatus(status);
            po.setUpdatedAt(LocalDateTime.now());
            purchaseOrderRepository.save(po);
        }

        return approvalRepository.save(approval);
    }

    public Approval updateApprovalStatus(Long approvalId, String status) {
        return updateApprovalStatus(approvalId, status, null, null);
    }

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    public List<Approval> getApprovalsByPurchaseOrderId(Long purchaseOrderId) {
        return approvalRepository.findByPurchaseOrderId(purchaseOrderId);
    }

    public List<Approval> getApprovalsByRequisitionId(Long requisitionId) {
        return approvalRepository.findByRequisitionId(requisitionId);
    }
}
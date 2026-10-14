package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.entity.procurement.Requisition;
import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.procurement.RequisitionRepository;
import com.procurement.procurement.repository.user.UserRepository;
import com.procurement.procurement.service.audit.AuditService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final ApprovalService approvalService;

    public RequisitionService(RequisitionRepository requisitionRepository,
                              UserRepository userRepository,
                              AuditService auditService,
                              ApprovalService approvalService) {
        this.requisitionRepository = requisitionRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.approvalService = approvalService;
    }

    // ===================== CREATE =====================
    public Requisition createRequisition(Requisition requisition) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        requisition.setRequestedBy(currentUser);
        requisition.setStatus("PENDING");
        requisition.setCreatedAt(LocalDateTime.now());
        requisition.setUpdatedAt(LocalDateTime.now());

        if (requisition.getItems() != null) {
            for (com.procurement.procurement.entity.procurement.RequisitionItem item : requisition.getItems()) {
                item.setRequisition(requisition);
            }
        }

        Requisition saved = requisitionRepository.save(requisition);

        // 🔥 AUTO CREATE APPROVAL
        Approval approval = new Approval();
        approval.setRequisition(saved);
        approval.setStatus("PENDING");
        approvalService.createApproval(approval);

        auditService.log(
                "Requisition",
                saved.getId(),
                "CREATE",
                "Requisition created and approval generated"
        );

        return saved;
    }

    // ===================== UPDATE =====================
    public Requisition updateRequisition(Long id, Requisition updatedReq) {

        Requisition req = requisitionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Requisition not found with id: " + id));

        req.setRequisitionNumber(updatedReq.getRequisitionNumber());
        req.setItems(updatedReq.getItems());
        req.setStatus(updatedReq.getStatus());
        req.setUpdatedAt(LocalDateTime.now());

        Requisition saved = requisitionRepository.save(req);

        auditService.log(
                "Requisition",
                saved.getId(),
                "UPDATE",
                "Requisition updated successfully"
        );

        return saved;
    }

    public List<Requisition> getAllRequisitions() {
        List<Requisition> list = requisitionRepository.findAll();
        for (Requisition req : list) {
            syncRequisitionStatusWithApproval(req);
        }
        return list;
    }

    public List<Requisition> getMyRequisitions() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Requisition> list = requisitionRepository.findByRequestedBy(currentUser);
        for (Requisition req : list) {
            syncRequisitionStatusWithApproval(req);
        }
        return list;
    }

    public List<Requisition> getRequisitionsByStatus(String status) {
        return requisitionRepository.findByStatus(status);
    }

    private void syncRequisitionStatusWithApproval(Requisition req) {
        if (req != null && req.getId() != null) {
            List<Approval> approvals = approvalService.getApprovalsByRequisitionId(req.getId());
            if (approvals != null && !approvals.isEmpty()) {
                Approval latestApproval = approvals.get(approvals.size() - 1);
                if (latestApproval.getStatus() != null && !latestApproval.getStatus().equalsIgnoreCase(req.getStatus())) {
                    req.setStatus(latestApproval.getStatus());
                    req.setUpdatedAt(LocalDateTime.now());
                    requisitionRepository.save(req);
                }
            }
        }
    }
}
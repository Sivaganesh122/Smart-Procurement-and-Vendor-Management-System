package com.procurement.procurement.service.audit;

import com.procurement.procurement.entity.audit.AuditLog;
import com.procurement.procurement.repository.audit.AuditLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // ===================== CREATE AUDIT ENTRY =====================
    public void log(String entityName,
                    Long entityId,
                    String action,
                    String details) {

        String username = "SYSTEM";

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            username = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
        }

        AuditLog auditLog = new AuditLog(
                entityName,
                entityId,
                action,
                username,
                LocalDateTime.now(),
                details
        );

        auditLogRepository.save(auditLog);
    }

    // ===================== FETCH METHODS =====================
    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }

    public List<AuditLog> getByEntity(String entityName) {
        return auditLogRepository.findByEntityName(entityName);
    }

    public List<AuditLog> getByUser(String username) {
        return auditLogRepository.findByPerformedBy(username);
    }

    public List<AuditLog> getByEntityAndId(String entityName, Long entityId) {
        return auditLogRepository.findByEntityNameAndEntityId(entityName, entityId);
    }
}

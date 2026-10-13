package com.procurement.procurement.repository.audit;

import com.procurement.procurement.entity.audit.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Basic filters
    List<AuditLog> findByEntityName(String entityName);

    List<AuditLog> findByPerformedBy(String performedBy);

    List<AuditLog> findByEntityNameAndEntityId(String entityName, Long entityId);

    // Pagination support (VERY IMPORTANT in production)
    Page<AuditLog> findByEntityName(String entityName, Pageable pageable);

    Page<AuditLog> findByPerformedBy(String performedBy, Pageable pageable);

    // Filter by date range
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

}

package com.procurement.procurement.controller.procurement;

import com.procurement.procurement.entity.procurement.Requisition;
import com.procurement.procurement.repository.procurement.RequisitionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/procurement/requisition")
public class RequisitionController {

    private final RequisitionRepository requisitionRepository;

    public RequisitionController(RequisitionRepository requisitionRepository) {
        this.requisitionRepository = requisitionRepository;
    }

    @PostMapping
    public ResponseEntity<Requisition> create(@RequestBody Requisition requisition) {
        requisition.setStatus("PENDING");
        return ResponseEntity.ok(requisitionRepository.save(requisition));
    }

    @GetMapping
    public ResponseEntity<List<Requisition>> getAll() {
        return ResponseEntity.ok(requisitionRepository.findAll());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestParam String status) {

        Optional<Requisition> reqOpt = requisitionRepository.findById(id);
        if (reqOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Requisition not found");
        }

        Requisition requisition = reqOpt.get();
        requisition.setStatus(status);
        requisitionRepository.save(requisition);

        return ResponseEntity.ok("Requisition status updated to " + status);
    }
}
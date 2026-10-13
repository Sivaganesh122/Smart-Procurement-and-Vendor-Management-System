package com.procurement.procurement.controller.vendor;

import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.service.vendor.VendorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // ================= CREATE VENDOR =================
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROCUREMENT_MANAGER')")
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {

        if (vendor.getStatus() == null) {
            vendor.setStatus("ACTIVE");
        }

        Vendor saved = vendorService.createVendor(vendor);
        return ResponseEntity.ok(saved);
    }

    // ================= GET ALL VENDORS =================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','PROCUREMENT_MANAGER')")
    public ResponseEntity<List<Vendor>> getAllVendors() {

        return ResponseEntity.ok(vendorService.getAllVendors());
    }

    // ================= GET VENDOR BY ID =================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROCUREMENT_MANAGER')")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {

        return ResponseEntity.ok(vendorService.getVendorById(id));
    }

    // ================= UPDATE VENDOR =================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id,
                                               @RequestBody Vendor vendor) {

        Vendor updated = vendorService.updateVendor(id, vendor);
        return ResponseEntity.ok(updated);
    }

    // ================= DELETE VENDOR =================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {

        vendorService.deleteVendor(id);
        return ResponseEntity.ok("Vendor deleted successfully");
    }
}
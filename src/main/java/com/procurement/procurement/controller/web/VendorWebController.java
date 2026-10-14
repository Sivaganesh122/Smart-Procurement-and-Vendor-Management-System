package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.service.vendor.VendorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vendor")   // 🔥 IMPORTANT
public class VendorWebController {

    private final VendorService vendorService;

    public VendorWebController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    // ===================== Load Vendor Page =====================
    @GetMapping
    public String vendorPage(Model model) {

        List<Vendor> vendors = vendorService.getAllVendors();
        model.addAttribute("vendors", vendors);

        return "vendor";   // 🔥 templates/vendor.html
    }

    // ===================== Create Vendor =====================
    @PostMapping
    public String createVendor(@ModelAttribute Vendor vendor) {

        if (vendor.getStatus() == null || vendor.getStatus().trim().isEmpty()) {
            vendor.setStatus("ACTIVE");  // default status
        }
        vendorService.createVendor(vendor);

        return "redirect:/vendor";
    }

    // ===================== Delete Vendor =====================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteVendor(@PathVariable Long id) {

        vendorService.deleteVendor(id);

        return "redirect:/vendor";
    }
}
package com.procurement.procurement.controller.web;

import com.procurement.procurement.service.procurement.ApprovalService;
import com.procurement.procurement.service.procurement.PurchaseOrderService;
import com.procurement.procurement.service.procurement.RequisitionService;
import com.procurement.procurement.service.vendor.VendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    private final VendorService vendorService;
    private final RequisitionService requisitionService;
    private final PurchaseOrderService purchaseOrderService;
    private final ApprovalService approvalService;

    public WebController(VendorService vendorService,
                         RequisitionService requisitionService,
                         PurchaseOrderService purchaseOrderService,
                         ApprovalService approvalService) {
        this.vendorService = vendorService;
        this.requisitionService = requisitionService;
        this.purchaseOrderService = purchaseOrderService;
        this.approvalService = approvalService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("vendorCount", vendorService.getAllVendors().size());
        model.addAttribute("requisitionCount", requisitionService.getAllRequisitions().size());
        model.addAttribute("poCount", purchaseOrderService.getAllPurchaseOrders().size());

        long pendingApprovals = approvalService.getAllApprovals().stream()
                .filter(a -> "PENDING".equalsIgnoreCase(a.getStatus()))
                .count();
        model.addAttribute("pendingApprovalCount", pendingApprovals);

        return "dashboard";
    }
}
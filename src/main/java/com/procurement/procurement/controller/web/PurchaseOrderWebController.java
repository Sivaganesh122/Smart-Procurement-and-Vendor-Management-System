package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.entity.procurement.PurchaseOrderItem;
import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.service.procurement.ApprovalService;
import com.procurement.procurement.service.procurement.PurchaseOrderService;
import com.procurement.procurement.service.vendor.VendorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/purchase-order")
public class PurchaseOrderWebController {

    private final PurchaseOrderService purchaseOrderService;
    private final VendorService vendorService;
    private final ApprovalService approvalService;

    public PurchaseOrderWebController(PurchaseOrderService purchaseOrderService,
                                     VendorService vendorService,
                                     ApprovalService approvalService) {
        this.purchaseOrderService = purchaseOrderService;
        this.vendorService = vendorService;
        this.approvalService = approvalService;
    }

    @GetMapping
    public String viewPurchaseOrders(Model model) {
        model.addAttribute("orders", purchaseOrderService.getAllPurchaseOrders());
        model.addAttribute("vendors", vendorService.getAllVendors());
        return "purchase-order";
    }

    @PostMapping
    public String createPO(
            @RequestParam String poNumber,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false, defaultValue = "1") Integer quantity,
            @RequestParam(required = false, defaultValue = "0.0") Double unitPrice) {

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(poNumber);
        po.setStatus("PENDING");

        if (vendorId != null) {
            try {
                Vendor vendor = vendorService.getVendorById(vendorId);
                po.setVendor(vendor);
            } catch (Exception ignored) {
            }
        }

        if (productName != null && !productName.trim().isEmpty()) {
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProductName(productName.trim());
            item.setQuantity(quantity != null && quantity > 0 ? quantity : 1);
            item.setUnitPrice(unitPrice != null && unitPrice >= 0 ? unitPrice : 0.0);
            item.setPurchaseOrder(po);

            List<PurchaseOrderItem> items = new ArrayList<>();
            items.add(item);
            po.setItems(items);
        }

        PurchaseOrder saved = purchaseOrderService.createPurchaseOrder(po);

        // Auto create approval record for PO
        Approval approval = new Approval();
        approval.setPurchaseOrder(saved);
        approval.setStatus("PENDING");
        approvalService.createApproval(approval);

        return "redirect:/purchase-order";
    }
}
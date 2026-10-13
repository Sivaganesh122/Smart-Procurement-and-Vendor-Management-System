package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.procurement.Requisition;
import com.procurement.procurement.entity.procurement.RequisitionItem;
import com.procurement.procurement.service.procurement.RequisitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/requisition")
public class RequisitionWebController {

    private final RequisitionService requisitionService;

    public RequisitionWebController(RequisitionService requisitionService) {
        this.requisitionService = requisitionService;
    }

    // Load page
    @GetMapping
    public String requisitionPage(Model model) {

        List<Requisition> requisitions = requisitionService.getAllRequisitions();
        model.addAttribute("requisitions", requisitions);

        return "requisition";
    }

    // Create requisition
    @PostMapping
    public String createRequisition(
            @RequestParam String requisitionNumber,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "1") Integer quantity,
            @RequestParam(required = false, defaultValue = "0.0") Double unitPrice) {

        Requisition requisition = new Requisition();
        requisition.setRequisitionNumber(requisitionNumber);
        requisition.setStatus("PENDING");

        if (itemName != null && !itemName.trim().isEmpty()) {
            RequisitionItem item = new RequisitionItem();
            item.setItemName(itemName.trim());
            item.setDescription(description != null ? description.trim() : "");
            item.setQuantity(quantity != null && quantity > 0 ? quantity : 1);
            item.setUnitPrice(unitPrice != null && unitPrice >= 0 ? unitPrice : 0.0);
            item.setRequisition(requisition);

            List<RequisitionItem> items = new ArrayList<>();
            items.add(item);
            requisition.setItems(items);
        }

        requisitionService.createRequisition(requisition);

        return "redirect:/requisition";
    }
}
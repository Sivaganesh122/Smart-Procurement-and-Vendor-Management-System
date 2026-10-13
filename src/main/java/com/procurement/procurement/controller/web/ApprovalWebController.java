package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.user.User;
import com.procurement.procurement.repository.user.UserRepository;
import com.procurement.procurement.service.procurement.ApprovalService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/approval")
public class ApprovalWebController {

    private final ApprovalService approvalService;
    private final UserRepository userRepository;

    public ApprovalWebController(ApprovalService approvalService, UserRepository userRepository) {
        this.approvalService = approvalService;
        this.userRepository = userRepository;
    }

    // ================= VIEW PAGE =================
    @GetMapping
    public String viewApprovals(Model model) {
        model.addAttribute("approvals",
                approvalService.getAllApprovals());
        return "approval";
    }

    // ================= UPDATE STATUS =================
    @PostMapping("/update")
    public String updateApprovalStatus(
            @RequestParam Long id,
            @RequestParam String status,
            @RequestParam(required = false) String comments) {

        User currentUser = null;
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            currentUser = userRepository.findByUsername(username).orElse(null);
        }

        approvalService.updateApprovalStatus(id, status, comments, currentUser);

        return "redirect:/approval";
    }
}
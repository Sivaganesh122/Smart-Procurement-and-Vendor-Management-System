package com.procurement.procurement.controller.report;

import com.procurement.procurement.service.report.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<byte[]> generateVendorReport(
            @PathVariable Long vendorId,
            @RequestParam(defaultValue = "pdf") String format) {

        byte[] report = reportService.generateVendorReport(vendorId, format);

        String extension =
                format.equalsIgnoreCase("excel") ? "xlsx" : "pdf";

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=vendor-" + vendorId + "." + extension
                )
                .body(report);
    }
}

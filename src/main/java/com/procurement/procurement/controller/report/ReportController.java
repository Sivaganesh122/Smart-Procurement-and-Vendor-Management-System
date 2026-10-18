package com.procurement.procurement.controller.report;

import com.procurement.procurement.service.report.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

        boolean isExcel = format.equalsIgnoreCase("excel");
        String extension = isExcel ? "xlsx" : "pdf";
        MediaType mediaType = isExcel
                ? MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=vendor-" + vendorId + "." + extension
                )
                .body(report);
    }

    @GetMapping({"/vendors", "/vendor/all"})
    public ResponseEntity<byte[]> generateAllVendorsReport(
            @RequestParam(defaultValue = "pdf") String format) {

        byte[] report = reportService.generateAllVendorsReport(format);

        boolean isExcel = format.equalsIgnoreCase("excel");
        String extension = isExcel ? "xlsx" : "pdf";
        MediaType mediaType = isExcel
                ? MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                : MediaType.APPLICATION_PDF;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=all-vendors." + extension
                )
                .body(report);
    }
}

package com.procurement.procurement.service.report;

import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.vendor.VendorRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final VendorRepository vendorRepository;

    public ReportService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public byte[] generateVendorReport(Long vendorId, String format) {

        try {
            // Fetch only the selected vendor from the database.
            Vendor vendor = vendorRepository.findById(vendorId)
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));

            // JasperReports expects a collection as its data source.
            List<Vendor> vendors = Collections.singletonList(vendor);
            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(vendors);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "Vendor Information Report");

            InputStream reportStream =
                    new ClassPathResource("jasper/vendor_report.jrxml")
                            .getInputStream();

            JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
            JasperReport jasperReport =
                    JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    dataSource
            );

            // Export to Excel.
            if ("excel".equalsIgnoreCase(format)) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(
                        new SimpleOutputStreamExporterOutput(outputStream)
                );
                exporter.exportReport();

                return outputStream.toByteArray();
            }

            // Export to PDF by default.
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Vendor report generation failed", e);
        }
    }
}

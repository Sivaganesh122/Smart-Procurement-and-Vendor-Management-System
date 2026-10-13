package com.procurement.procurement.service.vendor;

import com.procurement.procurement.entity.vendor.Vendor;
import com.procurement.procurement.repository.vendor.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    // ===================== CREATE =====================
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    // ===================== UPDATE =====================
    public Vendor updateVendor(Long id, Vendor updatedVendor) {

        Optional<Vendor> optionalVendor = vendorRepository.findById(id);

        if (optionalVendor.isPresent()) {

            Vendor vendor = optionalVendor.get();

            vendor.setName(updatedVendor.getName());
            vendor.setEmail(updatedVendor.getEmail());
            vendor.setContactNumber(updatedVendor.getContactNumber());  // FIXED
            vendor.setAddress(updatedVendor.getAddress());
            vendor.setStatus(updatedVendor.getStatus());

            return vendorRepository.save(vendor);
        }

        throw new RuntimeException("Vendor not found with id: " + id);
    }

    // ===================== GET ALL =====================
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    // ===================== GET BY ID =====================
    public Vendor getVendorById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Vendor not found with id: " + id));
    }

    // ===================== SEARCH =====================
    public List<Vendor> getVendorsByName(String name) {
        return vendorRepository.findByNameContainingIgnoreCase(name);
    }

    // ===================== DELETE =====================
    public void deleteVendor(Long id) {
        Vendor vendor = getVendorById(id);
        vendorRepository.delete(vendor);
    }
}
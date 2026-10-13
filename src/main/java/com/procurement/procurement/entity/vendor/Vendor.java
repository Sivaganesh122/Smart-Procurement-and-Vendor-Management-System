package com.procurement.procurement.entity.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String contactNumber;
    private String address;
    private String status; // ACTIVE, INACTIVE, PENDING_APPROVAL

    // Prevent infinite recursion in REST response
    @JsonIgnore
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorDocument> documents;

    @JsonIgnore
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorRating> ratings;

    public Vendor() {
    }

    public Vendor(String name, String email, String contactNumber,
                  String address, String status) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.status = status;
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VendorDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<VendorDocument> documents) {
        this.documents = documents;
    }

    public List<VendorRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<VendorRating> ratings) {
        this.ratings = ratings;
    }
}
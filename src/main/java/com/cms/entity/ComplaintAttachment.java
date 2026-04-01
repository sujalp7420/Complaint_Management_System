package com.cms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_attachments")
public class ComplaintAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "complaint_id")
    private Complaints complaint;

    @Column(length = 255)
    @NotBlank(message = "File name is required")
    private String fileName;

    @Column(length = 255)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = true)
    private Users uploadedBy;

    @Transient
    private Integer uploadedById;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setComplaint(Complaints complaint) {
        this.complaint = complaint;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setUploadedBy(Users uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Integer getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(Integer uploadedById) {
        this.uploadedById = uploadedById;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Integer getId() {
        return id;
    }

    @JsonIgnore
    public Complaints getComplaint() {
        return complaint;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    @JsonIgnore
    public Users getUploadedBy() {
        return uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}

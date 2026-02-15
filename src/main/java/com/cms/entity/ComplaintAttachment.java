package com.cms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_attachments")
public class ComplaintAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaints complaint;

    @Column(length = 255)
    private String fileName;

    @Column(length = 255)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private Users uploadedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    public void setUploadedAt() {
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

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Integer getId() {
        return id;
    }

    public Complaints getComplaint() {
        return complaint;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Users getUploadedBy() {
        return uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}

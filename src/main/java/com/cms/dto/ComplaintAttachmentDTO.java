package com.cms.dto;

import java.time.LocalDateTime;

public class ComplaintAttachmentDTO {

    private Integer id;
    private Integer complaintId;
    private String fileName;
    private String filePath;
    private Integer uploadedById;
    private LocalDateTime uploadedAt;

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getComplaintId() { return complaintId; }
    public void setComplaintId(Integer complaintId) { this.complaintId = complaintId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Integer getUploadedById() { return uploadedById; }
    public void setUploadedById(Integer uploadedById) { this.uploadedById = uploadedById; }

    public LocalDateTime getUploadedAt(){ return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
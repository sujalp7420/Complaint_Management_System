package com.cms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "complaints")
public class Complaints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "complaint_number", nullable = false, length = 20)
    private String complaintNumber;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private Users createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private Users assignedTo;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL)
    private List<ComplaintComment> comments;


    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL)
    private List<ComplaintStatusHistory> statusHistory;

    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL)
    private List<ComplaintAttachment> attachments;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setComplaintNumber(String complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public void setAssignedTo(Users assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setComments(List<ComplaintComment> comments) {
        this.comments = comments;
    }

    public void setStatusHistory(List<ComplaintStatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public void setAttachments(List<ComplaintAttachment> attachments) {
        this.attachments = attachments;
    }

    public Integer getId() {
        return id;
    }

    public String getComplaintNumber() {
        return complaintNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public Category getCategory() {
        return category;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public Users getAssignedTo() {
        return assignedTo;
    }

    public List<ComplaintComment> getComments() {
        return comments;
    }

    public List<ComplaintStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public List<ComplaintAttachment> getAttachments() {
        return attachments;
    }
}

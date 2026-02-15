package com.cms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_status_history")
public class ComplaintStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "old_status", length = 50)
    private String oldStatus;

    @Column(name = "new_status", length = 50)
    private String newStatus;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    // ✅ Relationship with Complaint
    @ManyToOne
    @JoinColumn(name = "complaint_id")
    private Complaints complaint;

    // ✅ Relationship with User
    @ManyToOne
    @JoinColumn(name = "changed_by")
    private Users changedBy;

    // Auto set timestamp
    @PrePersist
    public void setChangedAt() {
        this.changedAt = LocalDateTime.now();
    }

    // ---------------- GETTERS & SETTERS ----------------

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public Complaints getComplaint() { return complaint; }
    public void setComplaint(Complaints complaint) { this.complaint = complaint; }

    public Users getChangedBy() { return changedBy; }
    public void setChangedBy(Users changedBy) { this.changedBy = changedBy; }
}

package com.cms.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_comments")
public class ComplaintComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id")
    private Complaints complaint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setComplaint(Complaints complaint) {
        this.complaint = complaint;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public Complaints getComplaint() {
        return complaint;
    }

    public Users getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

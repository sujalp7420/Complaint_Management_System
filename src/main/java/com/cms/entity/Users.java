package com.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_user")
    private StatusUser statusUser = StatusUser.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "createdBy")
    @JsonIgnore
    private List<Complaints> complaintsCreated;

    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore
    private List<Complaints> complaintsAssigned;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<ComplaintComment> comments;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public StatusUser getStatusUser() { return statusUser; }
    public void setStatusUser(StatusUser statusUser) { this.statusUser = statusUser; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Complaints> getComplaintsCreated() { return complaintsCreated; }
    public void setComplaintsCreated(List<Complaints> complaintsCreated) { this.complaintsCreated = complaintsCreated; }

    public List<Complaints> getComplaintsAssigned() { return complaintsAssigned; }
    public void setComplaintsAssigned(List<Complaints> complaintsAssigned) { this.complaintsAssigned = complaintsAssigned; }

    public List<ComplaintComment> getComments() { return comments; }
    public void setComments(List<ComplaintComment> comments) { this.comments = comments; }

}
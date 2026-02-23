package com.cms.dto;

import com.cms.entity.Role;
import com.cms.entity.StatusUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Integer id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    private String phone;

    @NotNull(message = "Role is required")
    private Role role;

    private StatusUser statusUser = StatusUser.ACTIVE;

    // Constructors
    public UserDTO() {}

    public UserDTO(Integer id, String name, String email, String phone, Role role, StatusUser statusUser) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.statusUser = statusUser;
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
}
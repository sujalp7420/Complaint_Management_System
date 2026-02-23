package com.cms.controller;

import com.cms.dto.ComplaintDTO;
import com.cms.dto.UserDTO;
import com.cms.entity.Status;
import com.cms.service.ComplaintService;
import com.cms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ComplaintService complaintService;

    // ================= USERS =================

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    // ================= COMPLAINTS =================

    @GetMapping("/complaints")
    public List<ComplaintDTO> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @PutMapping("/complaints/{id}/status")
    public ComplaintDTO updateStatus(
            @PathVariable Integer id,
            @RequestParam Status status
    ) {
        return complaintService.updateComplaintStatus(id, status);
    }

}
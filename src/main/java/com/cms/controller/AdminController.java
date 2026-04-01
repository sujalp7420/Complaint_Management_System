package com.cms.controller;

import com.cms.dto.ComplaintDTO;
import com.cms.dto.UserDTO;
import com.cms.entity.Status;
import com.cms.service.ComplaintService;
import com.cms.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final ComplaintService complaintService;

    public AdminController(UserService userService, ComplaintService complaintService) {
        this.userService = userService;
        this.complaintService = complaintService;
    }

    // ================= USERS =================

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    // ================= COMPLAINTS =================

    @GetMapping("/complaints")
    public List<ComplaintDTO> getAllComplaints() {
        return complaintService.getAllComplaints();
    }

    @RequestMapping(value = "/complaints/{id}/status", method = { RequestMethod.PUT, RequestMethod.PATCH })
    public ComplaintDTO updateStatus(
            @PathVariable Integer id,
            @RequestParam Status status
    ) {
        return complaintService.updateComplaintStatus(id, status);
    }

}
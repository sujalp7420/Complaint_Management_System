package com.cms.controller;

import com.cms.dto.ComplaintDTO;
import com.cms.entity.Status;
import com.cms.service.ComplaintService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<ComplaintDTO> createComplaint(@Valid @RequestBody ComplaintDTO complaintDTO) {
        ComplaintDTO createdComplaint = complaintService.createComplaint(complaintDTO);
        return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDTO> getComplaint(@PathVariable Integer id) {
        ComplaintDTO complaint = complaintService.getComplaintById(id);
        return ResponseEntity.ok(complaint);
    }

    @GetMapping
    public ResponseEntity<List<ComplaintDTO>> getAllComplaints() {
        List<ComplaintDTO> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ComplaintDTO>> getByStatus(@PathVariable Status status) {
        List<ComplaintDTO> complaints = complaintService.getComplaintsByStatus(status);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ComplaintDTO>> getByUser(@PathVariable Integer userId) {
        List<ComplaintDTO> complaints = complaintService.getComplaintsByUser(userId);
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/assigned/{userId}")
    public ResponseEntity<List<ComplaintDTO>> getAssignedTo(@PathVariable Integer userId) {
        List<ComplaintDTO> complaints = complaintService.getComplaintsAssignedTo(userId);
        return ResponseEntity.ok(complaints);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComplaintDTO> updateComplaint(@PathVariable Integer id, @Valid @RequestBody ComplaintDTO complaintDTO) {
        ComplaintDTO updatedComplaint = complaintService.updateComplaint(id, complaintDTO);
        return ResponseEntity.ok(updatedComplaint);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintDTO> updateStatus(@PathVariable Integer id, @RequestParam Status status) {
        ComplaintDTO updatedComplaint = complaintService.updateComplaintStatus(id, status);
        return ResponseEntity.ok(updatedComplaint);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(@PathVariable Integer id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok("Complaint deleted successfully");
    }
}
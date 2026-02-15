package com.cms.controller;

import com.cms.entity.Complaints;
import com.cms.entity.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cms.service.ComplaintService;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public ResponseEntity<Complaints> createComplaint(@RequestBody Complaints complaint) {
        return ResponseEntity.ok(complaintService.createComplaint(complaint));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Complaints> getComplaint(@PathVariable Integer id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @GetMapping
    public ResponseEntity<List<Complaints>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Complaints>> getByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(complaintService.getComplaintsByStatus(status));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Complaints> updateStatus(@PathVariable Integer id,
                                                   @RequestParam Status status) {
        return ResponseEntity.ok(complaintService.updateComplaintStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(@PathVariable Integer id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}


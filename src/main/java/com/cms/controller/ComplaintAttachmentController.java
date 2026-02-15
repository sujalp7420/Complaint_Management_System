package com.cms.controller;

import com.cms.entity.ComplaintAttachment;
import com.cms.service.ComplaintAttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class ComplaintAttachmentController {

    private final ComplaintAttachmentService attachmentService;

    public ComplaintAttachmentController(ComplaintAttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/{complaintId}")
    public ResponseEntity<ComplaintAttachment> addAttachment(
            @PathVariable Integer complaintId,
            @RequestBody ComplaintAttachment attachment) {

        return ResponseEntity.ok(attachmentService.addAttachment(complaintId, attachment));
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<List<ComplaintAttachment>> getAttachments(
            @PathVariable Integer complaintId) {

        return ResponseEntity.ok(attachmentService.getAttachments(complaintId));
    }
}


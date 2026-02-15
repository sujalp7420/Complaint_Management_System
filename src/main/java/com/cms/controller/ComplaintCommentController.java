package com.cms.controller;

import com.cms.entity.ComplaintComment;
import com.cms.service.ComplaintCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class ComplaintCommentController {

    private final ComplaintCommentService commentService;

    public ComplaintCommentController(ComplaintCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{complaintId}")
    public ResponseEntity<ComplaintComment> addComment(
            @PathVariable Integer complaintId,
            @RequestBody ComplaintComment comment) {

        return ResponseEntity.ok(commentService.addComment(complaintId, comment));
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<List<ComplaintComment>> getComments(
            @PathVariable Integer complaintId) {

        return ResponseEntity.ok(commentService.getCommentsByComplaint(complaintId));
    }
}


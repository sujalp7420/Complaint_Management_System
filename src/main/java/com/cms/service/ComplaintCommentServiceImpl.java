package com.cms.service;

import com.cms.entity.ComplaintComment;
import com.cms.entity.Complaints;
import com.cms.repository.ComplaintCommentRepository;
import com.cms.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintCommentServiceImpl implements ComplaintCommentService {

    private final ComplaintCommentRepository commentRepository;
    private final ComplaintRepository complaintRepository;

    public ComplaintCommentServiceImpl(ComplaintCommentRepository commentRepository,
                                       ComplaintRepository complaintRepository) {
        this.commentRepository = commentRepository;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public ComplaintComment addComment(Integer complaintId, ComplaintComment comment) {

        Complaints complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        comment.setComplaint(complaint);

        return commentRepository.save(comment);
    }

    @Override
    public List<ComplaintComment> getCommentsByComplaint(Integer complaintId) {
        return commentRepository.findByComplaint_Id(complaintId);
    }
}


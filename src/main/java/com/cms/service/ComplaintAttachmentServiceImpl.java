package com.cms.service;

import com.cms.entity.ComplaintAttachment;
import com.cms.entity.Complaints;
import com.cms.repository.ComplaintAttachmentRepository;
import com.cms.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintAttachmentServiceImpl implements ComplaintAttachmentService {

    private final ComplaintAttachmentRepository attachmentRepository;
    private final ComplaintRepository complaintRepository;

    public ComplaintAttachmentServiceImpl(ComplaintAttachmentRepository attachmentRepository,
                                          ComplaintRepository complaintRepository) {
        this.attachmentRepository = attachmentRepository;
        this.complaintRepository = complaintRepository;
    }

    @Override
    public ComplaintAttachment addAttachment(Integer complaintId, ComplaintAttachment attachment) {

        Complaints complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        attachment.setComplaint(complaint);

        return attachmentRepository.save(attachment);
    }

    @Override
    public List<ComplaintAttachment> getAttachments(Integer complaintId) {
        return attachmentRepository.findByComplaint_Id(complaintId);
    }
}


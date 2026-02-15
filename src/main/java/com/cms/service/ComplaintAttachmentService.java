package com.cms.service;

import com.cms.entity.ComplaintAttachment;

import java.util.List;

public interface ComplaintAttachmentService {

    ComplaintAttachment addAttachment(Integer complaintId, ComplaintAttachment attachment);

    List<ComplaintAttachment> getAttachments(Integer complaintId);
}


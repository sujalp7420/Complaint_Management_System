package com.cms.service;

import com.cms.entity.ComplaintComment;

import java.util.List;

public interface ComplaintCommentService {

    ComplaintComment addComment(Integer complaintId, ComplaintComment comment);

    List<ComplaintComment> getCommentsByComplaint(Integer complaintId);
}


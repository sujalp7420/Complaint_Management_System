package com.cms.repository;

import com.cms.entity.ComplaintAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintAttachmentRepository extends JpaRepository<ComplaintAttachment, Integer> {

    List<ComplaintAttachment> findByComplaint_Id(Integer complaintId);
}


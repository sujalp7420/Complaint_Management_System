package com.cms.repository;

import com.cms.entity.ComplaintComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment, Integer> {

    List<ComplaintComment> findByComplaint_Id(Integer complaintId);
}

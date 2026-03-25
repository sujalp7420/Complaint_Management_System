package com.cms.repository;

import com.cms.entity.Complaints;
import com.cms.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaints, Integer> {
    List<Complaints> findByStatus(Status status);
    List<Complaints> findByCreatedBy_Id(Integer userId);
    List<Complaints> findByAssignedTo_Id(Integer userId);
    List<Complaints> findByCreatedBy_IdAndStatus(Integer userId, Status status);
}
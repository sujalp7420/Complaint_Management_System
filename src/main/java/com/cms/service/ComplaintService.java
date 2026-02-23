package com.cms.service;

import com.cms.dto.ComplaintDTO;
import com.cms.entity.Complaints;
import com.cms.entity.Status;

import java.util.List;
public interface ComplaintService {
    ComplaintDTO createComplaint(ComplaintDTO complaintDTO);
    ComplaintDTO getComplaintById(Integer id);
    List<ComplaintDTO> getAllComplaints();
    List<ComplaintDTO> getComplaintsByStatus(Status status);
    List<ComplaintDTO> getComplaintsByUser(Integer userId);
    List<ComplaintDTO> getComplaintsAssignedTo(Integer userId);
    ComplaintDTO updateComplaint(Integer id, ComplaintDTO complaintDTO);
    ComplaintDTO updateComplaintStatus(Integer id, Status status);
    void deleteComplaint(Integer id);
}
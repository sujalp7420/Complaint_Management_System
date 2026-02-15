package com.cms.service;

import com.cms.entity.Complaints;
import com.cms.entity.Status;

import java.util.List;

public interface ComplaintService {

    Complaints createComplaint(Complaints complaint);

    Complaints getComplaintById(Integer id);

    List<Complaints> getAllComplaints();

    List<Complaints> getComplaintsByStatus(Status status);

    Complaints updateComplaintStatus(Integer id, Status status);

    void deleteComplaint(Integer id);
}


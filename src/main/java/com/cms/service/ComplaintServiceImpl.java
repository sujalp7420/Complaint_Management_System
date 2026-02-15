package com.cms.service;

import com.cms.entity.Complaints;
import com.cms.entity.Status;
import com.cms.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @Override
    public Complaints createComplaint(Complaints complaint) {
        return complaintRepository.save(complaint);
    }

    @Override
    public Complaints getComplaintById(Integer id) {
        return complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
    }

    @Override
    public List<Complaints> getAllComplaints() {
        return complaintRepository.findAll();
    }

    @Override
    public List<Complaints> getComplaintsByStatus(Status status) {
        return complaintRepository.findByStatus(status);
    }

    @Override
    public Complaints updateComplaintStatus(Integer id, Status status) {
        Complaints complaint = getComplaintById(id);
        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }

    @Override
    public void deleteComplaint(Integer id) {
        complaintRepository.deleteById(id);
    }
}


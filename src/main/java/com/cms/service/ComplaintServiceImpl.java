package com.cms.service;

import com.cms.dto.ComplaintDTO;
import com.cms.entity.Complaints;
import com.cms.entity.Status;
import com.cms.entity.Category;
import com.cms.entity.Users;
import com.cms.exception.ResourceNotFoundException;
import com.cms.repository.ComplaintRepository;
import com.cms.repository.CategoryRepository;
import com.cms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository,
                                CategoryRepository categoryRepository,
                                UserRepository userRepository,
                                ModelMapper mapper) {
        this.complaintRepository = complaintRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ComplaintDTO createComplaint(ComplaintDTO complaintDTO) {

        Complaints complaint = new Complaints();

        complaint.setComplaintNumber(generateComplaintNumber());
        complaint.setTitle(complaintDTO.getTitle());
        complaint.setDescription(complaintDTO.getDescription());
        complaint.setPriority(complaintDTO.getPriority());
        complaint.setStatus(
                complaintDTO.getStatus() != null ?
                        complaintDTO.getStatus() : Status.OPEN
        );
        complaint.setCreatedAt(LocalDateTime.now());

        // Set Category
        Category category = categoryRepository.findById(complaintDTO.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + complaintDTO.getCategoryId()));
        complaint.setCategory(category);

        // Set CreatedBy
        Users createdBy = userRepository.findById(complaintDTO.getCreatedById())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + complaintDTO.getCreatedById()));
        complaint.setCreatedBy(createdBy);

        // Set AssignedTo (Optional)
        if (complaintDTO.getAssignedToId() != null) {
            Users assignedTo = userRepository.findById(complaintDTO.getAssignedToId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User not found with id: " + complaintDTO.getAssignedToId()));
            complaint.setAssignedTo(assignedTo);
        }

        Complaints savedComplaint = complaintRepository.save(complaint);

        return mapper.map(savedComplaint, ComplaintDTO.class);
    }

    @Override
    public ComplaintDTO getComplaintById(Integer id) {

        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found with id: " + id));

        return mapper.map(complaint, ComplaintDTO.class);
    }

    @Override
    public List<ComplaintDTO> getAllComplaints() {

        return complaintRepository.findAll()
                .stream()
                .map(c -> mapper.map(c, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getComplaintsByStatus(Status status) {

        return complaintRepository.findByStatus(status)
                .stream()
                .map(c -> mapper.map(c, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getComplaintsByUser(Integer userId) {

        return complaintRepository.findByCreatedBy_Id(userId)
                .stream()
                .map(c -> mapper.map(c, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getComplaintsAssignedTo(Integer userId) {

        return complaintRepository.findByAssignedTo_Id(userId)
                .stream()
                .map(c -> mapper.map(c, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ComplaintDTO updateComplaint(Integer id,
                                        ComplaintDTO complaintDTO) {

        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found with id: " + id));

        complaint.setTitle(complaintDTO.getTitle());
        complaint.setDescription(complaintDTO.getDescription());
        complaint.setPriority(complaintDTO.getPriority());

        if (complaintDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(complaintDTO.getCategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Category not found with id: " + complaintDTO.getCategoryId()));
            complaint.setCategory(category);
        }

        if (complaintDTO.getAssignedToId() != null) {
            Users assignedTo = userRepository.findById(complaintDTO.getAssignedToId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "User not found with id: " + complaintDTO.getAssignedToId()));
            complaint.setAssignedTo(assignedTo);
        }

        complaint.setUpdatedAt(LocalDateTime.now());

        Complaints updatedComplaint = complaintRepository.save(complaint);

        return mapper.map(updatedComplaint, ComplaintDTO.class);
    }

    @Override
    public ComplaintDTO updateComplaintStatus(Integer id,
                                              Status status) {

        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found with id: " + id));

        complaint.setStatus(status);

        if (status == Status.RESOLVED) {
            complaint.setResolvedAt(LocalDateTime.now());
        }

        complaint.setUpdatedAt(LocalDateTime.now());

        Complaints updatedComplaint = complaintRepository.save(complaint);

        return mapper.map(updatedComplaint, ComplaintDTO.class);
    }

    @Override
    public void deleteComplaint(Integer id) {

        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Complaint not found with id: " + id));

        complaintRepository.delete(complaint);
    }

    private String generateComplaintNumber() {
        return "CMP-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}

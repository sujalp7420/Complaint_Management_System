package com.cms.service;

import com.cms.dto.ComplaintDTO;
import com.cms.entity.*;
import com.cms.exception.ResourceNotFoundException;
import com.cms.repository.ComplaintRepository;
import com.cms.repository.CategoryRepository;
import com.cms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        complaint.setStatus(complaintDTO.getStatus() != null ? complaintDTO.getStatus() : Status.OPEN);

        // Set Category
        Category category = categoryRepository.findById(complaintDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + complaintDTO.getCategoryId()));
        complaint.setCategory(category);

        // Set CreatedBy - Get currently authenticated user
        Users createdBy = getCurrentUser();
        complaint.setCreatedBy(createdBy);

        // Set AssignedTo (Optional - Only Staff/Admin can assign)
        if (complaintDTO.getAssignedToId() != null) {
            Users assignedTo = userRepository.findById(complaintDTO.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + complaintDTO.getAssignedToId()));
            complaint.setAssignedTo(assignedTo);
        }

        Complaints savedComplaint = complaintRepository.save(complaint);
        return mapper.map(savedComplaint, ComplaintDTO.class);
    }

    @Override
    public ComplaintDTO getComplaintById(Integer id) {
        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));
        return mapper.map(complaint, ComplaintDTO.class);
    }

    @Override
    public List<ComplaintDTO> getAllComplaints() {
        Users currentUser = getCurrentUser();

        // If user is ADMIN or STAFF, return all complaints
        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.STAFF) {
            return complaintRepository.findAll()
                    .stream()
                    .map(c -> mapper.map(c, ComplaintDTO.class))
                    .collect(Collectors.toList());
        }

        // If CUSTOMER, return only their complaints
        return complaintRepository.findByCreatedBy_Id(currentUser.getId())
                .stream()
                .map(c -> mapper.map(c, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getComplaintsByStatus(Status status) {
        Users currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.STAFF) {
            return complaintRepository.findByStatus(status)
                    .stream()
                    .map(c -> mapper.map(c, ComplaintDTO.class))
                    .collect(Collectors.toList());
        }

        return complaintRepository.findByCreatedBy_IdAndStatus(currentUser.getId(), status)
                .stream()
                .map(c -> mapper.map(c, ComplaintDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getComplaintsByUser(Integer userId) {
        // Only ADMIN and STAFF can view complaints by specific user
        Users currentUser = getCurrentUser();
        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.STAFF) {
            return complaintRepository.findByCreatedBy_Id(userId)
                    .stream()
                    .map(c -> mapper.map(c, ComplaintDTO.class))
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("Access denied. Only ADMIN or STAFF can view complaints by specific user.");
    }

    @Override
    public List<ComplaintDTO> getComplaintsAssignedTo(Integer userId) {
        Users currentUser = getCurrentUser();

        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.STAFF) {
            return complaintRepository.findByAssignedTo_Id(userId)
                    .stream()
                    .map(c -> mapper.map(c, ComplaintDTO.class))
                    .collect(Collectors.toList());
        }

        // Customers can only see complaints assigned to them
        if (currentUser.getId().equals(userId)) {
            return complaintRepository.findByAssignedTo_Id(userId)
                    .stream()
                    .map(c -> mapper.map(c, ComplaintDTO.class))
                    .collect(Collectors.toList());
        }

        throw new RuntimeException("Access denied");
    }

    @Override
    public ComplaintDTO updateComplaint(Integer id, ComplaintDTO complaintDTO) {
        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        Users currentUser = getCurrentUser();

        // Only ADMIN, STAFF, or the complaint creator can update
        if (currentUser.getRole() != Role.ADMIN &&
                currentUser.getRole() != Role.STAFF &&
                !complaint.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied. You can only update your own complaints.");
        }

        complaint.setTitle(complaintDTO.getTitle());
        complaint.setDescription(complaintDTO.getDescription());
        complaint.setPriority(complaintDTO.getPriority());

        if (complaintDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(complaintDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + complaintDTO.getCategoryId()));
            complaint.setCategory(category);
        }

        // Only ADMIN and STAFF can reassign complaints
        if (complaintDTO.getAssignedToId() != null &&
                (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.STAFF)) {
            Users assignedTo = userRepository.findById(complaintDTO.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + complaintDTO.getAssignedToId()));
            complaint.setAssignedTo(assignedTo);
        }

        complaint.setUpdatedAt(LocalDateTime.now());

        Complaints updatedComplaint = complaintRepository.save(complaint);
        return mapper.map(updatedComplaint, ComplaintDTO.class);
    }

    @Override
    public ComplaintDTO updateComplaintStatus(Integer id, Status status) {
        Complaints complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        Users currentUser = getCurrentUser();

        // Only ADMIN and STAFF can update status
        if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.STAFF) {
            throw new RuntimeException("Access denied. Only ADMIN or STAFF can update complaint status.");
        }

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
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + id));

        Users currentUser = getCurrentUser();

        // Only ADMIN or the complaint creator can delete
        if (currentUser.getRole() != Role.ADMIN &&
                !complaint.getCreatedBy().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied. You can only delete your own complaints.");
        }

        complaintRepository.delete(complaint);
    }

    // Helper method to get current authenticated user
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private String generateComplaintNumber() {
        return "CMP-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
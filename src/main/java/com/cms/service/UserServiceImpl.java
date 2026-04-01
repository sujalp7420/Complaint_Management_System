package com.cms.service;

import com.cms.dto.UserDTO;
import com.cms.entity.Users;
import com.cms.entity.Role;
import com.cms.entity.StatusUser;
import com.cms.exception.ResourceNotFoundException;
import com.cms.exception.DuplicateResourceException;
import com.cms.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper mapper,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("User with email " + userDTO.getEmail() + " already exists");
        }

        Users user = new Users();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : Role.USER);
        user.setStatusUser(userDTO.getStatusUser() != null ? userDTO.getStatusUser() : StatusUser.ACTIVE);

        Users savedUser = userRepository.save(user);
        UserDTO response = mapper.map(savedUser, UserDTO.class);
        response.setPassword(null); // Don't return password
        return response;
    }

    @Override
    public UserDTO getUserById(Integer id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserDTO response = mapper.map(user, UserDTO.class);
        response.setPassword(null);
        return response;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        UserDTO response = mapper.map(user, UserDTO.class);
        response.setPassword(null);
        return response;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO dto = mapper.map(user, UserDTO.class);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(user -> {
                    UserDTO dto = mapper.map(user, UserDTO.class);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Check email uniqueness if it's being changed
        if (!user.getEmail().equals(userDTO.getEmail()) &&
                userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("User with email " + userDTO.getEmail() + " already exists");
        }

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // Only update password if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        user.setPhone(userDTO.getPhone());
        user.setRole(userDTO.getRole());

        Users updatedUser = userRepository.save(user);
        UserDTO response = mapper.map(updatedUser, UserDTO.class);
        response.setPassword(null);
        return response;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
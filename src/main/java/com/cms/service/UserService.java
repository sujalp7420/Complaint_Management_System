package com.cms.service;

import com.cms.dto.UserDTO;
import com.cms.entity.Role;
import com.cms.entity.StatusUser;
import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(Integer id);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByRole(Role role);

    UserDTO updateUser(Integer id, UserDTO userDTO);

    UserDTO updateUserStatus(Integer id, StatusUser status);

    void deleteUser(Integer id);

    boolean existsByEmail(String email);
}
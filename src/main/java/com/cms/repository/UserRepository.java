package com.cms.repository;

import com.cms.entity.Users;
import com.cms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    List<Users> findByRole(Role role);

    boolean existsByEmail(String email);
}
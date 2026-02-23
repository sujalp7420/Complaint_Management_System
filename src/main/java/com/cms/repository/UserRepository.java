package com.cms.repository;

import com.cms.entity.Users;
import com.cms.entity.Role;
import com.cms.entity.StatusUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    List<Users> findByRole(Role role);

    List<Users> findByStatusUser(StatusUser statusUser);

    @Query("SELECT u FROM Users u WHERE u.role = :role AND u.statusUser = :status")
    List<Users> findByRoleAndStatus(@Param("role") Role role, @Param("status") StatusUser status);

    boolean existsByEmail(String email);
}
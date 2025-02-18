package com.example.U4_S7_L2_ese.repository;

import com.example.U4_S7_L2_ese.entity.ERole;
import com.example.U4_S7_L2_ese.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);
}

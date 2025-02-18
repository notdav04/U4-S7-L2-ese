package com.example.U4_S7_L2_ese.repository;


import com.example.U4_S7_L2_ese.entity.Dipendente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DipendenteDAORepository extends JpaRepository<Dipendente, Long> {

    Optional<Dipendente> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}

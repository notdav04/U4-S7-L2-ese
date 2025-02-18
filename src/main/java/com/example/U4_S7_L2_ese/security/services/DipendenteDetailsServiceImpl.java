package com.example.U4_S7_L2_ese.security.services;

import com.example.U4_S7_L2_ese.entity.Dipendente;
import com.example.U4_S7_L2_ese.repository.DipendenteDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class DipendenteDetailsServiceImpl implements UserDetailsService {
  @Autowired
  DipendenteDAORepository dipendenteRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Dipendente dipendente = dipendenteRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return DipendenteDetailsImpl.build(dipendente);
  }

}

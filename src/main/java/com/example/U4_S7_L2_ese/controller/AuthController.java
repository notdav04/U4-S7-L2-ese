package com.example.U4_S7_L2_ese.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.U4_S7_L2_ese.payload.request.DipendenteDTO;
import com.example.U4_S7_L2_ese.payload.request.LoginRequest;
import com.example.U4_S7_L2_ese.payload.response.JwtResponse;
import com.example.U4_S7_L2_ese.security.jwt.JwtUtils;
import com.example.U4_S7_L2_ese.security.services.DipendenteDetailsImpl;
import com.example.U4_S7_L2_ese.service.DipendenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	DipendenteService service;

	@Autowired
	JwtUtils jwtUtils;

	// Metodo che autentica tramite username e password e genera il JWT token finale 
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		// Utilizziamo il costruttore per costruire un token
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword());
		
		
		// Rappresenta il TOKEN una volta effettuata l'autenticazione tramite il metodo authenticate
		Authentication tokenAutenticato = authenticationManager.authenticate(token);

//		Dall'attuale ambiente di Security andiamo a settare il nuovo TOKEN appena autenticato
		SecurityContextHolder.getContext().setAuthentication(tokenAutenticato);
		
		// INFO DA INVIARE AL CLIENT (JWT , DETTAGLI UTENTE , RUOLI DELL'UTENTE)
//		Generiamo il JWT
		String jwt = jwtUtils.generaJwtToken(tokenAutenticato);

//		Ritorna i dettagli dell'utente : ruoli e dati dell'utente
		DipendenteDetailsImpl userDetails = (DipendenteDetailsImpl) tokenAutenticato.getPrincipal();
		
//		Recupera i ruoli tramite i dettagli dell'utente
		List<String> roles = userDetails.getAuthorities().stream().map(item -> 
																   item.getAuthority()).collect(Collectors.toList());

		// organizziamo la response di tipo JWT con una tipologia ad hoc creata da noi.
		// INCPSULIAMO TUTTE LE INFO DA INVIARE AL CLIENT IN UN UNICO DTO
		JwtResponse jwtRes =  new JwtResponse(jwt,userDetails.getId(),userDetails.getUsername(),userDetails.getEmail(),roles);
		
		return new ResponseEntity<>(jwtRes, HttpStatus.OK);
//		return new ResponseEntity<>("", jwtRes, HttpStatus.OK);
	}

	
	
	/**
	 * Metodo che effettua la registrazione di un nuovo utente sul database
	 * @param dipendenteDto
	 * @return
	 */
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody DipendenteDTO dipendenteDto) {

		// CONTROLLO SU CAMPI UNIQUE   : USERNAME
		if(service.checkUsername(dipendenteDto.getUsername())) {
			return new ResponseEntity<>("Username già presente", HttpStatus.BAD_REQUEST);
		}

		// CONTROLLO SU CAMPI UNIQUE   : EMAIL
		if (service.checkEmail(dipendenteDto.getEmail())) {
			return new ResponseEntity<>("La mail è già presente", HttpStatus.BAD_REQUEST);
		}

		// CREAZIONE DELL'ACCOUNT DEL NUOVO UTENTE
		service.saveDipendente(dipendenteDto);
		return new ResponseEntity<>("L'utente è stato registrato con successo!", HttpStatus.OK);
	}
}

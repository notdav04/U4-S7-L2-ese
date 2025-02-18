package com.example.U4_S7_L2_ese.security.jwt;

import java.security.Key;
import java.util.Date;

import com.example.U4_S7_L2_ese.security.services.DipendenteDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private int jwtExpirationMs;

  // Metodo che dal Token autenticato genera il JWT TOKEN finale
  public String generaJwtToken(Authentication authentication) {
    // Recuperiamo i dettagli dell'utente
    DipendenteDetailsImpl userPrincipal = (DipendenteDetailsImpl) authentication.getPrincipal();

    // Tramite il metodo compact trasforma tutte le impostazioni in una Stringa
    // Classe utile per creare istanze di interfacce JWT
    return Jwts.builder().setSubject((userPrincipal.getUsername()))
            .setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
  }
  
  // Crea una nuova istanza di SecretKey da utilizzare con gli algoritmi 
  // HMAC-SHA in base all'array di byte di chiavi specificato.
  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // RECUPERA L'USERNAME PARTENDO DAL TOKEN 
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(key()).build()
            .parseClaimsJws(token).getBody().getSubject();
  }

  
  /**
   * Metodo che effettua la validazione del JWT TOKEN
   * @param authToken : Token Autenticato
   * @return
   */
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}

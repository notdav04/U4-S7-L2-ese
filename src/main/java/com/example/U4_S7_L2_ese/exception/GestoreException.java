package com.example.U4_S7_L2_ese.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


//PURTROPPO LA GESTIONE DEGLI ERRORI CUSTOM NON MI è MOLTO CHIARA
//IN QUANTO L ABBIAMO VISTA VELOCEMENTE E CON 1 SOLO ESEMPIO DI CODICE
//HO VOLUTO COMUNQUE PROVARE LA CREAZIONE DI 1 EXCEPTION CUSTOM SEGUENDO IL MODELLO DI CODICE VISTO A LEZIONE\
//NON L HO UTILIZZATA PERCHE ANCORA DEVO LEGARE BENE I CONCETTI TEORICI ALLA PRATICA PER QUANTO RIGUARDA QUESTO ERROR HANDLING CON EXCEPTION CUSTOM

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GestoreException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<Object> gestore(CustomException customE){

        DipendenteException error = new DipendenteException(customE.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        return costruisciResponse(error);

    }

    public ResponseEntity<Object> costruisciResponse(DipendenteException err){
        return new ResponseEntity<>(err,err.getStatus());
    }
}

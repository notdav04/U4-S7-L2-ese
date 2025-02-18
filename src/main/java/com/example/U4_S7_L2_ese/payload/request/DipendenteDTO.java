package com.example.U4_S7_L2_ese.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.Set;

@Data
public class DipendenteDTO {

    @NotNull(message = "il campo username è obbligatorio")
    @NotBlank(message = "username non puo essere vuoto")
    private String username;

    @NotNull(message = "il campo nome è obbligatorio")
    @NotBlank(message = "nome non puo essere vuoto")
    private String nome;

    @NotNull(message = "il campo cognome è obbligatorio")
    @NotBlank(message = "cognome non puo essere vuoto")
    private String cognome;

    @Email(message = "email non valida")
    private String email;

    @URL
    private String avatar;

    private Set<String> ruoli;

    @NotBlank
    @Size(min=6, max=40)
    private String password;



}

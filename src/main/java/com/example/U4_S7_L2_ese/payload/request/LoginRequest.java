package com.example.U4_S7_L2_ese.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message="Il campo username è obbligatorio")
    private String username;

    @NotBlank(message="Il campo password è obbligatorio")
    private String password;
}

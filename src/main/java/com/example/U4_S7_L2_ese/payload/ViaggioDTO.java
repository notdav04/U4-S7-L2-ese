package com.example.U4_S7_L2_ese.payload;

import com.example.U4_S6_L5_progetto.entity.Dipendente;
import com.example.U4_S6_L5_progetto.entity.StatoViaggio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class ViaggioDTO {

    @NotNull(message = "il campo destinazione è obbligatorio")
    @NotBlank(message = "destinazione non puo essere vuota")
    @Size(min = 3, max=20, message="il nome della destinazione è troppo corto o troppo lungo(min=3, max=20)")
    private String destinazione;

    @NotNull(message = "il campo data è obbligatorio")
    private LocalDate data;

    @NotNull(message = "il campo stato è obbligatorio")
    private StatoViaggio stato;

    private Dipendente dipendente;


}

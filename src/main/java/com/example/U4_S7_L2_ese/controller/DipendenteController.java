package com.example.U4_S7_L2_ese.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.U4_S6_L5_progetto.payload.DipendenteDTO;
import com.example.U4_S6_L5_progetto.payload.PrenotazioneDTO;
import com.example.U4_S6_L5_progetto.service.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/dipendente")
public class DipendenteController {

    @Autowired
    DipendenteService dipendenteService;

    @Autowired
    Cloudinary configurazioneCloud;

    //metodi CRUD
    //POST
    @PostMapping
    public ResponseEntity<?> postDipendente(@RequestBody @Validated DipendenteDTO dipendenteDTO, BindingResult validation){
        if (validation.hasErrors()){
            String message = "ERRORE DI VALIDAZIONE \n";
            for(ObjectError error : validation.getAllErrors()){
                message += error.getDefaultMessage() + "\n";

            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        long idDipendenteSalvato = dipendenteService.saveDipendente(dipendenteDTO);
        return new ResponseEntity<>("dipendente inserito con id: " + idDipendenteSalvato, HttpStatus.CREATED);
    }

    //------------------POST CON IMMAGINE AVATAR DIPENDENTE------------
    //HO DECISO DI SCRIVERE UN POST CON ENDPOINT DIVERSO PER IL DIPENDENTE CON L AVATAR PER FISSARE MEGLIO IL CONCETTO DI INCLUSIONE E FUNZIONAMENTO DI CLOUDINARY
    @PostMapping(value = "/dipendenteConAvatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity<?> postDipendenteAvatar(@RequestPart("avatar")MultipartFile avatar, @RequestPart @Validated DipendenteDTO dipendenteDTO, BindingResult validation){
        if (validation.hasErrors()){
            String message = "ERRORE DI VALIDAZIONE \n";
            for(ObjectError error : validation.getAllErrors()){
                message += error.getDefaultMessage() + "\n";

            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

        }else {
            try{
                Map mappa = configurazioneCloud.uploader().upload(avatar.getBytes(), ObjectUtils.emptyMap());
                String urlImage = mappa.get("secure_url").toString();
                dipendenteDTO.setAvatar(urlImage);
                long idDipendenteSalvato = dipendenteService.saveDipendente(dipendenteDTO);
                return new ResponseEntity<>("dipendente inserito con id: " + idDipendenteSalvato, HttpStatus.CREATED);
            } catch (Exception e) {
                throw new RuntimeException("errore nell inserimento del dipendente con l avatar" + e);
            }
        }
    }


    // GET(ALL)
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Page<DipendenteDTO>> getAllDipendente(Pageable page){
        Page<DipendenteDTO> dipendenteDTO = dipendenteService.findAllDipendente(page);
        return new ResponseEntity<>(dipendenteDTO, HttpStatus.OK);
    }

    //GET
    @GetMapping("/{id}")
    public DipendenteDTO getDipendenteById(@PathVariable long id){
        return dipendenteService.findDipendenteById(id);
    }

    //PUT
    @PutMapping("/update/{id}")
    public ResponseEntity<?> putDipendente(@RequestBody @Validated DipendenteDTO dipendenteDTO, @PathVariable long id, BindingResult validation){
        if(validation.hasErrors()){
            String message = "ERRORE DI VALIDAZIONE \n";
            for (ObjectError errore : validation.getAllErrors()){
                message += errore.getDefaultMessage() + "\n";
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }else{
            dipendenteService.modificaDipendente(dipendenteDTO, id);
            return new ResponseEntity<>("dipendente modificato con successo", HttpStatus.OK );
        }
    }

    //CREAZIONE PRENOTAZIONE( POST )
    @PostMapping("/creaPrenotazione")
    public ResponseEntity<?> creaPrenotazione( @RequestBody @Validated PrenotazioneDTO prenotazioneDTO, BindingResult validation){
        if (validation.hasErrors()){
            String message = "ERRORE DI VALIDAZIONE \n";
            for (ObjectError errore : validation.getAllErrors()){
                message += errore.getDefaultMessage() + "\n";
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }else{
            dipendenteService.creaPrenotazione( prenotazioneDTO);
            return new ResponseEntity<>("prenotazione creata correttamente", HttpStatus.CREATED);
        }
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDipendente(@PathVariable long id){
        dipendenteService.eliminaDipendente(id);
        return new ResponseEntity<>("dipendente eliminato con successo", HttpStatus.OK);
    }
}

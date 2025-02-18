package com.example.U4_S7_L2_ese.controller;



import com.example.U4_S7_L2_ese.entity.StatoViaggio;
import com.example.U4_S7_L2_ese.payload.request.ViaggioDTO;
import com.example.U4_S7_L2_ese.service.ViaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/viaggio")
public class ViaggioController {

    @Autowired
    ViaggioService viaggioService;

    //metodi CRUD
    //POST
    @PostMapping
    public ResponseEntity<?> postViaggio(@RequestBody @Validated ViaggioDTO viaggioDTO, BindingResult validation){
        if (validation.hasErrors()){
            String message = "ERRORE DI VALIDAZIONE \n";
            for (ObjectError errore : validation.getAllErrors()){
                message += errore.getDefaultMessage() + "\n";
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        long idViaggioSalvato = viaggioService.saveViaggio(viaggioDTO);
        return new ResponseEntity<>("viaggio inserito con id: " + idViaggioSalvato, HttpStatus.CREATED);
    }
//GET (ALL)
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Page<ViaggioDTO>> getAllViaggio(Pageable page){
        Page<ViaggioDTO> viaggiDTO = viaggioService.findAllViaggio(page);
        return new ResponseEntity<>(viaggiDTO, HttpStatus.OK);
    }
//GET
    @GetMapping("/{id}")
    public ViaggioDTO getViaggioById(@PathVariable long id){
        return viaggioService.findViaggioById(id);
    }
//PUT
    @PutMapping("/update/{id}")
    public ResponseEntity<?> putViaggio( @RequestBody @Validated ViaggioDTO viaggioDTO, BindingResult validation, @PathVariable long id){
        if (validation.hasErrors()) {
            String message = "ERRORE DI VALIDAZIONE \n";
            for (ObjectError errore : validation.getAllErrors()){
                message += errore.getDefaultMessage() + "\n";
            }
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }else {
            viaggioService.modificaViaggio(viaggioDTO, id);
            return new ResponseEntity<>("viaggio modificato con successo", HttpStatus.OK);
        }
    }

    //PUT PER ASSEGNAZIONE DIPENDENTE
    @PutMapping("/assegnaDipendente/{id_viaggio}/{id_dipendente}")
    public ResponseEntity<?> assegnaDipendenteAViaggio(@PathVariable long id_viaggio, @PathVariable long id_dipendente){
        viaggioService.addDipendente(id_viaggio, id_dipendente);
        return new ResponseEntity<>("dipendente con id: " + id_dipendente + " aggiunto al viaggio con id: " + id_viaggio, HttpStatus.OK);
    }
//HO IMPLEMENTATO LA MODIFICA STATO COSI, L UNICO "PROBLEMA" è CHE IN POSTMAN NEL BODY DEVO INVIARE STRINGA PURE E NON JSON
    @PatchMapping("/modificaStato/{id}")
    public  ResponseEntity<?> modificaStatoViaggio(@RequestBody StatoViaggio stato, @PathVariable long id){
        viaggioService.modificaStato(id, stato);
        return new ResponseEntity<>("stato viaggio modificato correttamente", HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteViaggio(@PathVariable long id){
        viaggioService.eliminaViaggio(id);
        return new ResponseEntity<>("Viaggio eliminato con successo", HttpStatus.OK);
    }


}

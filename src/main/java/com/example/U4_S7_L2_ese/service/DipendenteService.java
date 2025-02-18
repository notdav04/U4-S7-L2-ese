package com.example.U4_S7_L2_ese.service;

import com.example.U4_S6_L5_progetto.entity.Dipendente;
import com.example.U4_S6_L5_progetto.entity.Prenotazione;
import com.example.U4_S6_L5_progetto.entity.Viaggio;
import com.example.U4_S6_L5_progetto.payload.DipendenteDTO;
import com.example.U4_S6_L5_progetto.payload.PrenotazioneDTO;
import com.example.U4_S6_L5_progetto.repository.DipendenteDAORepository;
import com.example.U4_S6_L5_progetto.repository.PrenotazioneDAORepository;
import com.example.U4_S6_L5_progetto.repository.ViaggioDAORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DipendenteService {

    @Autowired
    DipendenteDAORepository dipendenteDAO;

    @Autowired
    ViaggioDAORepository viaggioDAO;
    @Autowired
    PrenotazioneDAORepository prenotazioneDAO;

    //metodi dao

    //salvo dipendente( post )
    public Long saveDipendente(DipendenteDTO dipendenteDTO){
        Dipendente dipendente = fromDipendenteDTOToEntity(dipendenteDTO);
        return dipendenteDAO.save(dipendente).getId();
    }
    //prendo tutti i dipendenti( get all )
    public Page<DipendenteDTO> findAllDipendente(Pageable page) {
        Page<Dipendente> listaDipendenti = dipendenteDAO.findAll(page);
        List<DipendenteDTO> listaDipendentiDTO = new ArrayList<>();
        for (Dipendente dipendente : listaDipendenti.getContent()) {
            DipendenteDTO dipendenteDTO =fromDipendenteToDipendenteDTO(dipendente);
                    listaDipendentiDTO.add(dipendenteDTO);
        }
        Page<DipendenteDTO> listaPage = new PageImpl<>(listaDipendentiDTO);
        return listaPage;
    }

    //prendo un solo dipendente( get by id )
    public DipendenteDTO findDipendenteById(long id){
        Optional<Dipendente> dipendente = dipendenteDAO.findById(id);
        if (dipendente.isPresent()){
            DipendenteDTO dipendenteDTO = fromDipendenteToDipendenteDTO(dipendente.get());
            return dipendenteDTO;
        }else{
            throw new RuntimeException("nessun dipendente trovato con l id richiesto");
        }
    }

    //modifica dipendente( put )
    public void modificaDipendente(DipendenteDTO dipendenteDTO, long id){
        Optional<Dipendente> dipendenteTrovato = dipendenteDAO.findById(id);
        if (dipendenteTrovato.isPresent()){
            Dipendente dipendente = dipendenteTrovato.get();
            dipendente.setUsername(dipendenteDTO.getUsername());
            dipendente.setNome(dipendenteDTO.getNome());
            dipendente.setCognome(dipendenteDTO.getCognome());
            dipendente.setEmail(dipendenteDTO.getEmail());
//            dipendente.setAvatar(dipendenteDTO.getAvatar());
            dipendenteDAO.save(dipendente);

        }else{
            throw new RuntimeException("nessun dipendente trovato con l id richiesto! errore nella modifica");
        }
    }



    //eliminare dipendenti( delete )
    public void eliminaDipendente(long id){
        Optional<Dipendente> dipendenteTrovato = dipendenteDAO.findById(id);
        if (dipendenteTrovato.isPresent()){
            dipendenteDAO.deleteById(id);
        }else{
            throw new RuntimeException("nessun dipendente trovato conl id richiesto! errore nell eliminazione");
        }
    }

    //creazione prenotazione
    public void creaPrenotazione(PrenotazioneDTO prenotazioneDTO){
        Optional<Dipendente> dipendenteTrovato = dipendenteDAO.findById(prenotazioneDTO.getFk_dipendente());

        Optional<Viaggio> viaggioTrovato = viaggioDAO.findById(prenotazioneDTO.getFk_viaggio());
        if (dipendenteTrovato.isPresent() && viaggioTrovato.isPresent()){
            Dipendente dipendente = dipendenteTrovato.get();
            Viaggio viaggio = viaggioTrovato.get();

            if (!prenotazioneDAO.findByDataAndDipendente( prenotazioneDTO.getData(), dipendente).isEmpty()){
                throw new RuntimeException("il dipendente ha gia una prenotazione per la data richiesta");
            }else if(!prenotazioneDAO.findByDipendente(dipendente).isEmpty()){
                    throw new RuntimeException("il dipendente ha gia prenotato un viaggio");
                }else{
                Prenotazione nuovaPrenotazione = new Prenotazione();
                nuovaPrenotazione.setDipendente(dipendente);
                nuovaPrenotazione.setViaggio(viaggio);
                nuovaPrenotazione.setData(prenotazioneDTO.getData());
                nuovaPrenotazione.setNoteDipendente(prenotazioneDTO.getNoteDipendente());
                prenotazioneDAO.save(nuovaPrenotazione);
            }

        }else{
            throw new RuntimeException("nessun dipendente o viaggio trovato con gli id richiesti!");
        }
    }

    //travasi DTO
    public Dipendente fromDipendenteDTOToEntity(DipendenteDTO dipendenteDTO) {
        Dipendente dipendente = new Dipendente();
        dipendente.setUsername(dipendenteDTO.getUsername());
        dipendente.setNome(dipendenteDTO.getNome());
        dipendente.setCognome(dipendenteDTO.getCognome());
        dipendente.setEmail(dipendenteDTO.getEmail());
        dipendente.setAvatar(dipendenteDTO.getAvatar());
        return dipendente;
    }

    public DipendenteDTO fromDipendenteToDipendenteDTO(Dipendente dipendente) {
        DipendenteDTO dipendenteDTO = new DipendenteDTO();
        dipendenteDTO.setUsername(dipendente.getUsername());
        dipendenteDTO.setNome(dipendente.getNome());
        dipendenteDTO.setCognome(dipendente.getCognome());
        dipendenteDTO.setEmail(dipendente.getEmail());
        dipendenteDTO.setAvatar(dipendente.getAvatar());
        return dipendenteDTO;
    }
}

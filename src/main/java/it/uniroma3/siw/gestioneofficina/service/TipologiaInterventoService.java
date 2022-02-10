package it.uniroma3.siw.gestioneofficina.service;

import it.uniroma3.siw.gestioneofficina.model.Intervento;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.repository.InterventoRepository;
import it.uniroma3.siw.gestioneofficina.repository.TipologiaInterventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TipologiaInterventoService {
    @Autowired
    protected TipologiaInterventoRepository tipologiaInterventoRepository;


    @Transactional
    public TipologiaIntervento getTipologiaIntervento(long id) {
        Optional<TipologiaIntervento> result = this.tipologiaInterventoRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public TipologiaIntervento getTipologiaIntervento(String codice) {
        Optional<TipologiaIntervento> result = Optional.ofNullable(this.tipologiaInterventoRepository.findByCodice(codice));
        return result.orElse(null);
    }


    @Transactional
    public TipologiaIntervento saveTipolofgiaIntervento(TipologiaIntervento tipologiaIntervento) {
        return this.tipologiaInterventoRepository.save(tipologiaIntervento);
    }

    @Transactional
    public List<TipologiaIntervento> getAllTipologieInterventi() {
        List<TipologiaIntervento> result = new ArrayList<>();
        Iterable<TipologiaIntervento> iterable = this.tipologiaInterventoRepository.findAll();
        for(TipologiaIntervento tipologiaIntervento : iterable)
            result.add(tipologiaIntervento);
        return result;
    }

    @Transactional
    public void deleteTipologiaIntervento(TipologiaIntervento tipologiaIntervento) {
        this.tipologiaInterventoRepository.delete(tipologiaIntervento);
    }

    @Transactional
    public long countAll() {
        return tipologiaInterventoRepository.count();
    }
}

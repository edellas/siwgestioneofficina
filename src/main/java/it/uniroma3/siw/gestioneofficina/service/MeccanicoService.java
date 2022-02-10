package it.uniroma3.siw.gestioneofficina.service;

import it.uniroma3.siw.gestioneofficina.model.Meccanico;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.repository.MeccanicoRepository;
import it.uniroma3.siw.gestioneofficina.repository.TipologiaInterventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeccanicoService {
    @Autowired
    protected MeccanicoRepository meccanicoRepository;


    @Transactional
    public Meccanico getMeccanico(long id) {
        Optional<Meccanico> result = this.meccanicoRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Meccanico getMeccanico(String nome, String cognome) {
        Optional<Meccanico> result = Optional.ofNullable(this.meccanicoRepository.findByNomeAndCognome(nome,cognome));
        return result.orElse(null);
    }


    @Transactional
    public Meccanico saveMeccanico(Meccanico meccanico) {
        return this.meccanicoRepository.save(meccanico);
    }

    @Transactional
    public List<Meccanico> getAllMeccanico() {
        List<Meccanico> result = new ArrayList<>();
        Iterable<Meccanico> iterable = this.meccanicoRepository.findAll();
        for(Meccanico meccanico : iterable)
            result.add(meccanico);
        return result;
    }

    @Transactional
    public void deleteMeccanico(Meccanico meccanico) {
        this.meccanicoRepository.delete(meccanico);
    }

    @Transactional
    public long countAll() {
        return meccanicoRepository.count();
    }
}

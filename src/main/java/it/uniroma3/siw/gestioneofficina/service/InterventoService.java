package it.uniroma3.siw.gestioneofficina.service;

import it.uniroma3.siw.gestioneofficina.model.Intervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.repository.InterventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InterventoService {
    @Autowired
    protected InterventoRepository interventoRepository;


    @Transactional
    public Intervento getIntervento(long id) {
        Optional<Intervento> result = this.interventoRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public Intervento saveIntervento(Intervento intervento) {
        return this.interventoRepository.save(intervento);
    }

    @Transactional
    public List<Intervento> getAllInterventi() {
        List<Intervento> result = new ArrayList<>();
        Iterable<Intervento> iterable = this.interventoRepository.findAll();
        for(Intervento intervento : iterable)
            result.add(intervento);
        return result;
    }

    @Transactional
    public long countAll() {
        return interventoRepository.count();
    }
}

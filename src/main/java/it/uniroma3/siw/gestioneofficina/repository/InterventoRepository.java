package it.uniroma3.siw.gestioneofficina.repository;

import it.uniroma3.siw.gestioneofficina.model.Intervento;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

public interface InterventoRepository extends CrudRepository<Intervento, Long> {
    Optional<Intervento> findByTipologiaIntervento(TipologiaIntervento tipologiaIntervento);


    List<Intervento> findByCliente(User cliente);

}

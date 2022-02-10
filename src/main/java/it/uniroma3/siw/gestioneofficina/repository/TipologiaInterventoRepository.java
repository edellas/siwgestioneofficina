package it.uniroma3.siw.gestioneofficina.repository;

import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import org.springframework.data.repository.CrudRepository;

public interface TipologiaInterventoRepository extends CrudRepository<TipologiaIntervento, Long> {
    TipologiaIntervento findByCodice(String codice);
}

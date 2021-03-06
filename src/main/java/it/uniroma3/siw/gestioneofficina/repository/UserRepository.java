package it.uniroma3.siw.gestioneofficina.repository;

import it.uniroma3.siw.gestioneofficina.model.Intervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByInterventi(Intervento intervento);

}
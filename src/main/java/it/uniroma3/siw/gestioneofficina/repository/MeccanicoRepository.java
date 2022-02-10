package it.uniroma3.siw.gestioneofficina.repository;

import it.uniroma3.siw.gestioneofficina.model.Meccanico;
import org.springframework.data.repository.CrudRepository;

public interface MeccanicoRepository extends CrudRepository<Meccanico, Long> {
    Meccanico findByNomeAndCognome(String nome, String cognome);
}

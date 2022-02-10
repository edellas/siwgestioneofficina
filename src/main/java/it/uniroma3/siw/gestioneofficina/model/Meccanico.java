package it.uniroma3.siw.gestioneofficina.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "meccanico")
public class Meccanico {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String cognome;

    @OneToMany(mappedBy = "meccanico")
    private List<Intervento> interventiMeccanico;

    public Meccanico(){

    }

    public Meccanico(String nome, String cognome, List<Intervento> interventiMeccanico) {
        this.nome = nome;
        this.cognome = cognome;
        this.interventiMeccanico = interventiMeccanico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public List<Intervento> getInterventiMeccanico() {
        return interventiMeccanico;
    }

    public void setInterventiMeccanico(List<Intervento> interventiMeccanico) {
        this.interventiMeccanico = interventiMeccanico;
    }
}
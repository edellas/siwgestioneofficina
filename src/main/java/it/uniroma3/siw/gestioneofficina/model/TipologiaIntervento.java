package it.uniroma3.siw.gestioneofficina.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class TipologiaIntervento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String codice;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private float costo;

    @OneToMany(mappedBy = "tipologiaIntervento")
    private List<Intervento> interventi;

    public TipologiaIntervento(String nome, String codice, String descrizione, float costo, List<Intervento> interventi) {
        this.nome = nome;
        this.codice = codice;
        this.descrizione = descrizione;
        this.costo = costo;
        this.interventi = interventi;
    }

    public TipologiaIntervento() {
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

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public List<Intervento> getInterventi() {
        return interventi;
    }

    public void setInterventi(List<Intervento> interventi) {
        this.interventi = interventi;
    }
}

package it.uniroma3.siw.gestioneofficina.model;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Intervento {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User cliente;

    @ManyToOne
    private TipologiaIntervento tipologiaIntervento;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataPrenotazione;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataIntervento;

    @ManyToOne
    private Meccanico meccanico;

    public Intervento(){}

    public Intervento(User cliente, TipologiaIntervento tipologiaIntervento, LocalDateTime dataPrenotazione, LocalDate dataIntervento, Meccanico meccanico) {
        this.cliente = cliente;
        this.tipologiaIntervento = tipologiaIntervento;
        this.dataPrenotazione = dataPrenotazione;
        this.dataIntervento = dataIntervento;
        this.meccanico = meccanico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipologiaIntervento getTipologiaIntervento() {
        return tipologiaIntervento;
    }

    public void setTipologiaIntervento(TipologiaIntervento tipologiaIntervento) {
        this.tipologiaIntervento = tipologiaIntervento;
    }

    public LocalDateTime getDataPrenotazione() {
        return dataPrenotazione;
    }

    public void setDataPrenotazione(LocalDateTime dataPrenotazione) {
        this.dataPrenotazione = dataPrenotazione;
    }

    public LocalDate getDataIntervento() {
        return dataIntervento;
    }

    public void setDataIntervento(LocalDate dataIntervento) {
        this.dataIntervento = dataIntervento;
    }

    public Meccanico getMeccanico() {
        return meccanico;
    }

    public void setMeccanico(Meccanico meccanico) {
        this.meccanico = meccanico;
    }

    public User getCliente() {
        return cliente;
    }

    public void setCliente(User cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Intervento{" +
                "cliente=" + cliente.getFirstName() + cliente.getLastName() +
                ", tipologiaIntervento=" + tipologiaIntervento.getNome() + tipologiaIntervento.getDescrizione() +
                ", dataPrenotazione=" + dataPrenotazione +
                ", dataIntervento=" + dataIntervento +
                '}';
    }
}

package it.uniroma3.siw.gestioneofficina.controller.validation;

import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import it.uniroma3.siw.gestioneofficina.service.TipologiaInterventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TipologiaValidator implements Validator {

    @Autowired
    TipologiaInterventoService tipologiaInterventoService;

    @Override
    public void validate(Object o, Errors errors) {
        TipologiaIntervento tipologiaIntervento = (TipologiaIntervento) o;
        String nome = tipologiaIntervento.getNome().trim();
        String descrizione = tipologiaIntervento.getDescrizione().trim();
        String codice = tipologiaIntervento.getCodice().trim();

        if (nome.isEmpty())
            errors.rejectValue("nome", "required");
        else if(descrizione.isEmpty())
            errors.rejectValue("descrizione", "required");

        if (codice.isEmpty())
            errors.rejectValue("codice", "required");
        else if (this.tipologiaInterventoService.getTipologiaIntervento(codice) != null)
            errors.rejectValue("codice", "duplicate");
        }

    @Override
    public boolean supports(Class<?> clazz) {
        return TipologiaIntervento.class.equals(clazz);
    }
}

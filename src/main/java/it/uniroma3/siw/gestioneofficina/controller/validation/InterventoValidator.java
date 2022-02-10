package it.uniroma3.siw.gestioneofficina.controller.validation;

import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.Intervento;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import it.uniroma3.siw.gestioneofficina.service.InterventoService;
import it.uniroma3.siw.gestioneofficina.service.TipologiaInterventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class InterventoValidator implements Validator {

    @Autowired
    InterventoService interventoService;

    @Override
    public void validate(Object o, Errors errors) {
        Intervento intervento = (Intervento) o;
        String nomeTipologia = intervento.getTipologiaIntervento().getNome().trim();
        String cliente = intervento.getCliente().getFirstName().trim();
        String meccanico = intervento.getMeccanico().getNome().trim();

        if (nomeTipologia.isEmpty())
            errors.rejectValue("nome", "required");
        else if(cliente.isEmpty())
            errors.rejectValue("cliente", "required");

        if (meccanico.isEmpty())
            errors.rejectValue("meccanico", "required");
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Intervento.class.equals(clazz);
    }
}

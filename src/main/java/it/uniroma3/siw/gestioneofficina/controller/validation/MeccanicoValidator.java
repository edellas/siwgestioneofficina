package it.uniroma3.siw.gestioneofficina.controller.validation;

import it.uniroma3.siw.gestioneofficina.model.Meccanico;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.service.MeccanicoService;
import it.uniroma3.siw.gestioneofficina.service.TipologiaInterventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeccanicoValidator implements Validator {
    @Autowired
    MeccanicoService meccanicoService;

    @Override
    public void validate(Object o, Errors errors) {
        Meccanico meccanico = (Meccanico) o;
        String nome = meccanico.getNome().trim();
        String cognome = meccanico.getCognome();

        if (nome.isEmpty())
            errors.rejectValue("nome", "required");
        else if(cognome.isEmpty())
            errors.rejectValue("cognome", "required");
        else if (this.meccanicoService.getMeccanico(nome,cognome) != null)
            errors.rejectValue("nome", "duplicate");

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Meccanico.class.equals(clazz);
    }
}

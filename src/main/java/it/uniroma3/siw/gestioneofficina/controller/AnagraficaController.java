package it.uniroma3.siw.gestioneofficina.controller;

import it.uniroma3.siw.gestioneofficina.controller.session.SessionData;
import it.uniroma3.siw.gestioneofficina.model.*;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma3.siw.gestioneofficina.model.Credentials.DEFAULT_ROLE;

@Controller
public class AnagraficaController {

    @Autowired
    SessionData sessionData;
    @Autowired
    CredentialsService credentialsService;

    @RequestMapping(value = { "/utenti" }, method = RequestMethod.GET)
    public String showAnagrafica(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        List<Credentials> clienti = new ArrayList<>();
        for(Credentials c : credentialsService.getAllCredentials()){
            if(c.getRole().equals("CLIENTE")){
                clienti.add(c);
            }
        }
        model.addAttribute("credentials",credentialsService.getAllCredentials());
        model.addAttribute("clienti",clienti);
        model.addAttribute("visitor", visitor);

        return "utenti";
    }

}

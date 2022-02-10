package it.uniroma3.siw.gestioneofficina.controller;

import it.uniroma3.siw.gestioneofficina.controller.session.SessionData;
import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.Intervento;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import it.uniroma3.siw.gestioneofficina.service.InterventoService;
import it.uniroma3.siw.gestioneofficina.service.MeccanicoService;
import it.uniroma3.siw.gestioneofficina.service.TipologiaInterventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


@Controller
public class MainController {
    @Autowired
    CredentialsService credentialsService;
    @Autowired
    TipologiaInterventoService tipologiaInterventoService;
    @Autowired
    MeccanicoService meccanicoService;
    @Autowired
    InterventoService interventoService;
    @Autowired
    SessionData sessionData;

    @RequestMapping(value = { "/siw" }, method = RequestMethod.GET)
    public String siw(Model model) {
        return "redirect:/";
    }

    @RequestMapping(value = { "/", "/index", "/home" }, method = RequestMethod.GET)
    public String index(Model model) {
        User visitor = sessionData.getLoggedUser();
        Credentials credentialsVisitor = sessionData.getLoggedCredentials();
        model.addAttribute("tipologie", tipologiaInterventoService.getAllTipologieInterventi());
        model.addAttribute("meccanici", meccanicoService.getAllMeccanico());
        List<Intervento> interventi = new ArrayList<>();
        for(Intervento i : interventoService.getAllInterventi()){
            if(i.getCliente().getId().equals(visitor.getId())){
                interventi.add(i);
            }
        }
        model.addAttribute("interventi", interventi);
        List<Credentials> clienti = new ArrayList<>();
        for(Credentials c : credentialsService.getAllCredentials()){
            if(c.getRole().equals("CLIENTE")){
                clienti.add(c);
            }
        }
        model.addAttribute("clienti",clienti);
        if (visitor != null) {
            // User logged in
            Credentials credentials = credentialsService.getCredentials(visitor);
            model.addAttribute("credentials", credentials);

            model.addAttribute("visitor",visitor);
            model.addAttribute("credentialsVisitor",credentialsVisitor);
            return "home";
        }
        model.addAttribute("totalMembers", credentialsService.countAll());
        return "index";
    }

    @RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
    public String admin(Model model) {
        model.addAttribute("credentialsVisitor",sessionData.getLoggedCredentials());
        model.addAttribute("credentials", credentialsService.getAllCredentials());
        return "admin";
    }
}

package it.uniroma3.siw.gestioneofficina.controller;

import it.uniroma3.siw.gestioneofficina.controller.session.SessionData;
import it.uniroma3.siw.gestioneofficina.controller.validation.CredentialsValidator;
import it.uniroma3.siw.gestioneofficina.controller.validation.TipologiaValidator;
import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.repository.CredentialsRepository;
import it.uniroma3.siw.gestioneofficina.repository.TipologiaInterventoRepository;
import it.uniroma3.siw.gestioneofficina.repository.UserRepository;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import it.uniroma3.siw.gestioneofficina.service.InterventoService;
import it.uniroma3.siw.gestioneofficina.service.TipologiaInterventoService;
import it.uniroma3.siw.gestioneofficina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import static it.uniroma3.siw.gestioneofficina.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.gestioneofficina.model.Credentials.DEFAULT_ROLE;

@Controller
public class TipologiaInterventoController {
    @Autowired
    CredentialsRepository credentialsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;
    @Autowired
    CredentialsService credentialsService;
    @Autowired
    InterventoService interventoService;
    @Autowired
    TipologiaInterventoService tipologiaInterventoService;
    @Autowired
    TipologiaInterventoRepository tipologiaInterventoRepository;
    @Autowired
    TipologiaValidator tipologiaValidator;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SessionData sessionData;


    @RequestMapping(value = { "/tipologiainterventi" }, method = RequestMethod.GET)
    public String showTipologiaInterventi(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        model.addAttribute("tipinterventi",tipologiaInterventoService.getAllTipologieInterventi());

        return "tipologiaIntervento/tipologiainterventi";
    }

    @RequestMapping(value = { "/tipologiaintervento/{id}" }, method = RequestMethod.GET)
    public String showTipologiaIntervento(Model model, @PathVariable Long id) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        Credentials credentials = credentialsService.getCredentials(id);
        model.addAttribute("credentials",credentials);
        TipologiaIntervento tipologiaIntervento = tipologiaInterventoService.getTipologiaIntervento(id);
        model.addAttribute("tipinterventi",tipologiaInterventoService.getAllTipologieInterventi());
        model.addAttribute("tipologiaintervento", tipologiaIntervento);
        if (tipologiaIntervento != null) {
            // If the tipologia exists
            tipologiaInterventoService.saveTipolofgiaIntervento(tipologiaIntervento);
            return "tipologiaIntervento/tipologiaintervento";
        }

        return "tipologiaIntervento/tipologiainterventi";
    }

    @RequestMapping(value = { "/tipologiainterventi/create" }, method = RequestMethod.GET)
    public String showCreateTipologiaInterventoPrompt(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        model.addAttribute("tipologiainterventoForm", new TipologiaIntervento());
        model.addAttribute("editing", false);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errortipo403";
        }
        return "tipologiaIntervento/tipologiainterventoEdit";
    }

    @RequestMapping(value = { "/tipologiainterventi/create" }, method = RequestMethod.POST)
    public String createTipologiaIntervento(@Valid @ModelAttribute("tipologiainterventoForm") TipologiaIntervento tipologiaIntervento,
                              BindingResult tipologiaBindingResult,
                              Model model) throws Exception {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        this.tipologiaValidator.validate(tipologiaIntervento,tipologiaBindingResult);
        if(!visitor.getRole().equals(ADMIN_ROLE)){
            return "error/errortipo403";
        }
        if(!tipologiaBindingResult.hasErrors() && visitor.getRole().equals(ADMIN_ROLE)) {
            TipologiaIntervento savedTipologiaIntervento = tipologiaInterventoService.saveTipolofgiaIntervento(tipologiaIntervento);

            return "redirect:/tipologiaintervento/" + savedTipologiaIntervento.getId();
        }
        return "tipologiaIntervento/tipologiainterventoEdit";
    }

    @RequestMapping(value = { "/tipologiaintervento/{id}/edit" }, method = RequestMethod.GET)
    public String showEditTipologiaInterventoPrompt(Model model, @PathVariable Long id) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        TipologiaIntervento tipologiaIntervento = tipologiaInterventoService.getTipologiaIntervento(id);
        if (tipologiaIntervento != null && visitor.getRole().equals(ADMIN_ROLE)) {
                // Only admins/owners can edit tipologia
                model.addAttribute("tipologiainterventoForm", tipologiaIntervento);
                model.addAttribute("editing", true);

                return "tipologiaIntervento/tipologiainterventoEdit";
        }
        return "error/errortipo403";
    }

    @RequestMapping(value = { "/tipologiaintervento/{id}/edit" }, method = RequestMethod.POST)
    public String editTipologiaIntervento(@Valid @ModelAttribute("tipologiainterventoForm") TipologiaIntervento tipologiaInterventoForm,
                            BindingResult tipologiaBindingResult,
                            Model model,
                            @PathVariable Long id) throws Exception{

        this.tipologiaValidator.validate(tipologiaInterventoForm, tipologiaBindingResult);

        TipologiaIntervento tipologiaIntervento = tipologiaInterventoService.getTipologiaIntervento(id);
        Credentials visitor = sessionData.getLoggedCredentials();
        if (tipologiaIntervento != null) {
            if (!tipologiaBindingResult.hasErrors()) {
                if (visitor.getRole().equals(ADMIN_ROLE)) {
                    // Only admins/owners can edit tipologia
                    tipologiaIntervento.setNome(tipologiaInterventoForm.getNome());
                    tipologiaIntervento.setCosto(tipologiaInterventoForm.getCosto());
                    tipologiaIntervento.setDescrizione(tipologiaInterventoForm.getDescrizione());
                    tipologiaIntervento.setCodice(tipologiaInterventoForm.getCodice());
                    tipologiaInterventoService.saveTipolofgiaIntervento(tipologiaIntervento);
                }
                return "redirect:/tipologiaintervento/" + id;
            }
            model.addAttribute("visitor", visitor);
            return "tipologiaIntervento/tipologiainterventoEdit";
        }
        return "redirect:/tipologiaintervento/" + id;
    }
}

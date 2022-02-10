package it.uniroma3.siw.gestioneofficina.controller;

import it.uniroma3.siw.gestioneofficina.controller.session.SessionData;
import it.uniroma3.siw.gestioneofficina.controller.validation.CredentialsValidator;
import it.uniroma3.siw.gestioneofficina.controller.validation.MeccanicoValidator;
import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.Meccanico;
import it.uniroma3.siw.gestioneofficina.model.TipologiaIntervento;
import it.uniroma3.siw.gestioneofficina.repository.CredentialsRepository;
import it.uniroma3.siw.gestioneofficina.repository.UserRepository;
import it.uniroma3.siw.gestioneofficina.service.*;
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
public class MeccanicoController {
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
    MeccanicoService meccanicoService;

    @Autowired
    MeccanicoValidator meccanicoValidator;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SessionData sessionData;


    @RequestMapping(value = { "/meccanici" }, method = RequestMethod.GET)
    public String showMeccanici(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        model.addAttribute("meccanici",meccanicoService.getAllMeccanico());

        return "meccanico/meccanici";
    }

    @RequestMapping(value = { "/meccanico/{id}" }, method = RequestMethod.GET)
    public String showMeccanico(Model model, @PathVariable Long id) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        Credentials credentials = credentialsService.getCredentials(id);
        model.addAttribute("credentials",credentials);
        Meccanico meccanico = meccanicoService.getMeccanico(id);
        model.addAttribute("meccanici",meccanicoService.getAllMeccanico());
        model.addAttribute("meccanico", meccanico);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errormeccanico403";
        }
        if (meccanico != null) {
            // If the tipologia exists
            meccanicoService.saveMeccanico(meccanico);
            return "meccanico/meccanico";
        }

        return "meccanico/meccanici";
    }

    @RequestMapping(value = { "/meccanici/create" }, method = RequestMethod.GET)
    public String showCreateMeccanicoPrompt(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        model.addAttribute("meccanicoForm", new Meccanico());
        model.addAttribute("editing", false);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errormeccanico403";
        }
        return "meccanico/meccanicoEdit";
    }

    @RequestMapping(value = { "/meccanici/create" }, method = RequestMethod.POST)
    public String createMeccanico(@Valid @ModelAttribute("meccanicoForm") Meccanico meccanico,
                                            BindingResult meccanicoBindingResult,
                                            Model model) throws Exception {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        this.meccanicoValidator.validate(meccanico,meccanicoBindingResult);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errormeccanico403";
        }
        if(!meccanicoBindingResult.hasErrors()){
            Meccanico savedMeccanico = meccanicoService.saveMeccanico(meccanico);

            return "redirect:/meccanico/" + savedMeccanico.getId();
        }
        return "meccanico/meccanicoEdit";
    }

    @RequestMapping(value = { "/meccanico/{id}/edit" }, method = RequestMethod.GET)
    public String showEditMeccanicoPrompt(Model model, @PathVariable Long id) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        Meccanico meccanico = meccanicoService.getMeccanico(id);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errormeccanico403";
        }
        if (meccanico != null) {
            if (visitor.getRole().equals(ADMIN_ROLE)) {
                // Only admins/owners can edit Meccanico
                model.addAttribute("meccanicoForm", meccanico);
                model.addAttribute("editing", true);

                return "meccanico/meccanicoEdit";
            }
        }
        return "error/403";
    }

    @RequestMapping(value = { "/meccanico/{id}/edit" }, method = RequestMethod.POST)
    public String editMeccanico(@Valid @ModelAttribute("meccanicoForm") Meccanico meccanicoForm,
                                          BindingResult meccanicoBindingResult,
                                          Model model,
                                          @PathVariable Long id) throws Exception{


        Meccanico meccanico = meccanicoService.getMeccanico(id);
        Credentials visitor = sessionData.getLoggedCredentials();
        this.meccanicoValidator.validate(meccanicoForm,meccanicoBindingResult);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errormeccanico403";
        }
        if (meccanico != null) {
            if (visitor.getRole().equals(ADMIN_ROLE) && !meccanicoBindingResult.hasErrors()) {
                // Only admins/owners can edit meccanico
                meccanico.setNome(meccanicoForm.getNome());
                meccanico.setCognome(meccanicoForm.getCognome());
                meccanicoService.saveMeccanico(meccanico);
            }
            return "redirect:/meccanico/" + id;
        }
        model.addAttribute("visitor", visitor);

        return "redirect:/meccanico/" + id;
    }
}

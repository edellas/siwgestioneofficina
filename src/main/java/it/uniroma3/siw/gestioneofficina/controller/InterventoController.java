package it.uniroma3.siw.gestioneofficina.controller;

import com.lowagie.text.DocumentException;
import it.uniroma3.siw.gestioneofficina.controller.session.SessionData;
import it.uniroma3.siw.gestioneofficina.controller.validation.InterventoValidator;
import it.uniroma3.siw.gestioneofficina.controller.validation.TipologiaValidator;
import it.uniroma3.siw.gestioneofficina.model.*;
import it.uniroma3.siw.gestioneofficina.repository.InterventoRepository;
import it.uniroma3.siw.gestioneofficina.service.*;
import it.uniroma3.siw.gestioneofficina.util.InterventiPDFExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static it.uniroma3.siw.gestioneofficina.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.gestioneofficina.model.Credentials.DEFAULT_ROLE;

@Controller
public class InterventoController {
    @Autowired
    InterventoService interventoService;
    @Autowired
    SessionData sessionData;
    @Autowired
    CredentialsService credentialsService;
    @Autowired
    TipologiaInterventoService tipologiaInterventoService;
    @Autowired
    InterventoRepository interventoRepository;
    @Autowired
    UserService userService;
    @Autowired
    InterventoValidator interventoValidator;
    @Autowired
    MeccanicoService meccanicoService;

    @RequestMapping(value = { "/interventi" }, method = RequestMethod.GET)
    public String showInterventi(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        List<Intervento> interventi = interventoRepository.findByCliente(visitor.getUser());
        List<Intervento> tutti = interventoService.getAllInterventi();
        model.addAttribute("tutti",tutti);
        model.addAttribute("interventi",interventi);
        //model.addAttribute("interventi",interventoService.getAllInterventi());

        return "intervento/interventi";
    }

    @RequestMapping(value = { "/intervento/{id}" }, method = RequestMethod.GET)
    public String showIntervento(Model model, @PathVariable Long id) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        Credentials credentials = credentialsService.getCredentials(id);
        model.addAttribute("credentials",credentials);
        Intervento intervento = interventoService.getIntervento(id);
        model.addAttribute("interventi",interventoService.getAllInterventi());
        model.addAttribute("intervento", intervento);
        if(!intervento.getCliente().getId().equals(visitor.getUser().getId()) && visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errorinterventi403";
        }
        if (intervento != null) {
            // If the tipologia exists
            interventoService.saveIntervento(intervento);
            return "intervento/intervento";
        }

        return "intervento/interventi";
    }

    @RequestMapping(value = { "/interventi/create" }, method = RequestMethod.GET)
    public String showCreateInterventoPrompt(Model model) {
        Credentials visitor = sessionData.getLoggedCredentials();
        List<TipologiaIntervento> listtipologie = tipologiaInterventoService.getAllTipologieInterventi();
        List<User> listclienti = userService.getAllClienti();
        List<Meccanico> listmeccanici = meccanicoService.getAllMeccanico();
        model.addAttribute("listmeccanici",listmeccanici);
        model.addAttribute("listclienti", listclienti);
        model.addAttribute("listtipologie", listtipologie);
        model.addAttribute("visitor", visitor);
        model.addAttribute("interventoForm", new Intervento());
        model.addAttribute("editing", false);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errorinterventi403";
        }
        return "intervento/interventoEdit";
    }

    @RequestMapping(value = { "/interventi/create" }, method = RequestMethod.POST)
    public String createIntervento(@Valid @ModelAttribute("interventoForm") Intervento intervento,
                                            BindingResult interventoBindingResult,
                                            Model model) throws Exception {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errorinterventi403";
        }
        Intervento savedIntervento = interventoService.saveIntervento(intervento);

        return "redirect:/intervento/" + savedIntervento.getId();

    }

    @RequestMapping(value = { "/intervento/{id}/edit" }, method = RequestMethod.GET)
    public String showEditInterventoPrompt(Model model, @PathVariable Long id) {
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        Intervento intervento = interventoService.getIntervento(id);
        List<TipologiaIntervento> listtipologie = tipologiaInterventoService.getAllTipologieInterventi();
        List<User> listclienti = userService.getAllClienti();
        List<Meccanico> listmeccanici = meccanicoService.getAllMeccanico();
        model.addAttribute("listmeccanici",listmeccanici);
        model.addAttribute("listclienti", listclienti);
        model.addAttribute("listtipologie", listtipologie);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errorinterventi403";
        }
        if (intervento != null) {
            if (visitor.getRole().equals(ADMIN_ROLE)) {
                // Only admins/owners can edit intervento
                model.addAttribute("interventoForm", intervento);
                model.addAttribute("editing", true);

                return "intervento/interventoEdit";
            }
        }
        return "error/403";
    }

    @RequestMapping(value = { "/intervento/{id}/edit" }, method = RequestMethod.POST)
    public String editIntervento(@Valid @ModelAttribute("interventoForm") Intervento interventoForm,
                                          BindingResult interventoBindingResult,
                                          Model model,
                                          @PathVariable Long id) throws Exception{


        Intervento intervento = interventoService.getIntervento(id);
        Credentials visitor = sessionData.getLoggedCredentials();
        List<TipologiaIntervento> listtipologie = tipologiaInterventoService.getAllTipologieInterventi();
        List<User> listclienti = userService.getAllClienti();
        List<Meccanico> listmeccanici = meccanicoService.getAllMeccanico();
        model.addAttribute("listmeccanici",listmeccanici);
        model.addAttribute("listclienti", listclienti);
        model.addAttribute("listtipologie", listtipologie);
        if(visitor.getRole().equals(DEFAULT_ROLE)){
            return "error/errorinterventi403";
        }
        if (intervento != null) {
            if (visitor.getRole().equals(ADMIN_ROLE)) {
                // Only admins/owners can edit intervento
                intervento.setTipologiaIntervento(interventoForm.getTipologiaIntervento());
                intervento.setCliente(interventoForm.getCliente());
                intervento.setDataPrenotazione(interventoForm.getDataPrenotazione());
                intervento.setDataIntervento(interventoForm.getDataIntervento());
                interventoService.saveIntervento(intervento);
            }
            return "redirect:/intervento/" + id;
        }
        model.addAttribute("visitor", visitor);
        return "intervento/interventoEdit";
        //return "redirect:/intervento/" + id;
    }

    @RequestMapping(value = { "/interventi/export/pdf" }, method = RequestMethod.GET)
    public void exportToPDF(HttpServletResponse response, Model model) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        Credentials visitor = sessionData.getLoggedCredentials();
        model.addAttribute("visitor", visitor);
        List<Intervento> interventi = interventoRepository.findByCliente(visitor.getUser());
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String username = sessionData.getLoggedCredentials().getUserName();
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=fattura_" + username + ".pdf";
        response.setHeader(headerKey, headerValue);

        InterventiPDFExporter exporter = new InterventiPDFExporter(interventi);
        exporter.export(response);

    }
}

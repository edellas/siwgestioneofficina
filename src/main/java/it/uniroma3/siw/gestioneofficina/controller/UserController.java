package it.uniroma3.siw.gestioneofficina.controller;

import it.uniroma3.siw.gestioneofficina.controller.session.SessionData;
import it.uniroma3.siw.gestioneofficina.controller.validation.CredentialsValidator;
import it.uniroma3.siw.gestioneofficina.controller.validation.UserValidator;
import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.repository.CredentialsRepository;
import it.uniroma3.siw.gestioneofficina.repository.UserRepository;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import it.uniroma3.siw.gestioneofficina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static it.uniroma3.siw.gestioneofficina.model.Credentials.ADMIN_ROLE;

/**
 * The UserController handles all interactions involving User data.
 */
@Controller
public class UserController {
	
	@Autowired
	CredentialsRepository credentialsRepository;
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	@Autowired
	UserValidator userValidator;
	@Autowired
	UserService userService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SessionData sessionData;
	
	@RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
	public String profileMe(Model model) {
		Credentials visitor = sessionData.getLoggedCredentials();
		model.addAttribute("visitor", visitor);
		
		Credentials credentials = credentialsRepository.findById(visitor.getId()).orElse(visitor);
		model.addAttribute("credentials", credentials);
		
		return "profile/profile";
	}
	
	@RequestMapping(value = { "/profile/{userName}" }, method = RequestMethod.GET)
	public String profileOthers(Model model, @PathVariable String userName) {
		Credentials visitor = sessionData.getLoggedCredentials();
		model.addAttribute("visitor", visitor);
		
		credentialsRepository.findByUserNameIgnoreCase(userName).ifPresent(c -> {
			model.addAttribute("credentials", c);
		});

		if(userName != visitor.getUserName() && !visitor.getRole().equals("ADMIN")){
			return "error/error403";
		}
		
		return "profile/profile";
	}

	
	@RequestMapping(value = { "/profile/{userName}/edit" }, method = RequestMethod.GET)
	public String editProfile(Model model, @PathVariable String userName) {
		Credentials visitor = sessionData.getLoggedCredentials();
		model.addAttribute("visitor", visitor);

		Credentials credentials = credentialsRepository.findByUserNameIgnoreCase(userName).orElse(null);
		if (credentials != null) {
			if (visitor.getRole().equals(ADMIN_ROLE)) {
				model.addAttribute("userForm", credentials.getUser());
				model.addAttribute("credentialsForm", credentials);

				model.addAttribute("credentials", credentials);
				return "profile/profileEdit";
			}
		}
		return "error/error403";
	}
	
	@RequestMapping(value = { "/profile/{userName}/edit" }, method = RequestMethod.POST)
	public String editProfile(@Valid @ModelAttribute("userForm") User user,
							  BindingResult userBindingResult,
							  Model model,
							  @PathVariable String userName,
							  @RequestParam(value = "role", required = false) Optional<String> role) {
		// validate user fields
		this.userValidator.validate(user, userBindingResult);

		Credentials credentialsProfile = credentialsRepository.findByUserNameIgnoreCase(userName).orElse(null);
		Credentials visitor = sessionData.getLoggedCredentials();
		if (credentialsProfile != null) {
			if (!userBindingResult.hasErrors()) {
				// set the user and store the credentials;
				credentialsProfile.getUser().setFirstName(user.getFirstName());
				credentialsProfile.getUser().setLastName(user.getLastName());
				credentialsRepository.save(credentialsProfile);
				
				return "redirect:/profile/" + userName;
			}
			model.addAttribute("visitor", visitor);
			
			model.addAttribute("userForm", user);
			model.addAttribute("credentials", credentialsProfile);
			return "profile/profileEdit";
		}
		return "redirect:/profile/" + userName;
	}
}

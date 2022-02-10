package it.uniroma3.siw.gestioneofficina.service;


import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.repository.CredentialsRepository;
import it.uniroma3.siw.gestioneofficina.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    CredentialsRepository credentialsRepository;
    @Transactional
    public User getUser(long id) {
        Optional<User> result = this.userRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public User saveUser(User user) throws DataIntegrityViolationException {
        return this.userRepository.save(user);
    }

    @Transactional
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        Iterable<User> iterable = this.userRepository.findAll();
        for(User user : iterable)
            result.add(user);
        return result;
    }

    public List<User> getAllClienti(){
        List<User> result = new ArrayList<>();
        Iterable<Credentials> iterable = this.credentialsRepository.findAll();
        for(Credentials credentials : iterable) {
            if(credentials.getRole().equals("CLIENTE")) {
                result.add(credentials.getUser());
            }
        }
        return result;
    }
}


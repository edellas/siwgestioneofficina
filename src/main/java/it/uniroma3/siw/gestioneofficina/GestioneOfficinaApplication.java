package it.uniroma3.siw.gestioneofficina;

import it.uniroma3.siw.gestioneofficina.model.Credentials;
import it.uniroma3.siw.gestioneofficina.model.User;
import it.uniroma3.siw.gestioneofficina.service.CredentialsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestioneOfficinaApplication {
    @Autowired
    CredentialsService credentialsService;

    public static void main(String[] args) {
        SpringApplication.run(GestioneOfficinaApplication.class, args);
    }

    @Bean
    public InitializingBean populateDatabaseIfEmpty() {
        return () -> {
            if (credentialsService.countAll() == 0) {
                User cliente1 = new User("Aharon", "Salmoni");
                // Users
                Credentials credEdellas = credentialsService.saveCredentials(new Credentials(
                        "edellas",
                        "3001680705",
                        Credentials.ADMIN_ROLE,
                        new User("Edoardo", "Della Seta"))
                );

                Credentials credAharon = credentialsService.saveCredentials(new Credentials(
                        "aharon",
                        "3001680705",
                        Credentials.DEFAULT_ROLE,
                        new User("Aharon", "Salmoni"))
                );

            }

        };
    }

}

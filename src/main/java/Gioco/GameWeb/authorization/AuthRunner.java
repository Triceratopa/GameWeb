package Gioco.GameWeb.authorization;


import Gioco.GameWeb.entities.Role;
import Gioco.GameWeb.entities.WebPlayer;
import Gioco.GameWeb.services.WebPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private WebPlayerService webPlayerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Optional<WebPlayer> adminUser = webPlayerService.findByUsername("admin");
        if (adminUser.isEmpty()) {
            webPlayerService.registerMain("admin", "adminpwd", Set.of(Role.ROLE_ADMIN));
        }

        // Creazione dell'utente user se non esiste
        Optional<WebPlayer> normalUser = webPlayerService.findByUsername("user");
        if (normalUser.isEmpty()) {
            webPlayerService.registerUser(new RegisterRequest("user", "userpwd", "Gatta", "gatta.g@gmail.com", "Grassa"));
        }


    }
}

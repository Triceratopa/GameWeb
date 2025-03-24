package Gioco.GameWeb.controllers;


import Gioco.GameWeb.authorization.AuthResponse;
import Gioco.GameWeb.authorization.LoginRequest;
import Gioco.GameWeb.authorization.RegisterRequest;
import Gioco.GameWeb.entities.LevelRequest;
import Gioco.GameWeb.entities.Role;
import Gioco.GameWeb.entities.WebPlayer;
import Gioco.GameWeb.services.WebPlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final WebPlayerService webPlayerService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-user")
    public WebPlayer getCurrentUser(@AuthenticationPrincipal WebPlayer webPlayer) {
        return webPlayer;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        webPlayerService.registerUser(
        registerRequest
        );
        return ResponseEntity.ok("Registrazione avvenuta con successo");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        log.info("Login request:");
       String token = webPlayerService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ).getToken();
     Long id= webPlayerService.authenticateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()).getId();
        return ResponseEntity.ok().body(new AuthResponse(token, id));
    }

    @PutMapping("/level/{id}")
    public void updateLevel(@PathVariable Long id, @RequestBody LevelRequest levelRequest) {
        webPlayerService.updateLevel(id, levelRequest.getLevel());

    }

    @GetMapping("/{id}")
    public WebPlayer findByUsername(@PathVariable Long id) {
        return webPlayerService.findById(id);
    }
}

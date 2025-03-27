package Gioco.GameWeb.controllers;


import Gioco.GameWeb.authorization.AuthResponse;
import Gioco.GameWeb.authorization.LoginRequest;
import Gioco.GameWeb.authorization.RegisterRequest;
import Gioco.GameWeb.webPlayerEntity.*;
import Gioco.GameWeb.services.WebPlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        webPlayerService.registerUser(
        registerRequest
        );
        String token = webPlayerService.authenticateUser(
                registerRequest.getUsername(),
                registerRequest.getPassword()
        ).getToken();
        Long id= webPlayerService.authenticateUser(
                registerRequest.getUsername(),
                registerRequest.getPassword()).getId();
        return ResponseEntity.ok().body(new AuthResponse(token, id));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public void updateLevel(@PathVariable Long id, @RequestBody LevelRequest levelRequest) {
        webPlayerService.updateLevel(id, levelRequest.getLevel());

    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WebPlayerResponse findByUsername(@PathVariable Long id) {
        return webPlayerService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public WebPlayerResponse update(@PathVariable Long id, @RequestBody WebPlayerUpdateRequest webPlayerUpdateRequest) {
        return webPlayerService.update(id, webPlayerUpdateRequest);
        //solo l'utente può modificare il proprio profilo
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<WebPlayerResponse> findAll() {return webPlayerService.findAll();} //solo l'admin può vedere tutti gli utenti


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()") // tutto grazie a Giuliano
    public void delete(@PathVariable Long id) {
        webPlayerService.delete(id);
    }
}



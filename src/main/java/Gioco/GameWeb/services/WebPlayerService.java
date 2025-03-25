package Gioco.GameWeb.services;

import Gioco.GameWeb.authorization.AuthResponse;
import Gioco.GameWeb.authorization.RegisterRequest;
import Gioco.GameWeb.entities.Role;
import Gioco.GameWeb.entities.WebPlayer;
import Gioco.GameWeb.entities.WebPlayerResponse;
import Gioco.GameWeb.entities.WebPlayerUpdateRequest;
import Gioco.GameWeb.jwt.JwtTokenUtil;
import Gioco.GameWeb.repositories.WebPlayerRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WebPlayerService {

    @Autowired
    private WebPlayerRepository webPlayerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public WebPlayer registerUser(RegisterRequest registerRequest) {
        if (webPlayerRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EntityExistsException("Username già in uso");
        }

        WebPlayer webPlayer= new WebPlayer();
        webPlayer.setUsername(registerRequest.getUsername());
        webPlayer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        webPlayer.setRoles(Set.of(Role.ROLE_USER));
        webPlayer.setEmail(registerRequest.getEmail());
        webPlayer.setSurname(registerRequest.getSurname());
        webPlayer.setName(registerRequest.getName());
        webPlayer.setLevel(1);
        return webPlayerRepository.save(webPlayer);
    }

    public Optional<WebPlayer> findByUsername(String username) {
        return webPlayerRepository.findByUsername(username);
    }

    public AuthResponse authenticateUser(String username, String password)  {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            Long id = jwtTokenUtil.getIdFromToken(token);
            return new AuthResponse(token, id);

        } catch (AuthenticationException e) {
            throw new SecurityException("Credenziali non valide", e);
        }
    }

    public WebPlayerResponse findById(Long id) {
        return webPlayerResponseFromEntity(webPlayerRepository.findById(id) .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + id)));

    }

    public WebPlayer loadUserByUsername(String username)  {
       WebPlayer webPlayer = webPlayerRepository.findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con username: " + username));


        return webPlayer;
    }

    public void updateLevel (Long id, int level) {
        WebPlayer webPlayer = webPlayerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + id));
        webPlayer.setLevel(level);
        webPlayerRepository.save(webPlayer);
    }

    public WebPlayerResponse webPlayerResponseFromEntity(WebPlayer webPlayer) {
        WebPlayerResponse webPlayerResponse = new WebPlayerResponse();
        BeanUtils.copyProperties(webPlayer, webPlayerResponse);
        return webPlayerResponse;
    }

    public List<WebPlayerResponse> webPlayerResponsesListFromEntityList(List<WebPlayer> webPlayer) {
        return webPlayer.stream()
                .map(this::webPlayerResponseFromEntity)
                .toList();
    }

    public List<WebPlayerResponse> findAll() {
        return webPlayerResponsesListFromEntityList(webPlayerRepository.findAll());
    }

    public void registerMain(String username, String password, Set<Role> roles) {
        WebPlayer webPlayer = new WebPlayer();
        webPlayer.setUsername(username);
        webPlayer.setPassword(passwordEncoder.encode(password));
        webPlayer.setRoles(roles);
        webPlayerRepository.save(webPlayer);
    }

    public WebPlayerResponse update(Long id, WebPlayerUpdateRequest webPlayerUpdateRequest) {
        WebPlayer webPlayer = webPlayerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + id));
        BeanUtils.copyProperties(webPlayerUpdateRequest, webPlayer);
        return webPlayerResponseFromEntity(webPlayerRepository.save(webPlayer));
    }

    public void delete(Long id) {
        webPlayerRepository.deleteById(id);
    }

}

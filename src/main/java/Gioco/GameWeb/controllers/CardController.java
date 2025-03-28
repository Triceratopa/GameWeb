package Gioco.GameWeb.controllers;

import Gioco.GameWeb.cardEntity.Card;
import Gioco.GameWeb.cardEntity.CardRequest;
import Gioco.GameWeb.services.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/card")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CardController {
    private final CardService cardService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Card> findAll()
    {return cardService.findAll();}
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Card findById(@PathVariable Long id) {return cardService.findById(id); }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Card save(@RequestBody CardRequest cardRequest) {return cardService.save(cardRequest); }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Card update(@PathVariable Long id,@RequestBody CardRequest cardRequest) {return cardService.update(id,cardRequest); }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {cardService.delete(id); }
}

package Gioco.GameWeb.services;

import Gioco.GameWeb.cardEntity.Card;
import Gioco.GameWeb.cardEntity.CardRequest;
import Gioco.GameWeb.repositories.CardRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    public List<Card> findAll() {return cardRepository.findAll();}
    public Card findById(Long id) {return cardRepository.findById(id).get();}
    public Card cardFromRequest(CardRequest cardRequest){
        Card card = new Card();
        BeanUtils.copyProperties(cardRequest, card);
        return cardRepository.save(card);
    }
    public Card save(CardRequest cardRequest){

        return cardFromRequest(cardRequest);
    }
    public Card update(Long id, CardRequest cardRequest){
        Card card = cardRepository.findById(id).get();
        BeanUtils.copyProperties(cardRequest, card);
        return cardRepository.save(card);
    }
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }

}

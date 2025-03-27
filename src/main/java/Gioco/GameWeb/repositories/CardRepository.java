package Gioco.GameWeb.repositories;

import Gioco.GameWeb.cardEntity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

}

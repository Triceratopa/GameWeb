package Gioco.GameWeb.repositories;


import Gioco.GameWeb.webPlayerEntity.WebPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebPlayerRepository extends JpaRepository<WebPlayer, Long> {
    Optional<WebPlayer> findByUsername(String username);
    boolean existsByUsername(String username);

    int findLevelByUsername(String username);
}

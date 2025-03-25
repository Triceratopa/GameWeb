package Gioco.GameWeb.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebPlayerUpdateRequest {

    private String email;
    private String password;
}

// L'utente può modificare la sua email e la sua password
// se voglio che l'utente possa modificare altre cose aggiungo qui

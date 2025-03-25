package Gioco.GameWeb.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebPlayerResponse {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private int level;
}

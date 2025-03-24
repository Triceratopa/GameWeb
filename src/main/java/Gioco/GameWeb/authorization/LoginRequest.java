package Gioco.GameWeb.authorization;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

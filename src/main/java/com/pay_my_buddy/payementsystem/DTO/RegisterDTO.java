package com.pay_my_buddy.payementsystem.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "Le Username ne peut pas être vide ou null")
    private String username;

    @NotBlank(message = "L'email ne peut pas être vide ou null")
    @Email(message = "Le format de l'email n'est pas valide")
    private String email;

    @NotBlank(message="Le mot de passe ne peut pas être vide ou null")
    @Size(min = 5 , max = 8)
    private String password;
}

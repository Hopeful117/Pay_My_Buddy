package com.pay_my_buddy.payementsystem.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateDTO {


    @NotBlank(message = "Le Username ne peut pas être vide ou null")
    private String username;

    @NotBlank(message = "L'email ne peut pas être vide ou null")
    @Email(message = "Le format de l'email n'est pas valide")
    private String email;

    private String password;
}

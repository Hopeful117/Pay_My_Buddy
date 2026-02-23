package com.pay_my_buddy.payementsystem.DTO;

import com.pay_my_buddy.payementsystem.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDTO {
    @NotNull(message="Le receveur ne peut pas être vide")
    private User receiver;
    @NotBlank(message="La description ne peut pas être vide")
    private String description;
    @Positive(message="Le montant doit être supérieur à 0")
    private BigDecimal amount;


}

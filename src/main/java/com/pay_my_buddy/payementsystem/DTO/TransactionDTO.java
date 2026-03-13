package com.pay_my_buddy.payementsystem.DTO;

import com.pay_my_buddy.payementsystem.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
/**
 * Data Transfer Object (DTO) for transactions between users.
 * This class is used to capture and validate the data required for performing a transaction, including the receiver, description, and amount.
 */
@Data
public class TransactionDTO {
    @NotNull(message = "Le receveur ne peut pas être vide")
    private User receiver;
    @NotBlank(message = "La description ne peut pas être vide")
    private String description;
    @Positive(message = "Le montant doit être supérieur à 0")
    private BigDecimal amount;


}

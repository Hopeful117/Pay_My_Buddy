package com.pay_my_buddy.payementsystem.repository;


import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TransactionRepositoryTest {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setup(){
        User user = new User();
        user.setUsername("diana");
        user.setEmail("diana@example.com");
        user.setPassword("password");
        userRepository.save(user);

        User user2 = new User();
        user2.setUsername("alice");
        user2.setEmail("alice@example.com");
        user2.setPassword("password");
        userRepository.save(user2);

        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setDescription("Test transaction");
        transaction.setSender(user);
        transaction.setReceiver(user2);
        transactionRepository.save(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setAmount(BigDecimal.valueOf(50));
        transaction2.setDescription("Test transaction 2");
        transaction2.setSender(user2);
        transaction2.setReceiver(user);
        transactionRepository.save(transaction2);
    }
    @ParameterizedTest
    @CsvSource({
            "diana,2",
            "alice,2"
    })
    void shouldGetTransactionsByUser(String username, int expectedSize){
        User user = userRepository.findByUsername(username).orElseThrow();
        assertThat(transactionRepository.getTransactionsByUser(user)).hasSize(expectedSize);
    }




}

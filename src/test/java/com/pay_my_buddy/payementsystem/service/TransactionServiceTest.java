package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.TransactionRepository;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for TransactionServiceImpl, using Mockito to mock dependencies and JUnit 5 for testing.
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(1000.00));

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setBalance(BigDecimal.valueOf(500.00));
        receiver.setId(2);


        Transaction transaction1 = new Transaction();
        transaction1.setId(100);
        transaction1.setAmount(BigDecimal.valueOf(50.0));
        transaction1.setSender(sender);
        transaction1.setReceiver(receiver);

        Transaction transaction2 = new Transaction();
        transaction2.setId(101);
        transaction2.setAmount(BigDecimal.valueOf(75.0));
        transaction2.setSender(receiver);
        transaction2.setReceiver(sender);

        List<Transaction> transactions = List.of(transaction1, transaction2);


        lenient().when(transactionRepository.getTransactionsByUser(any(User.class)))
                .thenReturn(transactions);
        lenient().when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

    }

    /**
     * Test to verify that the service returns all transactions for a given user.
     */
    @Test
    void shouldReturnAllTransaction() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setBalance(BigDecimal.valueOf(500.00));
        sender.setId(1);


        List<Transaction> result = transactionService.getTransactionsByUser(sender);


        assertEquals(2, result.size());
        verify(transactionRepository).getTransactionsByUser(any(User.class));
    }

    /**
     * Test to verify that the service returns only transactions where the user is the sender.
     */
    @Test
    void shouldReturnOnlyTransactionWhereUserIsSender() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(500.00));
        List<Transaction> result = transactionService.getTransactionsSentByUser(sender);
        assertEquals(1, result.size());


    }

    /**
     * Test to verify that the service returns only transactions where the user is the receiver.
     */
    @Test
    void shouldReturnOnlyTransactionWhereUserIsReceiver() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(500.00));
        List<Transaction> result = transactionService.getTransactionsReceivedByUserId(sender);
        assertEquals(1, result.size());


    }

    /**
     * Test to verify that the service returns an empty list when no transactions exist for a given user.
     */
    @Test
    void shouldReturnEmptyListIfNoTransactionExist() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(500.00));
        when(transactionRepository.getTransactionsByUser(any(User.class))).thenReturn(List.of());

        List<Transaction> result = transactionService.getTransactionsByUser(sender);
        assertEquals(0, result.size());
        verify(transactionRepository).getTransactionsByUser(any(User.class));


    }

    /**
     * Parameterized test to verify that the service correctly normalizes the transaction amount to two decimal places.
     * It tests various input amounts and their expected normalized values.
     */
    @ParameterizedTest
    @CsvSource({
            "10, 10.00",
            "10.1, 10.10",
            "10.123, 10.12",
            "10.125, 10.13",
            "99.999, 100.00"
    })
    void shouldAddTransactionAndNormalizeAmount(String input, String expected) {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(500.00));

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);
        receiver.setBalance(BigDecimal.valueOf(100.00));

        BigDecimal inputAmount = new BigDecimal(input);
        BigDecimal expectedAmount = new BigDecimal(expected);

        BigDecimal senderBalanceBefore = sender.getBalance();
        BigDecimal receiverBalanceBefore = receiver.getBalance();

        transactionService.transfer(sender, receiver, "Test", inputAmount);

        // Vérifier la transaction sauvegardée
        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction savedTransaction = captor.getValue();

        // Vérifier que le montant est arrondi correctement
        assertEquals(expectedAmount, savedTransaction.getAmount());

        // Vérifier les soldes après transfert
        assertEquals(senderBalanceBefore.subtract(expectedAmount), sender.getBalance());
        assertEquals(receiverBalanceBefore.add(expectedAmount), receiver.getBalance());
    }

    /**
     * Test to verify that the service throws an IllegalArgumentException when either the sender or receiver does not exist.
     * It mocks the user repository to indicate that the user does not exist and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenUserDoesntExist() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(500.00));

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);
        receiver.setBalance(BigDecimal.valueOf(100.00));

        BigDecimal amount = new BigDecimal("10.00");


        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Utilisateur non trouvé",
                exception.getMessage());


    }

    /**
     * Test to verify that the service throws an IllegalArgumentException when the transaction description is empty.
     * It asserts that the service validates the input and handles it correctly.
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenDescriptionIsEmpty() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = new BigDecimal("10.00");


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(sender, receiver, "", amount));
        assertEquals("La description ne peut pas être vide",
                exception.getMessage());

    }

    /**
     * Test to verify that the service throws an IllegalArgumentException when the transaction amount is null.
     * It asserts that the service validates the input and handles it correctly.
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenAmountIsNull() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = null;


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Le montant ne peut pas être null",
                exception.getMessage());


    }

    /**
     * Test to verify that the service throws an IllegalArgumentException when the transaction amount is zero.
     * It asserts that the service validates the input and handles it correctly.
     */
    @Test
    void shouldThrowIllegalArgumentExceptionWhenAmountIsZero() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = new BigDecimal(0);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Le montant doit être supérieur à 0",
                exception.getMessage());

    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenSenderAndReceiverAreTheSame() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(1000.00));
        User receiver = sender;
        BigDecimal amount = new BigDecimal(10);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Auto-transactions non autorisées",
                exception.getMessage());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSenderHasInsufficientBalance() {
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        sender.setBalance(BigDecimal.valueOf(50.00));
        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);
        BigDecimal amount = new BigDecimal(100);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Fonds insuffisants",
                exception.getMessage());
    }
}

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

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
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


        lenient().when(transactionRepository.getTransactionsBySender(any(User.class)))
                .thenReturn(transactions);
        lenient().when(userRepository.existsByUsername(any(String.class))).thenReturn(true);

    }

    @Test
    void shouldReturnAllTransaction() {
        User sender = new User("john", "john@mail.com", "password");



        List<Transaction> result = transactionService.getTransactionsByUser(sender);


        assertEquals(2, result.size());
        verify(transactionRepository).getTransactionsBySender(any(User.class));
    }

    @Test
    void shouldReturnOnlyTransactionWhereUserIsSender(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        List<Transaction> result= transactionService.getTransactionsSentByUser(sender);
        assertEquals(1,result.size());



    }
    @Test
    void shouldReturnOnlyTransactionWhereUserIsReceiver(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);
        List<Transaction> result= transactionService.getTransactionsReceivedByUserId(sender);
        assertEquals(1,result.size());



    }
    @Test
    void shouldReturnEmptyListIfNoTransactionExist(){
        User sender = new User("john", "john@mail.com", "password");
        when(transactionRepository.getTransactionsBySender(any(User.class))).thenReturn(List.of());

        List<Transaction> result = transactionService.getTransactionsByUser(sender);
        assertEquals(0,result.size());
        verify(transactionRepository).getTransactionsBySender(any(User.class));


    }
    @ParameterizedTest
    @CsvSource({
            "10, 10.00",
            "10.1, 10.10",
            "10.123, 10.12",
            "10.125, 10.13",
            "99.999, 100.00"
    })

    void shouldAddTransactionAndNormalizeAmount(String input, String expected){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal inputAmount = new BigDecimal(input);
        BigDecimal expectedAmount = new BigDecimal(expected);

        transactionService.transfer(sender, receiver, "Test", inputAmount);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository).save(captor.capture());

        Transaction savedTransaction = captor.getValue();

        assertEquals(expectedAmount, savedTransaction.getAmount());
    }
    @Test
    void shouldThrowRuntimeExceptionWhenRepositoryFails() {

        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = new BigDecimal("10.00");



        when(transactionRepository.save(any(Transaction.class)))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(sender, receiver, "Test", amount)
        );

        assertEquals("Une erreur s'est produite lors de la transaction",
                exception.getMessage());

        verify(transactionRepository).save(any(Transaction.class));
    }


    @Test
void shouldThrowIllegalArgumentExceptionWhenUserDoesntExist(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = new BigDecimal("10.00");



        when(userRepository.existsByUsername(any(String.class))).thenReturn(false);
        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Utilisateur non trouvé",
                exception.getMessage());



    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenDescriptionIsEmpty(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = new BigDecimal("10.00");




        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->transactionService.transfer(sender, receiver, "", amount));
        assertEquals("La description ne peut pas être vide",
                exception.getMessage());

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenAmountIsNull(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = null;




        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Le montant ne peut pas être null",
                exception.getMessage());


    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenAmountIsZero(){
        User sender = new User("john", "john@mail.com", "password");
        sender.setId(1);

        User receiver = new User("jane", "jane@mail.com", "password");
        receiver.setId(2);

        BigDecimal amount = new BigDecimal(0);




        IllegalArgumentException exception= assertThrows(IllegalArgumentException.class,()->transactionService.transfer(sender, receiver, "Test", amount));
        assertEquals("Le montant doit être supérieur à 0",
                exception.getMessage());

    }
    }

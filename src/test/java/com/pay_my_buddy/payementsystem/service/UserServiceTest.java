package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.UpdateDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @ParameterizedTest
    @CsvSource({
            "john, john@mail.com, password123",
            "alice, alice@test.fr, Azerty123!",
            "bob_92, bob92@gmail.com, StrongPass1",
            "charlie.dev, charlie.dev@company.org, DevPass2024",
            "user-test, user.test+1@mail.com, P@ssw0rd!"

    })
    void shouldCreateUserSuccessfully(String username, String email, String password) {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        userService.createUser(username, email, password);
        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();


        assert savedUser.getUsername().equals(username);
        assert savedUser.getEmail().equals(email);
        assert savedUser.getPassword().equals("hashedPassword");

        verify(passwordEncoder).encode(password);


    }

    @ParameterizedTest
    @CsvSource({
            "john, new@mail.com, password123",
            "alice, test@test.com, Azerty123!"
    })
    void shouldThrowExceptionWhenUsernameAlreadyExists(String username, String email, String password) {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(username, email, password));
        assertEquals("Le username ou l'email sont déjà utilisés", exception.getMessage());


    }

    @ParameterizedTest
    @CsvSource({
            "newuser, john@mail.com, password123",
            "anotheruser, alice@test.fr, Azerty123!"
    })
    void shouldThrowExceptionWhenEmailAlreadyExists(String username, String email, String password) {
        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.createUser(username, email, password));
        assertEquals("Le username ou l'email sont déjà utilisés", exception.getMessage());


    }

    @ParameterizedTest
    @CsvSource({
            "john, john@mail.com, password123",
            "alice, alice@test.fr, Azerty123!"
    })
    void shouldThrowExceptionWhenRepositoryFails(String username, String email, String password) {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database Error"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.createUser(username, email, password)
        );
        assertEquals("Une erreur s'est produite lors de la création de l'utilisateur",
                exception.getMessage());

        verify(userRepository).save(any(User.class));


    }

    @ParameterizedTest
    @CsvSource({
            "1,john, john@mail.com, password123",
            "2,alice, alice@test.fr, Azerty123!",
            "3,bob_92, bob92@gmail.com, StrongPass1",
            "4,charlie.dev, charlie.dev@company.org, DevPass2024",
            "5,user-test, user.test+1@mail.com, P@ssw0rd!"

    })
    void shouldUpdatePasswordSuccessfully(String id, String username, String email, String password) {
        User user = new User(username, email, "oldpassword");

        int userId = Integer.parseInt(id);
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setUsername(username);
        updateDTO.setPassword(password);
        updateDTO.setEmail(email);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        userService.updateUser(userId, updateDTO);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();


        assertAll(
                () -> assertEquals(username, savedUser.getUsername(), "Username should be updated"),
                () -> assertEquals(email, savedUser.getEmail(), "Email should be updated"),
                () -> assertEquals("hashedPassword", savedUser.getPassword(), "Password should be encoded"));

    }


}

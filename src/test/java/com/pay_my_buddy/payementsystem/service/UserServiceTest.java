package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.UpdateDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UserServiceImpl, using Mockito to mock dependencies and JUnit 5 for testing.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    /**
     * Test method to verify that a user is created successfully when valid input is provided.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param password the password of the new user
     */
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

    /**
     * Test method to verify that an exception is thrown when trying to create a user with an existing username.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param password the password of the new user
     */
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

    /**
     * Test method to verify that an exception is thrown when trying to create a user with an existing email.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param password the password of the new user
     */
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

    /**
     * Test method to verify that an exception is thrown when the repository fails to save a new user.
     * It mocks the repository to throw an exception and asserts that the service handles it correctly.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param password the password of the new user
     */
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

    /**
     * Test method to verify that a user's password is updated successfully when valid input is provided.
     *
     * @param id       the ID of the user to update
     * @param username the new username of the user
     * @param email    the new email of the user
     * @param password the new password of the user
     */
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

    /**
     * Test method to verify that a user's email is updated successfully when valid input is provided.
     *
     * @param id       the ID of the user to update
     * @param username the new username of the user
     * @param email    the new email of the user
     * @param password the new password of the user
     */
    @ParameterizedTest
    @CsvSource({
            "1,john, john@mail.com, password123",
            "2,alice, alice@test.fr, Azerty123!",
            "3,bob_92, bob92@gmail.com, StrongPass1",
            "4,charlie.dev, charlie.dev@company.org, DevPass2024",
            "5,user-test, user.test+1@mail.com, P@ssw0rd!"

    })
    void shouldUpdateEmailSuccessfully(String id, String username, String email, String password) {
        User user = new User(username, "oldemail", password);
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

    /**
     * Test method to verify that a user's username is updated successfully when valid input is provided.
     *
     * @param id       the ID of the user to update
     * @param username the new username of the user
     * @param email    the new email of the user
     * @param password the new password of the user
     */
    @ParameterizedTest
    @CsvSource({
            "1,john, john@mail.com, password123",
            "2,alice, alice@test.fr, Azerty123!",
            "3,bob_92, bob92@gmail.com, StrongPass1",
            "4,charlie.dev, charlie.dev@company.org, DevPass2024",
            "5,user-test, user.test+1@mail.com, P@ssw0rd!"

    })
    void shouldUpdateUsernameSuccessfully(String id, String username, String email, String password) {
        User user = new User("oldusername", email, password);
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

    /**
     * Test method to verify that an exception is thrown when trying to update a user that does not exist.
     * It mocks the repository to return an empty Optional and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenUserNotFoundForUpdate() {
        int userId = 1;
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setUsername("newusername");
        updateDTO.setPassword("newpassword");
        updateDTO.setEmail("newemail");
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, updateDTO));
        assertEquals("Utilisateur non trouvé", exception.getMessage());

    }

    /**
     * Test method to verify that an exception is thrown when trying to update a user with an email that already exists.
     * It mocks the repository to indicate that the email already exists and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsForUpdate() {
        int userId = 1;
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setUsername("newusername");
        updateDTO.setPassword("newpassword");
        updateDTO.setEmail("existingemail");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User("oldusername", "oldemail", "password")));
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, updateDTO));
        assertEquals("Veuillez utiliser un email différent", exception.getMessage());
    }

    /**
     * Test method to verify that an exception is thrown when trying to update a user with a username that already exists.
     * It mocks the repository to indicate that the username already exists and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExistsForUpdate() {
        int userId = 1;
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setUsername("existingusername");
        updateDTO.setPassword("newpassword");
        updateDTO.setEmail("newemail");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User("oldusername", "oldemail", "password")));
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(userId, updateDTO));
        assertEquals("Le nom d'utilisateur est déjà utilisé", exception.getMessage());
    }

    /**
     * Test method to verify that an exception is thrown when the repository fails to save an updated user.
     * It mocks the repository to throw an exception and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenRepositoryFailsForUpdate() {
        int userId = 1;
        UpdateDTO updateDTO = new UpdateDTO();
        updateDTO.setUsername("newusername");
        updateDTO.setPassword("newpassword");
        updateDTO.setEmail("newemail");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User("oldusername", "oldemail", "password")));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database Error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.updateUser(userId, updateDTO));
        assertEquals("Une erreur s'est produite lors de la mise à jour de l'utilisateur", exception.getMessage());
        verify(userRepository).save(any(User.class));
    }

    /**
     * Test method to verify that a user is retrieved successfully by their email when the user exists.
     * It mocks the repository to return a user and asserts that the service returns the correct user.
     */
    @Test
    void shouldGetUserByEmailSuccessfully() {
        String email = "email@example.com";
        User user = new User("username", email, "password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        User foundUser = userService.getUserByEmail(email);
        assertEquals(email, foundUser.getEmail());
        verify(userRepository).findByEmail(email);
    }

    /**
     * Test method to verify that an exception is thrown when trying to retrieve a user by email that does not exist.
     * It mocks the repository to return an empty Optional and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        String email = "email@example.com";
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByEmail(email));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    /**
     * Test method to verify that a user is retrieved successfully by their username when the user exists.
     * It mocks the repository to return a user and asserts that the service returns the correct user.
     */
    @Test
    void shouldGetUserByUsernameSuccessfully() {
        String username = "username";
        User user = new User(username, "email@example.com", "password");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        User foundUser = userService.getUserByUsername(username);
        assertEquals(username, foundUser.getUsername());
        verify(userRepository).findByUsername(username);
    }

    /**
     * Test method to verify that an exception is thrown when trying to retrieve a user by username that does not exist.
     * It mocks the repository to return an empty Optional and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        String username = "username";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserByUsername(username));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername(username);
    }

    /**
     * Test method to verify that a connection is added successfully between two users when valid input is provided.
     * It mocks the repository to return the users and asserts that the connections are updated correctly.
     *
     * @param id       the ID of the user adding the connection
     * @param username the username of the user adding the connection
     * @param email    the email of the user adding the connection
     * @param password the password of the user adding the connection
     */
    @ParameterizedTest
    @CsvSource({
            "1,john, john@mail.com, password123",
            "2,alice, alice@test.fr, Azerty123!",
            "3,bob_92, bob92@gmail.com, StrongPass1",
            "4,charlie.dev, charlie.dev@company.org, DevPass2024",
            "5,user-test, user.test+1@mail.com, P@ssw0rd!"

    })
    void shouldAddConnectionSuccessfully(String id, String username, String email, String password) {
        User user = new User(username, email, password);
        User friend = new User("friend", "email", "password");
        int userId = Integer.parseInt(id);
        int friendId = 100;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
        userService.addConnection(userId, friendId);
        assertAll(
                () -> assertTrue(user.getConnections().contains(friend), "User should have friend in connections"),
                () -> assertTrue(friend.getConnections().contains(user), "Friend should have user in connections"));

        ArgumentCaptor<Set<User>> captor = ArgumentCaptor.forClass(Set.class);

        verify(userRepository, times(1)).saveAll(captor.capture());

        Set<User> savedUsers = captor.getValue();

        assertAll(
                () -> assertEquals(2, savedUsers.size(), "Two users should be saved"),
                () -> assertTrue(savedUsers.contains(user)),
                () -> assertTrue(savedUsers.contains(friend))
        );
        verify(userRepository).saveAll(anySet());
    }

    /**
     * Test method to verify that an exception is thrown when trying to add a connection for a user that does not exist.
     * It mocks the repository to return an empty Optional and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenUserNotFoundForAddConnection() {
        int userId = 1;
        int friendId = 100;
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.addConnection(userId, friendId));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    /**
     * Test method to verify that an exception is thrown when trying to add a connection that already exists.
     * It mocks the repository to return users that are already connected and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenAlreadyFriendsForAddConnection() {
        int userId = 1;
        int friendId = 100;
        User user = new User("username", "email", "password");
        user.setId(userId);
        User friend = new User("friend", "email", "password");
        friend.setId(friendId);
        user.getConnections().add(friend);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        boolean alreadyFriend = user.getConnections().stream().anyMatch(f -> f.getId() == friendId);
        assertTrue(alreadyFriend, "Users should already be friends");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.addConnection(userId, friendId));
        assertEquals("L'utilisateur est déjà dans vos relations", exception.getMessage());
        verify(userRepository).findById(userId);

    }

    /**
     * Test method to verify that an exception is thrown when trying to add a connection for a friend that does not exist.
     * It mocks the repository to return an empty Optional for the friend and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenFriendNotFoundForAddConnection() {
        int userId = 1;
        int friendId = 100;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User("username", "email", "password")));
        when(userRepository.findById(friendId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.addConnection(userId, friendId));
        assertEquals("Utilisateur non trouvé", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(userRepository).findById(friendId);
    }

    /**
     * Test method to verify that an exception is thrown when the repository fails to save the updated users after adding a connection.
     * It mocks the repository to throw an exception and asserts that the service handles it correctly.
     */
    @Test
    void shouldThrowExceptionWhenRepositoryFailsForAddConnection() {
        int userId = 1;
        int friendId = 100;
        User user = new User("username", "email", "password");
        User friend = new User("friend", "email", "password");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findById(friendId)).thenReturn(Optional.of(friend));
        when(userRepository.saveAll(anySet())).thenThrow(new RuntimeException("Database Error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.addConnection(userId, friendId));
        assertEquals("Une erreur s'est produite lors de l'ajout de la relation", exception.getMessage());
        verify(userRepository).saveAll(anySet());
    }
}




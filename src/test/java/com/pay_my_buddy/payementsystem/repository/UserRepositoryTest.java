package com.pay_my_buddy.payementsystem.repository;

import com.pay_my_buddy.payementsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveUser(){
        User user = new User("Jeanne","jeanne@mail.com","password1");
        User saved= userRepository.save(user);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldFindUserByUsername(){
        User saved = userRepository.save(new User("Jeanne","jeanne@mail.com","password2"));
        Optional<User> found = userRepository.findByUsername(saved.getUsername());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("Jeanne");
    }

    @Test
    void shouldFindUserByEmail(){
        User saved = userRepository.save(new User("Jeanne","jeanne@mail.com","password2"));
        Optional<User> found = userRepository.findByEmail(saved.getEmail());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("jeanne@mail.com");

    }

    @Test
    void shouldReturnTrueIfUsernameExists(){
        User saved = userRepository.save(new User("Jeanne","jeanne@mail.com","password2"));
        boolean exists = userRepository.existsByUsername(saved.getUsername());
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnTrueIfEmailExists(){
        User saved = userRepository.save(new User("Jeanne","jeanne@mail.com","password2"));
        boolean exists = userRepository.existsByEmail(saved.getEmail());
        assertThat(exists).isTrue();

    }
}

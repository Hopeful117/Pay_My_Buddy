package com.pay_my_buddy.payementsystem.repository;

import com.pay_my_buddy.payementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for managing User entities in the database.
 * This interface extends JpaRepository, providing CRUD operations and custom query methods for User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);


    Optional<User> findByEmail(String email);


    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}

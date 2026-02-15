package com.pay_my_buddy.payementsystem.repository;

import com.pay_my_buddy.payementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User findUserById(int id);

    @Query(value=("CALL deactivate_user(:id) "), nativeQuery = true)
    void updateIsActiveById(Boolean isActive, int id);

    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    void updateUserPasswordById(String password, int id);

    @Modifying
    @Query(value=("CALL anonymyze_user(:id)"), nativeQuery = true)
    void anonymizeUserById(int id);


}

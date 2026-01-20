CREATE DATABASE IF NOT EXISTS paymybuddy_db;
USE paymybuddy_db;

CREATE TABLE `user` (
  `id` integer PRIMARY KEY,
  `username` VARCHAR(255) UNIQUE NOT NULL,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `is_active` BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE `user_connection` (
  `user_id` int,
  `connection_id` int,
  `created_at` datetime NOT NULL
);

CREATE TABLE `transaction` (
  `id` integer PRIMARY KEY,
  `sender` int,
  `receiver` int,
  `description` varchar(255) NOT NULL,
  `amount` decimal(10,2) NOT NULL
);

ALTER TABLE `user_connection` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `user_connection` ADD FOREIGN KEY (`connection_id`) REFERENCES `user` (`id`);

ALTER TABLE `transaction` ADD FOREIGN KEY (`sender`) REFERENCES `user` (`id`);

ALTER TABLE `transaction` ADD FOREIGN KEY (`receiver`) REFERENCES `user` (`id`);

ALTER TABLE user_connection
  ADD CONSTRAINT fk_user_connection_user
  FOREIGN KEY (user_id) REFERENCES `user` (id)
  ON DELETE CASCADE;

ALTER TABLE transaction
    ADD CONSTRAINT fk_transaction_sender
    FOREIGN KEY (sender) REFERENCES `user` (id)
    ON DELETE RESTRICT;

ALTER TABLE transaction
    ADD CONSTRAINT fk_transaction_receiver
    FOREIGN KEY (receiver) REFERENCES `user` (id)
    ON DELETE RESTRICT;

CREATE INDEX idx_user_connection_user
ON `user_connection` (`user_id`);

CREATE INDEX idx_user_connection_connection
ON `user_connection` (`connection_id`);

CREATE INDEX idx_transaction_sender
ON `transaction` (`sender`);

CREATE INDEX idx_transaction_receiver
ON `transaction` (`receiver`);

DELIMITER //

CREATE PROCEDURE deactivate_user(IN p_user_id INT)
BEGIN
    -- Passe l'utilisateur en inactif
    UPDATE user
    SET is_active = 0
    WHERE id = p_user_id;

    -- Vérification
    SELECT id, username, is_active
    FROM user
    WHERE id = p_user_id;
END;
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE anonymize_user(IN p_user_id INT)
BEGIN
    -- Passe l'utilisateur en inactif
    UPDATE user
    SET
        is_active = 0,
        username = CONCAT('user', p_user_id),
        email = CONCAT('user', p_user_id, '@anon.com'),
        password = ''
    WHERE id = p_user_id;

    -- Vérification
    SELECT id, username, email, password, is_active
    FROM user
    WHERE id = p_user_id;
END;
//

DELIMITER ;
DELIMITER //

CREATE PROCEDURE add_connection(IN p_user_id INT, IN p_connection_id INT)
BEGIN
    -- Empêche de se connecter à soi-même
    IF p_user_id = p_connection_id THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cannot connect user to itself';
    ELSE
        INSERT IGNORE INTO user_connection (user_id, connection_id)
        VALUES (p_user_id, p_connection_id);
    END IF;
END;
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE remove_connection(IN p_user_id INT, IN p_connection_id INT)
BEGIN
    DELETE FROM user_connection
    WHERE user_id = p_user_id AND connection_id = p_connection_id;
END;
//

DELIMITER ;

DELIMITER //

CREATE PROCEDURE add_transaction(
    IN p_sender INT,
    IN p_receiver INT,
    IN p_description VARCHAR(255),
    IN p_amount DECIMAL(10,2)
)
BEGIN
    -- Vérifie que le montant est positif
    IF p_amount <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Amount must be positive';
    ELSE
        INSERT INTO transaction (sender, receiver, description, amount)
        VALUES (p_sender, p_receiver, p_description, p_amount);
    END IF;
END;
//

DELIMITER ;
DELIMITER //

CREATE PROCEDURE list_connections(IN p_user_id INT)
BEGIN
    SELECT u.id, u.username
    FROM user_connection uc
    JOIN user u ON uc.connection_id = u.id
    WHERE uc.user_id = p_user_id AND u.is_active = 1;
END;
//

DELIMITER ;
DELIMITER //

CREATE PROCEDURE list_transactions(IN p_user_id INT)
BEGIN
    SELECT t.id, t.sender, t.receiver, t.description, t.amount, t.created_at
    FROM transaction t
    WHERE t.sender = p_user_id OR t.receiver = p_user_id;
END;
//

DELIMITER ;




SHOW TABLES;

DESCRIBE `user`;
DESCRIBE `user_connection`;
DESCRIBE `transaction`;

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


SHOW TABLES;

DESCRIBE `user`;
DESCRIBE `user_connection`;
DESCRIBE `transaction`;

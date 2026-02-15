INSERT INTO `user` (id, username, email, password) VALUES
(1, 'alice', 'alice@example.com', '$2a$10$gFZy9b1HshkQ4E2R9Y2/2OZK2H7l0tW0qN9j/1qF8b4fTzY2k1Wv6'
),
(2, 'bob', 'bob@example.com', '$2a$10$gFZy9b1HshkQ4E2R9Y2/2OZK2H7l0tW0qN9j/1qF8b4fTzY2k1Wv6'),
(3, 'charlie', 'charlie@example.com', '$2a$10$gFZy9b1HshkQ4E2R9Y2/2OZK2H7l0tW0qN9j/1qF8b4fTzY2k1Wv6
'),
(4, 'diana', 'diana@example.com', '$2a$10$gFZy9b1HshkQ4E2R9Y2/2OZK2H7l0tW0qN9j/1qF8b4fTzY2k1Wv6
');

INSERT INTO `user_connection` (user_id, connection_id, created_at) VALUES
(1, 2, '2025-01-10 09:30:00'),
(1, 3, '2025-01-12 14:15:00'),
(2, 3, '2025-01-15 18:45:00'),
(3, 4, '2025-01-20 11:00:00');


INSERT INTO `transaction` (id, sender, receiver, description, amount) VALUES
(1, 1, 2, 'Remboursement restaurant', 25.50),
(2, 2, 1, 'Participation cinéma', 12.00),
(3, 3, 1, 'Cadeau anniversaire', 40.00),
(4, 4, 3, 'Partage abonnement', 8.99);
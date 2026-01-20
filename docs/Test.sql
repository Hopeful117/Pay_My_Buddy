-- Vérifier les utilisateurs
SELECT * FROM `user`;

-- Vérifier les connexions
SELECT u.username, c.username AS connection
FROM user_connection uc
JOIN user u ON uc.user_id = u.id
JOIN user c ON uc.connection_id = c.id;

-- Vérifier les transactions avec noms
SELECT s.username AS sender,
       r.username AS receiver,
       t.description,
       t.amount
FROM transaction t
JOIN user s ON t.sender = s.id
JOIN user r ON t.receiver = r.id;

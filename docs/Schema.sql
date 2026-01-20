CREATE TABLE `user` (
  `id` integer PRIMARY KEY,
  `username` VARCHAR(255) UNIQUE NOT NULL,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL
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

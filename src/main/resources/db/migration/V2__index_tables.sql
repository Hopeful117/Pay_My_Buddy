
-- INDEXES UTILES
CREATE INDEX idx_user_connections_user ON user_connections(user_id);
CREATE INDEX idx_user_connections_connection ON user_connections(connection_id);
CREATE INDEX idx_transactions_sender ON transactions(sender);
CREATE INDEX idx_transactions_receiver ON transactions(receiver);


CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    token VARCHAR(1000) NOT NULL,
    username VARCHAR(255) NOT NULL,
    token_type VARCHAR(10) NOT NULL,  -- 'access' o 'refresh'
    expiration TIMESTAMP NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    is_valid BOOLEAN DEFAULT TRUE
);

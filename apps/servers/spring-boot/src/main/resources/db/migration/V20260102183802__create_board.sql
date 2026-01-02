CREATE TABLE boards (
    id BIGSERIAL PRIMARY KEY,
    board_name VARCHAR(255) UNIQUE NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    modified_by VARCHAR(255),
    modified_date TIMESTAMP
);

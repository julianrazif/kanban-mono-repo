CREATE TABLE columns (
    id BIGSERIAL PRIMARY KEY,
    column_name VARCHAR(255) UNIQUE NOT NULL,
    board_id BIGINT NOT NULL,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    modified_by VARCHAR(255),
    modified_date TIMESTAMP,
    CONSTRAINT fk_board FOREIGN KEY (board_id) REFERENCES boards(id)
);

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    column_id BIGINT NOT NULL,
    user_id BIGINT,
    board_id BIGINT NOT NULL,
    title VARCHAR(255),
    description TEXT,
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    modified_by VARCHAR(255),
    modified_date TIMESTAMP,
    CONSTRAINT fk_column FOREIGN KEY (column_id) REFERENCES columns(id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_board FOREIGN KEY (board_id) REFERENCES boards(id)
);

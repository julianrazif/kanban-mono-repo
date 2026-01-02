CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    organization VARCHAR(255) DEFAULT 'Hacktiv8',
    created_by VARCHAR(255),
    created_date TIMESTAMP,
    modified_by VARCHAR(255),
    modified_date TIMESTAMP
);

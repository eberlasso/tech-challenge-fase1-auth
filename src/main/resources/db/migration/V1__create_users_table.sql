-- V1__create_users_table.sql
-- Description: Initial schema for user management as per Phase 1 requirements.

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       full_name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       login_handle VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       user_role VARCHAR(50) NOT NULL,

    -- Embedded Address fields
                       address_street VARCHAR(255) NOT NULL,
                       address_number VARCHAR(100) NOT NULL,
                       address_city VARCHAR(100) NOT NULL,
                       address_zip_code VARCHAR(20) NOT NULL,

    -- Audit field required by the project scope
                       last_modification_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index for performance on searches by name (Project Requirement)
CREATE INDEX idx_users_full_name ON users(full_name);
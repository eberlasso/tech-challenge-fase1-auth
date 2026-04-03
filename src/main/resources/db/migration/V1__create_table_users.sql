-- 1. Create the Domain table for Roles (Independent)
-- Stores the types of access available in the system
CREATE TABLE tb_roles (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(50) NOT NULL UNIQUE, -- e.g., 'RESTAURANT_OWNER', 'CUSTOMER'
                          description TEXT
);

-- 2. Create the Address table (Independent)
-- Stores physical location data
CREATE TABLE tb_addresses (
                              id BIGSERIAL PRIMARY KEY,
                              street VARCHAR(255) NOT NULL,
                              number VARCHAR(20) NOT NULL,
                              city VARCHAR(100) NOT NULL,
                              zip_code VARCHAR(20) NOT NULL,
                              state VARCHAR(50) NOT NULL
);

-- 3. Create the People table (Independent Identity)
-- Stores the core personal information (Audit fields included)
CREATE TABLE tb_people (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           created_at TIMESTAMP NOT NULL,
                           last_updated_at TIMESTAMP
);

-- 4. Create the User Relationship table (Dependent on ALL above)
-- Binds a Person to a Role, an Address, and Auth Credentials
CREATE TABLE tb_user_relationships (
                                       id BIGSERIAL PRIMARY KEY,
                                       person_id BIGINT NOT NULL,
                                       address_id BIGINT NOT NULL,
                                       role_id BIGINT NOT NULL,
                                       login VARCHAR(255) NOT NULL UNIQUE,
                                       password VARCHAR(255) NOT NULL,
                                       created_at TIMESTAMP NOT NULL,
                                       last_updated_at TIMESTAMP,

    -- Relationship Constraints
                                       CONSTRAINT fk_rel_person
                                           FOREIGN KEY (person_id)
                                               REFERENCES tb_people(id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT fk_rel_address
                                           FOREIGN KEY (address_id)
                                               REFERENCES tb_addresses(id)
                                               ON DELETE SET NULL,

                                       CONSTRAINT fk_rel_role
                                           FOREIGN KEY (role_id)
                                               REFERENCES tb_roles(id)
);

-- 5. Insert Initial Domain Data
-- This ensures the system has the mandatory roles from day one
INSERT INTO tb_roles (name, description) VALUES
                                             ('RESTAURANT_OWNER', 'User authorized to manage restaurants, menus, and orders'),
                                             ('CUSTOMER', 'User authorized to browse restaurants and place orders');

-- 6. Index for Performance
-- Optimizes the most frequent query in the system: login/authentication
CREATE INDEX idx_user_relationship_login ON tb_user_relationships(login);
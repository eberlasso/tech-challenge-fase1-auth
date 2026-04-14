-- Description: Remove unique constraint from login_handle in users table
-- Date: 2026-04-13

ALTER TABLE users DROP CONSTRAINT IF EXISTS users_login_handle_key;
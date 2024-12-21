-- Clear existing data (optional, falls notwendig)
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE ratings RESTART IDENTITY CASCADE;
TRUNCATE TABLE transactions RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;

-- Insert test data (only the user)
-- Custom user with no related data (e.g., no products, orders, transactions)
INSERT INTO users (id, created_at, updated_at, email, phone_number, sub)
VALUES (999, NOW(), NOW(), 'testuser@example.com', '1234567890', 'sub999')
ON CONFLICT (id) DO NOTHING;

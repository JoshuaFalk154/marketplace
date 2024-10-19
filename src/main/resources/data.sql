-- Clear existing data
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE products RESTART IDENTITY CASCADE;
TRUNCATE TABLE ratings RESTART IDENTITY CASCADE;
TRUNCATE TABLE transactions RESTART IDENTITY CASCADE;

-- Insert test data
-- Existing user with id 152
INSERT INTO users (id, created_at, updated_at, email, phone_number, sub)
VALUES (152, NOW(), NOW(), 'user152@example.com', '1234567890', 'sub152')
ON CONFLICT (id) DO NOTHING;

-- Transactions for user 152
INSERT INTO transactions (id, amount, status, created_at, updated_at, timestamp, payment_method, transaction_id)
VALUES
  (NEXTVAL('transactions_seq'), 100.0, 1, NOW(), NOW(), NOW(), 'Credit Card', 'txn_001');

-- Orders for user 152
INSERT INTO orders (id, status, created_at, updated_at, owner_id, transaction_id, order_id)
VALUES
  (NEXTVAL('orders_seq'), 1, NOW(), NOW(), 152, (SELECT id FROM transactions WHERE transaction_id = 'txn_001'), 'ord_001');

-- Products for user 152
INSERT INTO products (id, price, created_at, updated_at, seller_id, description, product_id, title)
VALUES
  (NEXTVAL('products_seq'), 50.0, NOW(), NOW(), 152, 'Sample product description', 'prod_001', 'Sample Product');

-- Ratings for the product by user 152
INSERT INTO ratings (id, rating, created_at, updated_at, user_id, product_id, rating_id)
VALUES
  (NEXTVAL('ratings_seq'), 5, NOW(), NOW(), 152, (SELECT id FROM products WHERE product_id = 'prod_001'), 'rate_001');
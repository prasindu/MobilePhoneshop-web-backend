-- src/main/resources/db/migration/V1__Initial_schema.sql

-- Users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL CHECK (role IN ('MANAGER', 'CASHIER')),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE categories (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(50) UNIQUE NOT NULL,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          received_price DECIMAL(10,2) NOT NULL,
                          selling_price DECIMAL(10,2) NOT NULL,
                          stock INTEGER NOT NULL DEFAULT 0,
                          barcode VARCHAR(100) UNIQUE NOT NULL,
                          image_url TEXT,
                          category_id BIGINT REFERENCES categories(id),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sales table
CREATE TABLE sales (
                       id BIGSERIAL PRIMARY KEY,
                       invoice_id VARCHAR(50) UNIQUE NOT NULL,
                       sale_date DATE NOT NULL,
                       sale_time TIME NOT NULL,
                       total DECIMAL(10,2) NOT NULL,
                       profit DECIMAL(10,2) NOT NULL,
                       discount DECIMAL(10,2) DEFAULT 0,
                       discount_type VARCHAR(20) DEFAULT 'PERCENTAGE',
                       customer_name VARCHAR(100),
                       customer_phone VARCHAR(20),
                       customer_email VARCHAR(100),
                       customer_address TEXT,
                       notes TEXT,
                       cashier_id BIGINT REFERENCES users(id),
                       is_return BOOLEAN DEFAULT FALSE,
                       return_reason TEXT,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sale items table
CREATE TABLE sale_items (
                            id BIGSERIAL PRIMARY KEY,
                            sale_id BIGINT REFERENCES sales(id) ON DELETE CASCADE,
                            product_id BIGINT REFERENCES products(id),
                            product_name VARCHAR(255) NOT NULL,
                            quantity INTEGER NOT NULL,
                            unit_price DECIMAL(10,2) NOT NULL,
                            discount DECIMAL(10,2) DEFAULT 0,
                            discount_type VARCHAR(20) DEFAULT 'PERCENTAGE',
                            is_custom BOOLEAN DEFAULT FALSE
);

-- Insert default users
INSERT INTO users (username, password, name, role) VALUES
                                                       ('admin', '$2a$10$6d6n7J1YPV.dCOCJWZGJJu7L7hf1xh7HgD4OXKG8JmMM.YgH1v2iW', 'Admin User', 'MANAGER'),
                                                       ('cashier', '$2a$10$6d6n7J1YPV.dCOCJWZGJJu7L7hf1xh7HgD4OXKG8JmMM.YgH1v2iW', 'John Cashier', 'CASHIER');

-- Insert default categories
INSERT INTO categories (name) VALUES
                                  ('smartphones'), ('accessories'), ('laptops'), ('tablets'), ('wearables');

-- Insert sample products
INSERT INTO products (name, description, received_price, selling_price, stock, barcode, category_id, image_url) VALUES
                                                                                                                    ('iPhone 15 Pro', 'Latest iPhone with titanium build', 800.00, 999.00, 25, 'IPH15PRO001', 1, 'https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400&h=400&fit=crop'),
                                                                                                                    ('Samsung Galaxy S24', 'Premium Android flagship', 700.00, 899.00, 18, 'SGS24001', 1, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400&h=400&fit=crop'),
                                                                                                                    ('MacBook Air M3', '13-inch laptop with M3 chip', 1000.00, 1299.00, 12, 'MBA13M3001', 3, 'https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400&h=400&fit=crop'),
                                                                                                                    ('iPad Pro', '11-inch tablet with M4 chip', 600.00, 799.00, 8, 'IPADPRO11001', 4, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400&h=400&fit=crop'),
                                                                                                                    ('AirPods Pro', 'Wireless earbuds with noise cancellation', 150.00, 249.00, 30, 'AIRPODSPRO001', 2, 'https://images.unsplash.com/photo-1606220945770-b5b6c2c55bf1?w=400&h=400&fit=crop');
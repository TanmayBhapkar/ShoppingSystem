CREATE DATABASE IF NOT EXISTS online_shopping;
USE online_shopping;

CREATE TABLE IF NOT EXISTS products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

INSERT INTO products (name, price, stock)
SELECT 'Keyboard', 799.00, 20 WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Keyboard');
INSERT INTO products (name, price, stock)
SELECT 'Mouse', 399.00, 30 WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Mouse');
INSERT INTO products (name, price, stock)
SELECT 'Headphones', 1299.00, 15 WHERE NOT EXISTS (SELECT 1 FROM products WHERE name='Headphones');

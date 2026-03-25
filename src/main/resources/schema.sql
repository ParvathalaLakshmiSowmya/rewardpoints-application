DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
	customer_id INT AUTO_INCREMENT PRIMARY KEY, 
	customer_name VARCHAR(50), 
	phone_Number VARCHAR(10), 
	address VARCHAR(50)
);

CREATE TABLE transactions (
	transaction_id INT AUTO_INCREMENT PRIMARY KEY, 
	customer_id INT, 
	amount DOUBLE,
	transaction_date DATE,
	CONSTRAINT fk_customer 
		FOREIGN KEY (customer_id) 
		REFERENCES customers(customer_id)
);
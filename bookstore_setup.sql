-- Create the database
CREATE DATABASE IF NOT EXISTS BookstoreDB;

-- Switch to the new database
USE BookstoreDB;

-- Create the Books table
CREATE TABLE IF NOT EXISTS Books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL
);

-- Verify the table structure
DESCRIBE Books;
CREATE TABLE IF NOT EXISTS clients (
    account_number TEXT NOT NULL PRIMARY KEY,
    pin TEXT NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    gender TEXT NOT NULL,
    address TEXT NOT NULL,
    date_of_birth DATE NOT NULL,
    savings TEXT NOT NULL,
    attempts INT NOT NULL
);
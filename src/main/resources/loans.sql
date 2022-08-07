CREATE TABLE IF NOT EXISTS loans (
    loan_number INT NOT NULL,
    account_number TEXT NOT NULL,
    date_of_loan DATE NOT NULL,
    amount TEXT NOT NULL,
    pending TEXT NOT NULL,
    is_declined TEXT NOT NULL
);
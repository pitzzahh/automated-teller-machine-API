CREATE TABLE IF NOT EXISTS loans (
    loan_number TEXT NOT NULL,
    account_number TEXT NOT NULL PRIMARY KEY,
    date_of_loan DATE NOT NULL,
    amount TEXT NOT NULL,
    loan_count TEXT NOT NULL
);
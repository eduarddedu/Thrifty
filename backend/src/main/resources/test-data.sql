INSERT INTO Account (id, name, currency) values(1, 'Daily expenses', 'EUR');
INSERT INTO Category (id, name, description, account_id) values(1, 'Groceries',  'Groceries', 1);
INSERT INTO Category (id, name, description, account_id) values(2, 'Rent',  'Rent', 1);
INSERT INTO Label (id, name, account_id) values(1, 'Cosmetics', 1);
INSERT INTO Expense (id, createdOn, description, amount, account_id) values(1, '2020-01-01', 'Supermarket', 100.12, 1);
INSERT INTO Expense_Category (expense_id, category_id) values(1, 1);
INSERT INTO USER (id, username, password, account_id) values(1, 'johndoe@example.com', '{bcrypt}$2a$10$INZNo1XGKNuwgLEsUkCBXeiQmcVp9QQqU6wU6plQLw8mJ/xdKK1my', 1);
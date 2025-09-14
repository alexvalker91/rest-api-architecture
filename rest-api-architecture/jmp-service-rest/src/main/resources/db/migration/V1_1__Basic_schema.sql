CREATE TABLE IF NOT EXISTS user_table (
    id SERIAL PRIMARY KEY,
    user_name VARCHAR(255),
    user_surname VARCHAR(255),
    user_birthday DATE
);

CREATE TABLE IF NOT EXISTS subscription_table (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    subscription_start_date DATE
);
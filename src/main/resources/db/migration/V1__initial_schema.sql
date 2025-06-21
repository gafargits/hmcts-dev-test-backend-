
CREATE TABLE task (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    description TEXT,
                    status VARCHAR(20) NOT NULL,
                    due_date TIMESTAMP,
                    date_created TIMESTAMP
);

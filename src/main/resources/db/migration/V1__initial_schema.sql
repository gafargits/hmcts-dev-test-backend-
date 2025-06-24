CREATE TABLE caseworker (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL
);

INSERT INTO caseworker (name) VALUES
                                ('Alice Johnson'),
                                ('Bob Smith'),
                                ('Charlie Davis');


CREATE TABLE task (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(255) NOT NULL,
                    description TEXT,
                    case_worker VARCHAR(255),
                    status VARCHAR(20) NOT NULL,
                    due_date TIMESTAMP,
                    date_created TIMESTAMP,
                    caseworker_id INTEGER,
                    CONSTRAINT fk_unique_task_title_caseworker UNIQUE (title, caseworker_id), FOREIGN KEY (caseworker_id) REFERENCES caseworker(id) ON DELETE SET NULL
);

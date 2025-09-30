CREATE TABLE survey (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        year INT NOT NULL,
                        domain VARCHAR(32) NOT NULL,
                        active BOOLEAN DEFAULT TRUE
);

CREATE TABLE survey_question (
                                 id BIGSERIAL PRIMARY KEY,
                                 text VARCHAR(500) NOT NULL,
                                 type VARCHAR(32) NOT NULL,
                                 survey_id BIGINT REFERENCES survey(id) ON DELETE CASCADE
);

CREATE TABLE survey_response (
                                 id BIGSERIAL PRIMARY KEY,
                                 user_email VARCHAR(255) NOT NULL,
                                 region VARCHAR(32) NOT NULL,
                                 survey_id BIGINT REFERENCES survey(id) ON DELETE CASCADE,
                                 question_id BIGINT REFERENCES survey_question(id) ON DELETE CASCADE,
                                 answer VARCHAR(255)
);
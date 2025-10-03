DROP TABLE IF EXISTS vozacka_dozvola_categories;

CREATE TABLE vozacka_dozvola_categories (
                                            vozacka_dozvola_id BIGINT NOT NULL,
                                            categories VARCHAR(50) NOT NULL,

                                            CONSTRAINT fk_license_categories FOREIGN KEY (vozacka_dozvola_id) REFERENCES driving_licenses(id) ON DELETE CASCADE
)
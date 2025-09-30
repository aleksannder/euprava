-- Population
CREATE TABLE population_stat (
                                 id BIGSERIAL PRIMARY KEY,
                                 region VARCHAR(32) NOT NULL,
                                 year INT NOT NULL,
                                 population BIGINT NOT NULL,
                                 average_age DOUBLE PRECISION,
                                 birth_rate DOUBLE PRECISION,
                                 mortality_rate DOUBLE PRECISION
);

-- GDP
CREATE TABLE gdp_stat (
                          id BIGSERIAL PRIMARY KEY,
                          region VARCHAR(32) NOT NULL,
                          year INT NOT NULL,
                          gdp_billion DOUBLE PRECISION NOT NULL,
                          growth_percent DOUBLE PRECISION NOT NULL,
                          cpi_percent DOUBLE PRECISION
);

-- Wages
CREATE TABLE wage_stat (
                           id BIGSERIAL PRIMARY KEY,
                           region VARCHAR(32) NOT NULL,
                           year INT NOT NULL,
                           average_wage DOUBLE PRECISION NOT NULL,
                           growth_percent DOUBLE PRECISION
);

-- Traffic
CREATE TABLE traffic_stat (
                              id BIGSERIAL PRIMARY KEY,
                              region VARCHAR(32) NOT NULL,
                              year INT NOT NULL,
                              registered_vehicles BIGINT,
                              traffic_accidents BIGINT,
                              fatalities BIGINT
);



-- =======================
-- POPULATION
-- =======================
INSERT INTO population_stat (region, year, population, average_age, birth_rate, mortality_rate) VALUES
-- Beograd
('RS11_BELGRADE', 2020, 1360000, 41.7, 9.5, 11.2),
('RS11_BELGRADE', 2021, 1365000, 41.9, 9.4, 11.3),
('RS11_BELGRADE', 2022, 1370000, 42.0, 9.3, 11.4),
('RS11_BELGRADE', 2023, 1375000, 42.1, 9.2, 11.5),
('RS11_BELGRADE', 2024, 1380000, 42.3, 9.1, 11.6),
('RS11_BELGRADE', 2025, 1382000, 42.4, 9.0, 11.7),

-- Vojvodina
('RS12_VOJVODINA', 2020, 1870000, 43.0, 8.9, 12.7),
('RS12_VOJVODINA', 2021, 1865000, 43.2, 8.8, 12.8),
('RS12_VOJVODINA', 2022, 1860000, 43.3, 8.7, 12.9),
('RS12_VOJVODINA', 2023, 1850000, 43.5, 8.7, 13.0),
('RS12_VOJVODINA', 2024, 1845000, 43.6, 8.6, 13.1),
('RS12_VOJVODINA', 2025, 1840000, 43.7, 8.5, 13.2),

-- Zapadna Srbija i Šumadija
('RS21_WEST_SUMADIJA', 2020, 2020000, 43.8, 8.8, 13.5),
('RS21_WEST_SUMADIJA', 2021, 2010000, 44.0, 8.7, 13.6),
('RS21_WEST_SUMADIJA', 2022, 2005000, 44.1, 8.6, 13.7),
('RS21_WEST_SUMADIJA', 2023, 2000000, 44.2, 8.5, 13.8),
('RS21_WEST_SUMADIJA', 2024, 1995000, 44.4, 8.5, 13.9),
('RS21_WEST_SUMADIJA', 2025, 1990000, 44.5, 8.4, 14.0),

-- Južna i Istočna Srbija
('RS22_SOUTH_EAST', 2020, 1680000, 44.5, 8.3, 14.3),
('RS22_SOUTH_EAST', 2021, 1675000, 44.6, 8.2, 14.4),
('RS22_SOUTH_EAST', 2022, 1665000, 44.7, 8.1, 14.5),
('RS22_SOUTH_EAST', 2023, 1650000, 44.9, 8.0, 14.5),
('RS22_SOUTH_EAST', 2024, 1640000, 45.0, 7.9, 14.6),
('RS22_SOUTH_EAST', 2025, 1630000, 45.1, 7.8, 14.7);

-- =======================
-- GDP
-- =======================
INSERT INTO gdp_stat (region, year, gdp_billion, growth_percent, cpi_percent) VALUES
                                                                                  ('RS11_BELGRADE', 2020, 20.5, 2.1, 1.9),
                                                                                  ('RS11_BELGRADE', 2021, 21.7, 2.5, 2.2),
                                                                                  ('RS11_BELGRADE', 2022, 23.1, 2.8, 7.0),
                                                                                  ('RS11_BELGRADE', 2023, 24.5, 3.2, 8.5),
                                                                                  ('RS11_BELGRADE', 2024, 25.8, 3.5, 7.8),
                                                                                  ('RS11_BELGRADE', 2025, 27.0, 3.7, 6.9),

                                                                                  ('RS12_VOJVODINA', 2020, 16.0, 1.9, 1.7),
                                                                                  ('RS12_VOJVODINA', 2021, 16.8, 2.2, 2.1),
                                                                                  ('RS12_VOJVODINA', 2022, 17.7, 2.5, 7.3),
                                                                                  ('RS12_VOJVODINA', 2023, 18.7, 2.8, 8.7),
                                                                                  ('RS12_VOJVODINA', 2024, 19.5, 3.0, 7.9),
                                                                                  ('RS12_VOJVODINA', 2025, 20.3, 3.3, 7.0),

                                                                                  ('RS21_WEST_SUMADIJA', 2020, 12.6, 1.5, 1.8),
                                                                                  ('RS21_WEST_SUMADIJA', 2021, 13.2, 1.8, 2.0),
                                                                                  ('RS21_WEST_SUMADIJA', 2022, 14.2, 2.2, 7.1),
                                                                                  ('RS21_WEST_SUMADIJA', 2023, 15.2, 2.5, 9.0),
                                                                                  ('RS21_WEST_SUMADIJA', 2024, 16.1, 2.7, 8.1),
                                                                                  ('RS21_WEST_SUMADIJA', 2025, 17.0, 2.9, 7.2),

                                                                                  ('RS22_SOUTH_EAST', 2020, 10.1, 1.2, 1.6),
                                                                                  ('RS22_SOUTH_EAST', 2021, 10.7, 1.5, 2.0),
                                                                                  ('RS22_SOUTH_EAST', 2022, 11.5, 1.9, 7.5),
                                                                                  ('RS22_SOUTH_EAST', 2023, 12.3, 2.0, 9.3),
                                                                                  ('RS22_SOUTH_EAST', 2024, 13.0, 2.3, 8.5),
                                                                                  ('RS22_SOUTH_EAST', 2025, 13.7, 2.5, 7.5);

-- =======================
-- WAGES
-- =======================
INSERT INTO wage_stat (region, year, average_wage, growth_percent) VALUES
                                                                       ('RS11_BELGRADE', 2020, 85000, 8.5),
                                                                       ('RS11_BELGRADE', 2021, 89000, 9.1),
                                                                       ('RS11_BELGRADE', 2022, 93000, 10.0),
                                                                       ('RS11_BELGRADE', 2023, 98000, 12.5),
                                                                       ('RS11_BELGRADE', 2024, 103000, 11.0),
                                                                       ('RS11_BELGRADE', 2025, 109000, 12.0),

                                                                       ('RS12_VOJVODINA', 2020, 74000, 8.0),
                                                                       ('RS12_VOJVODINA', 2021, 77000, 8.6),
                                                                       ('RS12_VOJVODINA', 2022, 81000, 9.0),
                                                                       ('RS12_VOJVODINA', 2023, 85000, 11.8),
                                                                       ('RS12_VOJVODINA', 2024, 89000, 10.5),
                                                                       ('RS12_VOJVODINA', 2025, 93000, 11.0),

                                                                       ('RS21_WEST_SUMADIJA', 2020, 69000, 7.5),
                                                                       ('RS21_WEST_SUMADIJA', 2021, 71000, 7.8),
                                                                       ('RS21_WEST_SUMADIJA', 2022, 74000, 8.5),
                                                                       ('RS21_WEST_SUMADIJA', 2023, 78000, 10.9),
                                                                       ('RS21_WEST_SUMADIJA', 2024, 81000, 10.2),
                                                                       ('RS21_WEST_SUMADIJA', 2025, 85000, 10.8),

                                                                       ('RS22_SOUTH_EAST', 2020, 64000, 7.0),
                                                                       ('RS22_SOUTH_EAST', 2021, 66000, 7.4),
                                                                       ('RS22_SOUTH_EAST', 2022, 69000, 8.0),
                                                                       ('RS22_SOUTH_EAST', 2023, 72000, 10.5),
                                                                       ('RS22_SOUTH_EAST', 2024, 75000, 9.8),
                                                                       ('RS22_SOUTH_EAST', 2025, 79000, 10.5);

-- =======================
-- TRAFFIC
-- =======================
INSERT INTO traffic_stat (region, year, registered_vehicles, traffic_accidents, fatalities) VALUES
                                                                                                ('RS11_BELGRADE', 2020, 580000, 11000, 95),
                                                                                                ('RS11_BELGRADE', 2021, 590000, 11500, 92),
                                                                                                ('RS11_BELGRADE', 2022, 605000, 12000, 88),
                                                                                                ('RS11_BELGRADE', 2023, 620000, 12500, 85),
                                                                                                ('RS11_BELGRADE', 2024, 635000, 12800, 82),
                                                                                                ('RS11_BELGRADE', 2025, 650000, 13000, 80),

                                                                                                ('RS12_VOJVODINA', 2020, 510000, 9700, 125),
                                                                                                ('RS12_VOJVODINA', 2021, 520000, 9900, 120),
                                                                                                ('RS12_VOJVODINA', 2022, 530000, 10100, 115),
                                                                                                ('RS12_VOJVODINA', 2023, 540000, 10200, 110),
                                                                                                ('RS12_VOJVODINA', 2024, 555000, 10400, 108),
                                                                                                ('RS12_VOJVODINA', 2025, 570000, 10600, 105),

                                                                                                ('RS21_WEST_SUMADIJA', 2020, 450000, 8800, 145),
                                                                                                ('RS21_WEST_SUMADIJA', 2021, 460000, 9000, 142),
                                                                                                ('RS21_WEST_SUMADIJA', 2022, 470000, 9200, 138),
                                                                                                ('RS21_WEST_SUMADIJA', 2023, 480000, 9500, 130),
                                                                                                ('RS21_WEST_SUMADIJA', 2024, 490000, 9700, 125),
                                                                                                ('RS21_WEST_SUMADIJA', 2025, 500000, 9900, 120),

                                                                                                ('RS22_SOUTH_EAST', 2020, 380000, 8200, 170),
                                                                                                ('RS22_SOUTH_EAST', 2021, 385000, 8300, 165),
                                                                                                ('RS22_SOUTH_EAST', 2022, 390000, 8500, 160),
                                                                                                ('RS22_SOUTH_EAST', 2023, 400000, 8700, 150),
                                                                                                ('RS22_SOUTH_EAST', 2024, 410000, 8900, 145),
                                                                                                ('RS22_SOUTH_EAST', 2025, 420000, 9100, 140);


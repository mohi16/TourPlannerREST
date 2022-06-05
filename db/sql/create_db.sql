CREATE TABLE tours (
  t_id SERIAL,
  t_name VARCHAR(50) NOT NULL UNIQUE,
  t_description VARCHAR(255) NOT NULL,
  t_from VARCHAR(50) NOT NULL,
  t_to VARCHAR(50) NOT NULL,
  t_transport_type VARCHAR(50) NOT NULL,
  t_distance REAL NOT NULL,
  t_est_time BIGINT NOT NULL,
  t_route_info VARCHAR(50) NOT NULL,
  t_map VARCHAR(50) NOT NULL,
  PRIMARY KEY (t_id)
);

CREATE TABLE logs (
  l_id SERIAL,
  l_date TIMESTAMP NOT NULL,
  l_comment VARCHAR(255) NOT NULL,
  l_difficulty INT CHECK(0 < l_difficulty AND l_difficulty < 6) NOT NULL,
  l_total_time BIGINT NOT NULL,
  l_rating INT CHECK(0 < l_rating AND l_rating < 6) NOT NULL,
  t_id INT REFERENCES tours (t_id),
  PRIMARY KEY (l_id)
);

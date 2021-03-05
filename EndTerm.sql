CREATE TABLE airplane
(
	airplane_id serial NOT NULL,
	airplane_model varchar(100) NOT NULL,
	business_sits int,
	economy_sits int,
	crew_sits int NOT NULL,
	PRIMARY KEY(airplane_id)
)

CREATE TABLE flight
(
	flight_id serial NOT NULL, 
	airplane_id int NOT NULL,
	code VARCHAR(100) NOT NULL,
	depart_from VARCHAR(100) NOT NULL,
	depart_to VARCHAR(100) NOT NULL,
	company_name VARCHAR(100) NOT NULL,
	date_from timestamp NOT NULL,
	date_to timestamp,
	PRIMARY KEY(flight_id),
	FOREIGN KEY(airplane_id) REFERENCES airplane(airplane_id)
)

CREATE TABLE ticket
(
	ticket_id serial NOT NULL,
	flight_id int NOT NULL,
	price int,
	class_vip boolean,
	status boolean,
	PRIMARY KEY(ticket_id),
	FOREIGN KEY(flight_id) REFERENCES flight(flight_id)
)

CREATE TABLE card
(
	card_id serial NOT NULL,
	card_number varchar(50) NOT NULL,
	security_code int NOT NULL,
	PRIMARY KEY(card_id)
)

CREATE TABLE passenger
(
	passenger_id serial NOT NULL,
	ticket_id int NOT NULL,
	card_id int NOT NULL,
	email VARCHAR(100),
	phone_number VARCHAR(50) NOT NULL,
	passport_number VARCHAR(50) NOT NULL,
	PRIMARY KEY(passenger_id),
	FOREIGN KEY(ticket_id) REFERENCES ticket(ticket_id),
	FOREIGN KEY(card_id) REFERENCES card(card_id)
)

insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus A321', 35, 61, 6);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Boieng 767-300ER', 17, 88, 9);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus 320NEO', 36, 114, 6);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Embraer Е190-Е2', 25, 94, 8);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus A322', 22, 160, 5);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus A320', 50, 126, 8);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Boieng 760-300ER', 16, 51, 10);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus 321NEO', 48, 129, 8);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus 321NEO LR', 50, 168, 7);
insert into airplane (airplane_model, business_sits, economy_sits, crew_sits) values ('Airbus 320', 45, 59, 7);


insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (1, 'KC704', 'Los-Angeles', 'New-York', 'SCAT', '2020-06-22 19:10:25', '2020-06-13 20:10:25');
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (2, 'KC814', 'Paris', 'Vienna', 'FlyArystan', '2021-01-02 20:10:25', null);
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (3, 'KC306', 'Nur-Sultan', 'Almaty', 'SCAT', '2020-06-29 14:00:00', null);
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (4, 'KC434', 'Aqtau', 'Nur-Sultan', 'Air Astana', '2020-07-24 17:15:00', null);
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (5, 'KC029', 'Moscow', 'Istambul', 'Air Astana', '2020-06-13 20:10:25', '2020-06-20 22:30:00');
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (6, 'KC579', 'Nur-Sultan', 'Dubai OAE', 'FlyArystan', '2021-01-20 21:00:00', '2020-02-01 19:10:00');
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (7, 'KC363', 'Ankara', 'Tbilisi', 'Air Astana', '2020-06-13 20:10:25', '2020-06-17 20:00:00');
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (8, 'KC742', 'Leipzig', 'London', 'SCAT', '2020-06-13 18:35:00', '2020-07-13 10:00:00');
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (9, 'KC819', 'Bern', 'Nur-Sultan', 'FlyArystan', '2020-06-13 04:00:00', '2020-06-25 18:00:00');
insert into flight(airplane_id, code, depart_from, depart_to, company_name, date_from, date_to) values (10, 'KC533', 'Almaty', 'Bucharest', 'SCAT', '2020-06-13 20:00:25', null);


insert into ticket (flight_id, price, class_vip, status) values (1, 17465, false, false);
insert into ticket (flight_id, price, class_vip, status) values (2, 36117, true, false);
insert into ticket (flight_id, price, class_vip, status) values (3, 14503, false, false);
insert into ticket (flight_id, price, class_vip, status) values (4, 40623, true, false);
insert into ticket (flight_id, price, class_vip, status) values (5, 44171, true, false);
insert into ticket (flight_id, price, class_vip, status) values (6, 42182, true, false);
insert into ticket (flight_id, price, class_vip, status) values (7, 16326, false, false);
insert into ticket (flight_id, price, class_vip, status) values (8, 10542, false, false);
insert into ticket (flight_id, price, class_vip, status) values (9, 42520, true, false);
insert into ticket (flight_id, price, class_vip, status) values (10, 20154, false, false);
insert into ticket (flight_id, price, class_vip, status) values (1, 40623, true, false);
insert into ticket (flight_id, price, class_vip, status) values (2, 16117, false, false);
insert into ticket (flight_id, price, class_vip, status) values (3, 40600, true, false);
insert into ticket (flight_id, price, class_vip, status) values (4, 20623, false, false);
insert into ticket (flight_id, price, class_vip, status) values (5, 14171, false, false);
insert into ticket (flight_id, price, class_vip, status) values (6, 22182, false, false);
insert into ticket (flight_id, price, class_vip, status) values (7, 40623, true, false);
insert into ticket (flight_id, price, class_vip, status) values (8, 40623, true, false);
insert into ticket (flight_id, price, class_vip, status) values (9, 10456, false, false);
insert into ticket (flight_id, price, class_vip, status) values (10, 42623, true, false);


insert into card (card_number, security_code) values ('4048313094488791', '748');
insert into card (card_number, security_code) values ('4556785218848204', '495');
insert into card (card_number, security_code) values ('2221005041170828', '175');
insert into card (card_number, security_code) values ('5176982725309336', '936');
insert into card (card_number, security_code) values ('2720992924844254', '136');
insert into card (card_number, security_code) values ('5260831925368930', '362');
insert into card (card_number, security_code) values ('5127116356142670', '590');
insert into card (card_number, security_code) values ('5212391625516121', '827');
insert into card (card_number, security_code) values ('5428501586536823', '699');
insert into card (card_number, security_code) values ('2582753279270830', '937');


ALTER TABLE passenger
ADD COLUMN first_name VARCHAR(100),
ADD COLUMN second_name VARCHAR(100),
ADD COLUMN age int,
ADD COLUMN gender VARCHAR(100);








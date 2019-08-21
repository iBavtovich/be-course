CREATE TABLE DOG (
    ID number(22) auto_increment primary key,
    NAME varchar(100) not null,
    BIRTH_DATE timestamp,
    HEIGHT number(3),
    WEIGHT number(3)
);

INSERT INTO DOG(NAME, BIRTH_DATE, HEIGHT, WEIGHT) values ('ABBA', NOW(), 1, 1);
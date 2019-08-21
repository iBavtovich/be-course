CREATE TABLE DOG (
    ID serial primary key,
    NAME varchar(100) not null,
    BIRTH_DATE timestamp,
    HEIGHT integer,
    WEIGHT integer
);

INSERT INTO DOG(NAME, BIRTH_DATE, HEIGHT, WEIGHT) values ('ABBA', NOW(), 1, 1);
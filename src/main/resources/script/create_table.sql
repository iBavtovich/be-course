CREATE TABLE DOG (
    'ID' number(22) identity primary key,
    'NAME' varchar(100) not null,
    'BIRTH_DATE' timestamp,
    'HEIGHT' number(3),
    'WEIGHT' number(3)
);
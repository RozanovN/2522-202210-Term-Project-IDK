-- Table with user id, name and password for log in

CREATE TABLE IF NOT EXISTS AdventureGamers (
UserID int NOT NULL,
UserName varchar(255),
UserPassword varchar(255),
PRIMARY KEY (UserID)
);

-- Table with gamer achievements

CREATE TABLE achievements (
Id int NOT NULL,
HighScore int,
Kills int,
NumberOfPlays int,
PRIMARY KEY(`Id`),
FOREIGN KEY (`Id`) REFERENCES `AdventureGamers`(`UserID`));


-- Example of current gamer 

INSERT INTO adventuregamers (UserID, UserName, UserPassword) 
VALUES (1, "Chris", "gamer");

-- Example of current achievements

INSERT INTO achievements (Id, Highscore, Kills, NumberOfPlays)
VALUES (1, 200, 4, 12);

SELECT * FROM adventuregamers;
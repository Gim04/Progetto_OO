CREATE TABLE Classifica
(
    ID int,
    PRIMARY KEY (ID)
);

CREATE TABLE Sede
(
    ID int,
    citta varchar(30),
    via varchar(50),
    codicePostale int,
    PRIMARY KEY (ID)
);

CREATE TABLE Organizzatore
(
    ID int,
    nome varchar(30),
    cognome varchar(30),
    email varchar(150),
    password varchar(16),
    PRIMARY KEY (ID)
);

CREATE TABLE Partecipante
(
    ID int,
    nome varchar(30),
    cognome varchar(30),
    email varchar(150),
    password varchar(16),
    PRIMARY KEY (ID)
);

CREATE TABLE Giudice
(
    ID int,
    nome varchar(30),
    cognome varchar(30),
    email varchar(150),
    password varchar(16),
    PRIMARY KEY (ID)
);

CREATE TABLE Team
(
    ID int,
    nome varchar(30),
    voto smallint,
    PRIMARY KEY (ID)
);

CREATE TABLE Documento
(
    ID int,
    team int,
    commento text,
    contenuto text,
    PRIMARY KEY (ID),
    FOREIGN KEY (team) REFERENCES Team(ID)
);

CREATE TABLE Hackathon
(
    ID int,
    sede int,
    dataInizio date,
    maxIscritti int,
    registrazioniAperte smallint,
    dataFine date,
    dimensioneTeam int,
    titolo varchar(50),
    descrizioneProblema varchar(500),
    classifica int,
    organizzatore int,
    PRIMARY KEY (ID),
    FOREIGN KEY (classifica) REFERENCES Classifica(ID),
    FOREIGN KEY (sede) REFERENCES Sede(ID),
    FOREIGN KEY (organizzatore) REFERENCES Organizzatore(ID)
);

CREATE TABLE HACKATHON_PARTECIPANTE
(
    hackathon INT,
    partecipante INT,
    PRIMARY KEY (hackathon, partecipante),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID),
    FOREIGN KEY (partecipante) REFERENCES Partecipante(ID)
);

CREATE TABLE HACKATHON_GIUDICE
(
    hackathon INT,
    giudice INT,
    PRIMARY KEY (hackathon, giudice),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID),
    FOREIGN KEY (giudice) REFERENCES Giudice(ID)
);

CREATE TABLE HACKATHON_ORGANIZZATORE
(
    hackathon INT,
    organizzatore INT,
    PRIMARY KEY (hackathon, organizzatore),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID),
    FOREIGN KEY (organizzatore) REFERENCES Organizzatore(ID)
);

CREATE TABLE TEAM_PARTECIPANTE
(
    team INT,
    partecipante INT,
    PRIMARY KEY (team, partecipante),
    FOREIGN KEY (team) REFERENCES Team(ID),
    FOREIGN KEY (partecipante) REFERENCES Partecipante(ID)
);

CREATE TABLE TEAM_HACKATHON
(
    team INT,
    hackathon INT,
    PRIMARY KEY (team, hackathon),
    FOREIGN KEY (team) REFERENCES Team(ID),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID)
);


CREATE TABLE GIUDICE_TEAM
(
    giudice INT,
    team INT,
    voto INT,
    PRIMARY KEY (giudice, team),
    FOREIGN KEY (giudice) REFERENCES Giudice(ID),
    FOREIGN KEY (team) REFERENCES Team(ID)
);

-----
-- Partecipanti
INSERT INTO Partecipante (ID, nome, cognome, email, password) VALUES
                                                                  (1, 'Alice', 'Rossi', 'alice.rossi@example.com', '1234'),
                                                                  (2, 'Marco', 'Bianchi', 'marco.bianchi@example.com', 'Sicura123!'),
                                                                  (3, 'Luca', 'Verdi', 'luca.verdi@example.com', 'P@ssword2024'),
                                                                  (4, 'Giulia', 'Neri', 'giulia.neri@example.com', 'Login!2025');
-- Organizzatore
INSERT INTO Organizzatore (ID, nome, cognome, email, password) VALUES
    (5, 'Giulio', 'Dardano', 'giulio.dardano@example.com', '1somorfismo!');

-- Giudice
INSERT INTO Giudice (ID, nome, cognome, email, password) VALUES
    (6, 'Antonio', 'Poco', 'antonio.pocomento@example.com', 'ioHoFortun4!');

-- Sede
INSERT INTO Sede (ID, citta, via, codicePostale) VALUES
    (1, 'Politecnico di Milano', 'Piazza Leonardo da Vinci, 32', 80001);

-- Classifica
INSERT INTO Classifica (ID) VALUES
    (1);

-- Hackathon
INSERT INTO Hackathon (
    ID, sede, dataInizio, maxIscritti, registrazioniAperte, dataFine,
    dimensioneTeam, titolo, descrizioneProblema, classifica, organizzatore
) VALUES
    (1, 1, '2025-06-15', 100, 0, '2025-06-17', 4, 'HackTheFuture 2025', 'Bug Bounty', 1, 5);

-- Partecipanti iscritti all'Hackathon
INSERT INTO HACKATHON_PARTECIPANTE (hackathon, partecipante) VALUES
                                                                 (1, 1), (1, 2), (1, 3), (1, 4);

-- Giudice assegnato all'Hackathon
INSERT INTO HACKATHON_GIUDICE (hackathon, giudice) VALUES
    (1, 6);

-- Organizzatore dell'Hackathon
INSERT INTO HACKATHON_ORGANIZZATORE (hackathon, organizzatore) VALUES
    (1, 5);

-- Team
INSERT INTO Team (ID, nome, voto) VALUES
                                      (1, 'Unina', 0),
                                      (2, 'Eureka', 0);

-- Team iscritti all'Hackathon
INSERT INTO TEAM_HACKATHON (team, hackathon) VALUES
                                                 (1, 1),
                                                 (2, 1);

-- Partecipanti nei Team
INSERT INTO TEAM_PARTECIPANTE (team, partecipante) VALUES
                                                       (1, 1), -- Alice
                                                       (1, 2), -- Marco
                                                       (2, 3), -- Luca
                                                       (2, 4); -- Giulia

-- Documento caricato dal team Unina
INSERT INTO Documento (ID, team, commento, contenuto) VALUES
    (1, 1, 'Insufficiente', 'Documento');

-- Voti dei giudici ai team
INSERT INTO GIUDICE_TEAM (giudice, team, voto) VALUES
                                                   (6, 1, 0),
                                                   (6, 2, 300);

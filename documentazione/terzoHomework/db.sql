CREATE TABLE Classifica
(
    ID SERIAL PRIMARY KEY
);

CREATE TABLE Sede
(
    ID SERIAL PRIMARY KEY,
    citta varchar(30),
    via varchar(50),
    codicePostale int
);

CREATE TABLE Organizzatore
(
    ID SERIAL PRIMARY KEY,
    nome varchar(30),
    cognome varchar(30),
    email varchar(150),
    password varchar(16),
    CONSTRAINT unique_EMAIL_ORGANIZZATORE UNIQUE(email)
);

CREATE TABLE Partecipante
(
    ID SERIAL PRIMARY KEY,
    nome varchar(30),
    cognome varchar(30),
    email varchar(150),
    password varchar(16),
    CONSTRAINT unique_EMAIL_PARTECIPANTE UNIQUE(email)
);

CREATE TABLE Giudice
(
    ID SERIAL PRIMARY KEY,
    nome varchar(30),
    cognome varchar(30),
    email varchar(150),
    password varchar(16),
    CONSTRAINT unique_EMAIL_GIUDICE UNIQUE(email)
);

CREATE TABLE Team
(
    ID SERIAL PRIMARY KEY,
    nome varchar(30),
    voto smallint,

);

CREATE TABLE Documento
(
    ID SERIAL PRIMARY KEY,
    team int,
    commento text,
    contenuto text,
    FOREIGN KEY (team) REFERENCES Team(ID)
);

CREATE TABLE Hackathon
(
    ID SERIAL PRIMARY KEY,
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
INSERT INTO Partecipante (nome, cognome, email, password) VALUES
                                                                  ('Alice', 'Rossi', 'alice.rossi@example.com', '1234'),
                                                                  ('Marco', 'Bianchi', 'marco.bianchi@example.com', 'Sicura123!'),
                                                                  ('Luca', 'Verdi', 'luca.verdi@example.com', 'P@ssword2024'),
                                                                  ('Giulia', 'Neri', 'giulia.neri@example.com', 'Login!2025');
-- Organizzatore
INSERT INTO Organizzatore (nome, cognome, email, password) VALUES
    ('Giulio', 'Dardano', 'giulio.dardano@example.com', '1somorfismo!');

-- Giudice
INSERT INTO Giudice (nome, cognome, email, password) VALUES
    ('Antonio', 'Poco', 'antonio.pocomento@example.com', 'ioHoFortun4!');

-- Sede
INSERT INTO Sede (citta, via, codicePostale) VALUES
    ('Politecnico di Milano', 'Piazza Leonardo da Vinci, 32', 80001);

-- Classifica
INSERT INTO Classifica (ID) VALUES
    (1);

-- Hackathon
INSERT INTO Hackathon (
    sede, dataInizio, maxIscritti, registrazioniAperte, dataFine,
    dimensioneTeam, titolo, descrizioneProblema, classifica, organizzatore
) VALUES
    (1, '2025-06-15', 100, 0, '2025-06-17', 4, 'HackTheFuture 2025', 'Bug Bounty', 1, 1);

-- Partecipanti iscritti all'Hackathon
INSERT INTO HACKATHON_PARTECIPANTE (hackathon, partecipante) VALUES
                                                                 (1, 1), (1, 2), (1, 3), (1, 4);

-- Giudice assegnato all'Hackathon
INSERT INTO HACKATHON_GIUDICE (hackathon, giudice) VALUES
    (1,1);

-- Team
INSERT INTO Team (nome, voto) VALUES
                                      ('Unina', 0),
                                      ('Eureka', 0);

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
INSERT INTO Documento (team, commento, contenuto) VALUES
    (1, 'Insufficiente', 'Documento');

-- Voti dei giudici ai team
INSERT INTO GIUDICE_TEAM (giudice, team, voto) VALUES
                                                   (1, 1, 0),
                                                   (1, 2, 300);

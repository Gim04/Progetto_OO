--- DEFINIZIONE TABELLE ---

CREATE TABLE Sede
(
    ID SERIAL PRIMARY KEY,
    citta varchar(30),
    via varchar(50),
    codicePostale int
);

CREATE TABLE Organizzatore
(
    nome varchar(30) NOT NULL,
    cognome varchar(30) NOT NULL,
    email varchar(150) PRIMARY KEY,
    password varchar(16) NOT NULL
);

CREATE TABLE Partecipante
(
    nome varchar(30) NOT NULL,
    cognome varchar(30) NOT NULL,
    email varchar(150) PRIMARY KEY,
    password varchar(16) NOT NULL
);

CREATE TABLE Giudice
(
    nome varchar(30) NOT NULL,
    cognome varchar(30) NOT NULL,
    email varchar(150) PRIMARY KEY,
    password varchar(16) NOT NULL
);

CREATE TABLE Team
(
    ID SERIAL PRIMARY KEY,
    nome varchar(30) NOT NULL,
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
    organizzatore varchar(150),
    FOREIGN KEY (sede) REFERENCES Sede(ID),
    FOREIGN KEY (organizzatore) REFERENCES Organizzatore(email)
);

--- TABELLE INTERMEDIE ---

CREATE TABLE HACKATHON_PARTECIPANTE
(
    hackathon INT,
    partecipante varchar(150),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID),
    FOREIGN KEY (partecipante) REFERENCES Partecipante(email),
    CONSTRAINT unique_HACKATHON_PARTECIPANTE UNIQUE(hackathon, partecipante)
);

CREATE TABLE HACKATHON_GIUDICE
(
    hackathon INT,
    giudice varchar(150),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID),
    FOREIGN KEY (giudice) REFERENCES Giudice(email),
    CONSTRAINT unique_HACKATHON_GIUDICE UNIQUE(hackathon, giudice)
);

CREATE TABLE TEAM_PARTECIPANTE
(
    team INT,
    partecipante varchar(150),
    FOREIGN KEY (team) REFERENCES Team(ID),
    FOREIGN KEY (partecipante) REFERENCES Partecipante(email),
    CONSTRAINT unique_TEAM_PARTECIPANTE UNIQUE(team, partecipante)
);

CREATE TABLE TEAM_HACKATHON
(
    team INT,
    hackathon INT,
    FOREIGN KEY (team) REFERENCES Team(ID),
    FOREIGN KEY (hackathon) REFERENCES Hackathon(ID),
    CONSTRAINT unique_TEAM_HACKATHON UNIQUE(team, hackathon)
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

INSERT INTO Documento (team, contenuto) VALUES
    (1, 'Documento 2');


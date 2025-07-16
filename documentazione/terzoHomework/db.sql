--- DOMINI ---
CREATE DOMAIN Voto AS SMALLINT
CHECK (VALUE BETWEEN 0 AND 10);


CREATE DOMAIN Email AS VARCHAR(150)
CHECK (VALUE ~ '^[A-Za-z0-9._%&+-]+@[A-Za-z0-9.-]+.[A-Za-z]$');


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
    email Email PRIMARY KEY,
    password varchar(16) NOT NULL
);

CREATE TABLE Partecipante
(
    nome varchar(30) NOT NULL,
    cognome varchar(30) NOT NULL,
    email Email PRIMARY KEY,
    password varchar(16) NOT NULL
);

CREATE TABLE Giudice
(
    nome varchar(30) NOT NULL,
    cognome varchar(30) NOT NULL,
    email Email PRIMARY KEY,
    password varchar(16) NOT NULL
);

CREATE TABLE Team
(
    ID SERIAL PRIMARY KEY,
    nome varchar(30) NOT NULL,
    voto Voto
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

--- TRIGGERS ---

-- 1. Non posso esistere nello stesso Hackathon piu' team con lo stesso nome ##########
CREATE OR REPLACE FUNCTION unicoNomeTeamHackathonF()
RETURNS TRIGGER AS $$
DECLARE
    nomeHackathon varchar(50);
    nomeTeam varchar(30);
BEGIN

SELECT nome INTO nomeTeam FROM Team WHERE Team.id = NEW.team;

IF (NOT EXISTS(
            SELECT * FROM TEAM_HACKATHON JOIN TEAM ON TEAM_HACKATHON.team = team.id WHERE nomeTeam = team.nome
                            AND TEAM_HACKATHON.hackathon = NEW.hackathon))
THEN
    RETURN NEW;
ELSE
    SELECT titolo INTO nomeHackathon FROM Hackathon WHERE Hackathon.id = NEW.hackathon;
    RAISE EXCEPTION E'Esiste gia\' un team con questo nome nell\'hackathon \' % \'!', nomeHackathon;
END IF;

END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER unicoNomeTeamHackathon
BEFORE INSERT ON TEAM_HACKATHON
FOR EACH ROW
EXECUTE FUNCTION unicoNomeTeamHackathonF();


-- 2. L’organizzatore non può aprire le registrazione dopo la fine dell’hackathon ##########
CREATE OR REPLACE FUNCTION organizzatoreRegistrazioniF()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.registrazioniAperte = 1 AND NEW.dataFine < CURRENT_DATE THEN
        RAISE EXCEPTION E'Non e\' possibile aprire le registrazioni dopo la fine dell\'hackathon';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER organizzatoreRegistrazioni
BEFORE INSERT OR UPDATE ON Hackathon
FOR EACH ROW
EXECUTE FUNCTION organizzatoreRegistrazioniF();

-- 3. Partecipante si può iscrivere solo con registrazioni aperte dall’organizzatore ##########
CREATE OR REPLACE FUNCTION iscrizionePartecipanteF()
RETURNS TRIGGER AS $$
BEGIN

    IF( EXISTS(
                SELECT titolo FROM Hackathon
                WHERE Hackathon.registrazioniAperte = 1 AND Hackathon.id = NEW.hackathon)) THEN

                RETURN NEW;
    END IF;

    RAISE EXCEPTION E'Le registrazioni per l\'hackathon sono chiuse!';

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER iscrizionePartecipante
BEFORE INSERT OR UPDATE ON HACKATHON_PARTECIPANTE
FOR EACH ROW
EXECUTE FUNCTION iscrizionePartecipanteF();

-- 4. Non possono esserci più iscritti del maxIscritti ##########
CREATE OR REPLACE FUNCTION maxPartecipanteF()
RETURNS TRIGGER AS $$
DECLARE
    c INT := 0;
    maxI INT := 0;
BEGIN

    SELECT COUNT(NEW.hackathon) INTO c FROM HACKATHON_PARTECIPANTE WHERE HACKATHON_PARTECIPANTE.hackathon = NEW.hackathon;
    SELECT maxIscritti INTO maxI FROM Hackathon WHERE Hackathon.id = NEW.hackathon;

    IF( c >= maxI ) THEN
        RAISE EXCEPTION E'Numero di iscritti massimo raggiunto per l\'hackathon!';
    END IF;

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER maxPartecipante
BEFORE INSERT ON HACKATHON_PARTECIPANTE
FOR EACH ROW
EXECUTE FUNCTION maxPartecipanteF();

-- 5. Non possono esserci più partecipanti allo stesso team che supera dimensioneTeam
CREATE OR REPLACE FUNCTION maxPartecipanteTeamF()
RETURNS TRIGGER AS $$
DECLARE
    c INT := 0;
    dimensioneTeamMax INT := 0;
BEGIN

    SELECT COUNT(NEW.team) INTO c FROM TEAM_PARTECIPANTE WHERE TEAM_PARTECIPANTE.team = NEW.team;
    SELECT dimensioneTeam INTO dimensioneTeamMax FROM Hackathon
    JOIN TEAM_HACKATHON ON TEAM_HACKATHON.team = NEW.team
    WHERE Hackathon.id = TEAM_HACKATHON.hackathon;

    IF( c >= dimensioneTeamMax) THEN
        RAISE EXCEPTION E'Numero di iscritti massimo raggiunto per il team!';
    END IF;

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER maxPartecipanteTeam
BEFORE INSERT ON TEAM_PARTECIPANTE
FOR EACH ROW
EXECUTE FUNCTION maxPartecipanteTeamF();

-- 6. Un utente non può avere piu ruoli. (Controllare che la mail sia unica nelle tabelle: Giudice, Partecipante, Organizzatore)
-- Partecipante -> Giudice, Organizzatore
CREATE OR REPLACE FUNCTION ruoloPartecipanteF()
RETURNS TRIGGER AS $$
BEGIN

    IF( EXISTS(
        SELECT email FROM Giudice WHERE Giudice.email = NEW.email
        UNION
        SELECT email FROM Organizzatore WHERE Organizzatore.email = NEW.email
    )) THEN
        RAISE EXCEPTION E'Esiste gia\' un utente con questa mail';
    END IF;

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ruoloPartecipante
BEFORE INSERT OR UPDATE ON Partecipante
FOR EACH ROW
EXECUTE FUNCTION ruoloPartecipanteF();

-- Giudice -> Organizzatore, Partecipante
CREATE OR REPLACE FUNCTION ruoloGiudiceF()
RETURNS TRIGGER AS $$
BEGIN

    IF( EXISTS(
        SELECT email FROM Partecipante WHERE Partecipante.email = NEW.email
        UNION
        SELECT email FROM Organizzatore WHERE Organizzatore.email = NEW.email
    )) THEN
        RAISE EXCEPTION E'Esiste gia\' un utente con questa mail';
    END IF;

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ruoloGiudice
BEFORE INSERT OR UPDATE ON Giudice
FOR EACH ROW
EXECUTE FUNCTION ruoloGiudiceF();

-- Organizzatore -> Giudice, Partecipante
CREATE OR REPLACE FUNCTION ruoloOrganizzatoreF()
RETURNS TRIGGER AS $$
BEGIN

    IF( EXISTS(
        SELECT email FROM Giudice WHERE Giudice.email = NEW.email
        UNION
        SELECT email FROM Partecipante WHERE Partecipante.email = NEW.email
    )) THEN
        RAISE EXCEPTION E'Esiste gia\' un utente con questa mail';
    END IF;

    RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ruoloOrganizzatore
BEFORE INSERT OR UPDATE ON Organizzatore
FOR EACH ROW
EXECUTE FUNCTION ruoloOrganizzatoreF();

-- UtenteDAO
CREATE OR REPLACE FUNCTION authenticate_user(email_in Email, password_in VARCHAR(16))
RETURNS TABLE(nome varchar,
              cognome varchar,
              email varchar,
              password varchar,
              ruolo TEXT) AS $$
BEGIN

    RETURN QUERY SELECT Partecipante.nome, Partecipante.cognome, Partecipante.email, Partecipante.password, 'partecipante' FROM Partecipante WHERE Partecipante.email = email_in AND Partecipante.password = password_in
    UNION
    SELECT  Giudice.nome, Giudice.cognome, Giudice.email, Giudice.password, 'giudice' FROM Giudice WHERE Giudice.email = email_in AND Giudice.password = password_in
    UNION
    SELECT  Organizzatore.nome, Organizzatore.cognome, Organizzatore.email, Organizzatore.password, 'organizzatore' FROM Organizzatore WHERE Organizzatore.email = email_in AND Organizzatore.password = password_in;

END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION register_user(
    nome_in VARCHAR,
    cognome_in VARCHAR,
    email_in VARCHAR,
    password_in VARCHAR,
    ruolo_in TEXT
)
RETURNS TEXT AS $$
BEGIN
    IF ruolo_in = 'partecipante' THEN
        INSERT INTO Partecipante(nome, cognome, email, password)
        VALUES (nome_in, cognome_in, email_in, password_in);
    ELSIF ruolo_in = 'giudice' THEN
        INSERT INTO Giudice(nome, cognome, email, password)
        VALUES (nome_in, cognome_in, email_in, password_in);
    ELSIF ruolo_in = 'organizzatore' THEN
        INSERT INTO Organizzatore(nome, cognome, email, password)
        VALUES (nome_in, cognome_in, email_in, password_in);
    ELSE
        RAISE EXCEPTION 'Ruolo non valido!';
    END IF;
    RETURN 'OK';
END;
$$ LANGUAGE plpgsql;

-- HackathonDAO
CREATE OR REPLACE FUNCTION calculate_classifica(hackathon_in INT)
RETURNS TABLE("Nome Team" VARCHAR, "Voto" Voto) AS $$
BEGIN

    IF (EXISTS(SELECT * FROM Hackathon WHERE Hackathon.ID = hackathon_in AND Hackathon.registrazioniAperte = 0 AND Hackathon.dataFine < CURRENT_DATE)) THEN

    RETURN QUERY SELECT team.nome as TN, team.voto as TV FROM hackathon JOIN team_hackathon ON hackathon.id = team_hackathon.hackathon
    JOIN team on team_hackathon.team = team.id
    WHERE hackathon.id = hackathon_in
    ORDER BY team.voto DESC;

    ELSE

    RAISE EXCEPTION 'Hackathon non valido!';

    END IF;

END;
$$ LANGUAGE plpgsql;

--- INSERT ---

INSERT INTO PARTECIPANTE (nome, cognome, email, password)
VALUES
('Alice', 'Rossi', 'alice.rossi@example.com', '1234'),
('Marco', 'Bianchi', 'marco.bianchi@example.com', 'Sicura123!'),
('Luca', 'Verdi', 'luca.verdi@example.com', 'P@ssword2024'),
('Giulia', 'Neri', 'giulia.neri@example.com', 'Login!2025'),
('john', 'orange', 'orange@example.com', 'lacopiabella'),
('Marco', 'Rossi', 'marcorossi@example.com', 'marcorossi1234');


INSERT INTO GIUDICE (nome, cognome, email, password)
VALUES
('Antonio','Poco ','antonio.pocomento@example.com', 'ioHoFortun4!'),
('john','lemon ','johnlemon@example.com', 'illimone69');


INSERT INTO ORGANIZZATORE (nome, cognome, email, password)
VALUES
('Giulio','Dardano ','giulio.dardano@example.com', '1somorfismo!');


INSERT INTO Sede (citta, via, codicePostale)
VALUES ('Politecnico di Milano', 'Piazza Leonardo da Vinci, 32', 80001);

INSERT INTO Hackathon (sede, dataInizio, maxIscritti, registrazioniAperte, dataFine, dimensioneTeam, titolo, descrizioneProblema, organizzatore) VALUES
(1, CURRENT_DATE-5, 100, 1, CURRENT_DATE+10, 4, 'HackTheFuture 2025', 'web spy', 'giulio.dardano@example.com'),
(1, CURRENT_DATE-2, 100, 1, CURRENT_DATE, 4, 'InnovAction', 'code break', 'giulio.dardano@example.com'),
(1, CURRENT_DATE-2, 100, 0, CURRENT_DATE, 4, 'Milky Way', 'SQL Injection', 'giulio.dardano@example.com');


INSERT INTO HACKATHON_PARTECIPANTE (hackathon, partecipante)
VALUES
--(1, 'alice.rossi@example.com'),
(1, 'marco.bianchi@example.com'),
(1, 'luca.verdi@example.com'),
(1, 'giulia.neri@example.com'),
(2, 'luca.verdi@example.com'),
(2, 'giulia.neri@example.com');

INSERT INTO HACKATHON_GIUDICE (hackathon, giudice)
VALUES
(1, 'antonio.pocomento@example.com'),
(1, 'johnlemon@example.com'),
(2, 'antonio.pocomento@example.com'),
(2, 'johnlemon@example.com');

INSERT INTO Team (nome, voto)
VALUES
('Unina', 7),
('Eureka', 5),
('Pocomentus', 6),
('Tantomentus', 8);

INSERT INTO TEAM_PARTECIPANTE (team, partecipante)
VALUES
--(1, 'alice.rossi@example.com'),
(1, 'marco.bianchi@example.com'),
(1, 'orange@example.com'),

(2, 'luca.verdi@example.com'),
(2, 'giulia.neri@example.com'),

(3, 'giulia.neri@example.com'),
(4, 'luca.verdi@example.com');

INSERT INTO TEAM_HACKATHON (team, hackathon)
VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2);

INSERT INTO Documento (team, commento, contenuto) VALUES
(1, 'Insufficiente', 'Documento'),
(1, 'ciao', 'Documento 2'),
(2, 'discreto', 'Documento 3'),
(3, 'buono', 'documebto 4');

UPDATE Hackathon set registrazioniAperte=0 where ID = 2;
UPDATE Hackathon set dataFine=CURRENT_DATE-1 where ID = 2;
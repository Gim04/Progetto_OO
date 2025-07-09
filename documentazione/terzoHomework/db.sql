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
    IF NEW.registrazioniAperte = 1 AND OLD.dataFine < CURRENT_DATE THEN
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
                JOIN HACKATHON_PARTECIPANTE
                ON HACKATHON_PARTECIPANTE.hackathon = hackathon.id
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

/*
CREATE OR REPLACE FUNCTION invite_partecipante_to_team(email_in VARCHAR, team_in VARCHAR, hackathon_in VARCHAR)
RETURNS BOOLEAN AS $$
DECLARE
    idPartecipante INT;
    idTeam INT;
BEGIN

    SELECT ID INTO idPartecipante FROM Partecipante WHERE email=email_in;
    IF idPartecipante IS NULL THEN
        RAISE EXCEPTION 'Partecipante non trovato!';
    END IF;

    SELECT team.ID INTO idTeam FROM team
    JOIN TEAM_HACKATHON ON TEAM_HACKATHON.team = team.id
    JOIN Hackathon ON Hackathon.id = TEAM_HACKATHON.hackathon
    WHERE nome=team_in AND titolo=hackathon_in;
    IF idTeam IS NULL THEN
        RAISE EXCEPTION 'Team non trovato!';
    END IF;

    INSERT INTO TEAM_PARTECIPANTE(team, partecipante) VALUES (idTeam, idPartecipante);
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql;*/
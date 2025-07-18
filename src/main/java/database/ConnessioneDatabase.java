package database;

import java.sql.*;

/**
 * Classe per la gestione della connessione al database PostgreSQL.
 */
public class ConnessioneDatabase
{
    /**
     * Istanza singleton della classe.
     */
    public static ConnessioneDatabase Instance; //pattern singleton

    /**
     * Oggetto di connessione al database.
     */
    Connection con;

    /**
     * Nome del database.
     */
    static final String DB_NAME = "hackathon";

    /**
     * Nome utente per accedere al database.
     */
    static final String USERNAME = "postgres";

    /**
     * Password per accedere al database.
     */
    static final String PASSWORD = "brosincode";

    /**
     * URL JDBC per la connessione al database.
     */
    static final String URL = "jdbc:postgresql://localhost:5432/" + DB_NAME;

    /**
     * Nome del driver JDBC PostgreSQL.
     */
    static final String DRIVER = "org.postgresql.Driver";

    /**
     * Costruttore della classe. Inizializza la connessione al database
     * se non è già stata creata un'altra istanza.
     */
    public ConnessioneDatabase()
    {
        if(Instance != null) return;

        Instance = this;

        try
        {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Restituisce la connessione al database.
     *
     * @return oggetto {@link Connection} attivo verso il database.
     */
    public Connection getConnection()
    {
        return con;
    }
}

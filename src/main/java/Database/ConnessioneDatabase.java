package Database;

import java.sql.*;

public class ConnessioneDatabase
{
    public static ConnessioneDatabase Instance;

    Connection con;
    static final String DB_NAME = "hackathon";
    static final String USERNAME = "postgres";
    static final String PASSWORD = "brosincode";
    static final String URL = "jdbc:postgresql://localhost:5432/" + DB_NAME;
    static final String DRIVER = "org.postgresql.Driver";

    public ConnessioneDatabase()
    {
        if(Instance != null) return;

        Instance = this;


        try
        {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    public Connection getConnection()
    {
        return con;
    }
}

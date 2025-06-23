package ImplementazionePostgresDAO;

import Database.ConnessioneDatabase;
import model.Partecipante;
import model.Giudice;
import model.Organizzatore;
import model.Utente;
import util.ERuolo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteImplementazioneDAO
{

    Connection connection;

    public UtenteImplementazioneDAO()
    {
        try
        {
            connection = ConnessioneDatabase.Instance.getConnection();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    ResultSet authenticateUserUtil(String table, String email, String password)
    {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM "+table+" WHERE email='" + email + "' AND password='" + password + "'");
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return null;
            }

            while (rs.next()) {
                return rs;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public Utente authenticateUser(String email, String password)
    {
        ResultSet rs = null;

        try {

            // CASO PARTECIPANTE
            rs = authenticateUserUtil("partecipante", email, password);
            if (rs != null) {
                return new Partecipante(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
            }

            // CASO GIUDICE
            rs = authenticateUserUtil("giudice", email, password);

            if (rs != null) {
                return new Giudice(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
            }

            // CASO ORGANIZZATORE
            rs = authenticateUserUtil("organizzatore", email, password);

            if (rs != null) {
                return new Organizzatore(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password"));
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String registerUser(String nome, String cognome, String email, String password, ERuolo ruolo)
    {
        try
        {
            final String sql = "INSERT INTO " + ruolo.toString() + "(nome, cognome, email, password) VALUES (" + "'" + nome + "'" + ", '" + cognome + "', '" + email + "', '" + password + "')";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.execute();

        }catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }

        return null;
    }

    public boolean inviteJudgeToHackathon(String email, String hackathon)
    {
        boolean result = false;
        ResultSet rs = null;
        int giudiceID = -1;
        int hackathonID = -1;
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM giudice WHERE email='" + email + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Giudice non trovato!");
            giudiceID = rs.getInt("ID");

            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo='" + hackathon + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Hackathon non trovato!");
            hackathonID = rs.getInt("ID");

            stmt = connection.prepareStatement("INSERT INTO HACKATHON_GIUDICE VALUES (" + "'" + hackathonID + "', '" + giudiceID + "')");
            stmt.execute();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }
}

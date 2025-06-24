package ImplementazionePostgresDAO;

import Database.ConnessioneDatabase;
import model.*;
import org.postgresql.jdbc2.ArrayAssistant;
import util.ERuolo;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class HackathonImplementazioneDAO {

    Connection connection;

    public HackathonImplementazioneDAO() {
        try {
            connection = ConnessioneDatabase.Instance.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Hackathon> getHackathonList() {
        ArrayList<Hackathon> r = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.id");
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            r = new ArrayList<>();

            while (rs.next()) {
                r.add(
                        new Hackathon(
                                rs.getString("titolo"),
                                new Sede(rs.getString("via"),
                                        rs.getString("citta"),
                                        rs.getInt("codicePostale")),
                                rs.getInt("dimensioneTeam"),
                                rs.getInt("maxIscritti"),
                                rs.getDate("dataInizio"),
                                rs.getDate("dataFine"),
                                rs.getBoolean("registrazioniAperte")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }

    public ArrayList<Hackathon> getHackathonListForJudge(String emailGiudice) {
        ArrayList<Hackathon> r = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.id " +
                                                                     "JOIN HACKATHON_GIUDICE ON hackathon.id = HACKATHON_GIUDICE.hackathon "+
                                                                     "JOIN giudice ON HACKATHON_GIUDICE.giudice = giudice.id " +
                                                                     "WHERE giudice.email = '"+ emailGiudice + "'");
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            r = new ArrayList<>();

            while (rs.next()) {

                Hackathon h = new Hackathon(
                        rs.getString("titolo"),
                        new Sede(rs.getString("via"),
                                rs.getString("citta"),
                                rs.getInt("codicePostale")),
                        rs.getInt("dimensioneTeam"),
                        rs.getInt("maxIscritti"),
                        rs.getDate("dataInizio"),
                        rs.getDate("dataFine"),
                        rs.getBoolean("registrazioniAperte")
                );

                ArrayList<Team> teams = getTeamForHackathon(h);
                for(Team t : teams) {
                    h.addTeam(t);
                }

                teams.clear();

                r.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }

    public ArrayList<Team> getTeamForHackathon(Hackathon hackathon)
    {
        ArrayList<Team> r = new ArrayList<>();

        /*
        "SELECT team.nome FROM hackathon JOIN sede ON hackathon.id = sede " +
                                                                      "JOIN TEAM_HACKATHON ON TEAM_HACKATHON.hackathon = hackathon.id" +
                                                                      "JOIN team ON team.id = TEAM_HACKATHON.team "
         */

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT nome, voto FROM TEAM_HACKATHON " +
                    "JOIN hackathon ON hackathon.id = TEAM_HACKATHON.hackathon " +
                    "JOIN team ON team.id = TEAM_HACKATHON.team WHERE hackathon.titolo = '" + hackathon.getTitolo() + "'");
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            while (rs.next()) {
                Team m = new Team(rs.getString("nome"));
                m.setVoto(rs.getInt("voto"));
                r.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }

    public ArrayList<Hackathon> getHackathonListForOrganizzatore(String email)
    {
        ArrayList<Hackathon> r = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.ID " +
                    "JOIN organizzatore ON hackathon.organizzatore = organizzatore.id " +
                    "WHERE organizzatore.email = '"+ email + "'");
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            r = new ArrayList<>();

            while (rs.next()) {

                Hackathon h = new Hackathon(
                        rs.getString("titolo"),
                        new Sede(rs.getString("via"),
                                rs.getString("citta"),
                                rs.getInt("codicePostale")),
                        rs.getInt("dimensioneTeam"),
                        rs.getInt("maxIscritti"),
                        rs.getDate("dataInizio"),
                        rs.getDate("dataFine"),
                        rs.getBoolean("registrazioniAperte")
                );

                ArrayList<Team> teams = getTeamForHackathon(h);
                for(Team t : teams) {
                    h.addTeam(t);
                }

                teams.clear();

                r.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }


    public boolean inserisciHackathon(String nome, int dimensioneTeam, int maxIscritti, LocalDate dataI, LocalDate dataF, boolean registrazioni, String email)
    {

        boolean result = false;

        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM organizzatore WHERE email = '"+ email + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            int val = 0;
            if(!registrazioni) val = 1;
            stmt = connection.prepareStatement("INSERT INTO hackathon(titolo, dimensioneTeam, dataInizio, dataFine, registrazioniAperte, maxIscritti, organizzatore)" +
                                                                      "VALUES (" + "'" + nome + "', '" + dimensioneTeam + "', '" + dataI + "', '" + dataF + "', " + val + ",'" + maxIscritti + "', "+ id +")");
            stmt.execute();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public boolean insertVoto(String nome, int voto)
    {
        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM team WHERE nome = '"+ nome + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt = connection.prepareStatement("UPDATE team set voto = '"+ voto + "' WHERE id = " + id);
            stmt.execute();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

}

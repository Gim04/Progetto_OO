package ImplementazionePostgresDAO;

import DAO.HackathonDAO;
import Database.ConnessioneDatabase;
import controller.Controller;
import model.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class HackathonImplementazionePostgresDAO implements HackathonDAO
{
    Connection connection;

    public HackathonImplementazionePostgresDAO() {
        try {
            connection = ConnessioneDatabase.Instance.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
        +-+-+-+-+-+-+-+-+-+
        |H|a|c|k|a|t|h|o|n|
        +-+-+-+-+-+-+-+-+-+
     */
    public ArrayList<Hackathon> getHackathonList()
    {
        ArrayList<Hackathon> r = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.id");
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

                ArrayList<Team> hteams = getTeamForHackathon(h);
                if(hteams == null) hteams = new ArrayList<>();
                h.setTeams(hteams);
                r.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }

    public ArrayList<Hackathon> getHackathonListForJudge(String emailGiudice)
    {
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
            if(registrazioni) val = 1;
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

    public ArrayList<Documento> getDocumenti(String nome, String hackathon)
    {
        ArrayList<Documento> r = new ArrayList<>();

        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM team WHERE nome = '"+ nome + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return new ArrayList<>();

            int id = set.getInt("ID");

            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = '"+ hackathon + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return new ArrayList<Documento>();

            int idH = set.getInt("ID");

            stmt = connection.prepareStatement(
                    "SELECT * FROM team JOIN documento ON team.id = documento.team " + "JOIN TEAM_HACKATHON ON TEAM_HACKATHON.team = team.id WHERE team.id = " + id + " AND TEAM_HACKATHON.hackathon = " + idH
            );
            set = stmt.executeQuery();

            if(!set.next())
                return new ArrayList<Documento>();

            do
            {
                Documento doc = new Documento(set.getString("contenuto"));
                doc.setCommento(set.getString("commento"));
                r.add(doc);
            }while (set.next());

            return r;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public boolean checkRegistrazioniChiuse(String hackathon)
    {
        try
        {
            ResultSet set = null;

            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = '"+ hackathon + "' AND registrazioniAperte=1");
            set = stmt.executeQuery();

            if(!set.next())
                return true;

            return false;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    public DefaultTableModel calculateClassifica(String hackathon)
    {
        DefaultTableModel model = null;
        String[] columns = {"Team", "Voto"};

        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT team.nome as TN, team.voto as TV FROM hackathon JOIN team_hackathon ON hackathon.id = team_hackathon.hackathon " +
                    "JOIN team on team_hackathon.team = team.id " +
                    "WHERE titolo = '"+ hackathon + "'" +
                    "ORDER BY team.voto DESC");
            set = stmt.executeQuery();

            if(!set.next())
                return new DefaultTableModel(null, columns);

            ArrayList<String> c0 = new ArrayList<>();
            ArrayList<String> c1 = new ArrayList<>();

            do
            {
                c0.add(set.getString("TN"));
                c1.add(set.getString("TV"));

            }while(set.next());

            String[][] data = new String[c0.size()][2];

            int i = 0;
            while(i < c0.size())
            {
                data[i][0] = c0.get(i);
                data[i][1] = c1.get(i);
                i++;
            }

            model = new DefaultTableModel(data, columns);

            return model;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /*
        +-+-+-+-+-+-+-+
        |U|t|i|l|i|t|y|
        +-+-+-+-+-+-+-+
     */
    public ArrayList<Team> getTeamForHackathon(Hackathon hackathon)
    {
        ArrayList<Team> r = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT team.id, nome, voto FROM TEAM_HACKATHON " +
                    "JOIN hackathon ON hackathon.id = TEAM_HACKATHON.hackathon " +
                    "JOIN team ON team.id = TEAM_HACKATHON.team WHERE hackathon.titolo = '" + hackathon.getTitolo() + "'");
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            while (rs.next()) {
                int id = rs.getInt("id");
                Team m = new Team(rs.getString("nome"));
                m.setVoto(rs.getInt("voto"));
                m.setPartecipanti(getPartecipantiOfTeam(id));
                r.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }

    public ArrayList<Partecipante> getPartecipantiOfTeam(int team)
    {
        ArrayList<Partecipante> r = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT nome, email, password, cognome FROM partecipante " +
                    "JOIN TEAM_PARTECIPANTE ON TEAM_PARTECIPANTE.partecipante = partecipante.id " +
                    "WHERE TEAM_PARTECIPANTE.team = " + team);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            while (rs.next()) {
                r.add(new Partecipante(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }
}

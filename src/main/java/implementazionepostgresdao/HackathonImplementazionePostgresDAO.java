package implementazionepostgresdao;

import dao.HackathonDAO;
import database.ConnessioneDatabase;
import model.*;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public List<Hackathon> getHackathonList()
    {
        List<Hackathon> r = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.id")){
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

                h.setDescrizioneProblema(rs.getString("descrizioneProblema"));

                List<Team> hteams = getTeamForHackathon(h);
                if(hteams == null) hteams = new ArrayList<>();
                h.setTeams(hteams);
                setPartecipantiOfHackathon(h);
                r.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }

    private void setPartecipantiOfHackathon(Hackathon h)
    {

        try(PreparedStatement stmt = connection.prepareStatement("SELECT * FROM HACKATHON_PARTECIPANTE JOIN HACKATHON ON hackathon.id = HACKATHON_PARTECIPANTE.hackathon JOIN Partecipante ON Partecipante.email = HACKATHON_PARTECIPANTE.partecipante WHERE hackathon.titolo = ?")){
            stmt.setString(1, h.getTitolo());
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
               h.addPartecipanteToList(new Partecipante(rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("password")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Hackathon> getHackathonListForJudge(String emailGiudice)
    {
        List<Hackathon> r = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.id " +
                "JOIN HACKATHON_GIUDICE ON hackathon.id = HACKATHON_GIUDICE.hackathon "+
                "JOIN giudice ON HACKATHON_GIUDICE.giudice = giudice.email " +
                "WHERE giudice.email = ?")) {

            stmt.setString(1, emailGiudice);
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

                List<Team> teams = getTeamForHackathon(h);
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

    public List<Hackathon> getHackathonListForOrganizzatore(String email)
    {
        List<Hackathon> r = new ArrayList<>();
        try( PreparedStatement stmt = connection.prepareStatement("SELECT * FROM hackathon JOIN sede ON hackathon.sede = sede.ID " +
                "JOIN organizzatore ON hackathon.organizzatore = organizzatore.email " +
                "WHERE organizzatore.email = ?")) {

            stmt.setString(1, email);
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

                List<Team> teams = getTeamForHackathon(h);
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


    public boolean inserisciHackathon(String nome, int dimensioneTeam, int maxIscritti, LocalDate dataI, LocalDate dataF, boolean registrazioni, String email, Sede sede)
    {

        boolean result = false;
        int val = 0;
        if(registrazioni) val = 1;

        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT id FROM Sede WHERE citta=? AND via=? AND codicePostale=?");
            stmt.setString(1, sede.getCitta());
            stmt.setString(2, sede.getVia());
            stmt.setInt(3, sede.getCodicePostale());
            ResultSet set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("id");

            stmt.close();
            stmt = connection.prepareStatement("INSERT INTO hackathon(sede, titolo, dimensioneTeam, dataInizio, dataFine, registrazioniAperte, maxIscritti, organizzatore)" +
                    "VALUES (?,?,?,?,?,?,?,?)");
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.setInt(3, dimensioneTeam);
            stmt.setDate(4, Date.valueOf(dataI));
            stmt.setDate(5, Date.valueOf(dataF));
            stmt.setInt(6, val);
            stmt.setInt(7, maxIscritti);
            stmt.setString(8, email);

            stmt.execute();

            stmt.close();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public List<Documento> getDocumenti(String nome, String hackathon)
    {
        List<Documento> r = new ArrayList<>();

        PreparedStatement stmt = null;
        try
        {
            stmt = connection.prepareStatement("SELECT ID FROM team WHERE nome = ?");
            stmt.setString(1, nome);

            ResultSet set;
            set = stmt.executeQuery();

            if(!set.next())
                return new ArrayList<>();

            int id = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = ?");
            stmt.setString(1, hackathon);

            set = stmt.executeQuery();

            if(!set.next())
                return new ArrayList<>();

            int idH = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement(
                    "SELECT * FROM team JOIN documento ON team.id = documento.team " + "JOIN TEAM_HACKATHON ON TEAM_HACKATHON.team = team.id WHERE team.id = ? AND TEAM_HACKATHON.hackathon = ?"
            );
            stmt.setInt(1, id);
            stmt.setInt(2, idH);
            set = stmt.executeQuery();

            if(!set.next())
                return new ArrayList<>();

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
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return new ArrayList<>();
    }

    public boolean checkRegistrazioniChiuse(String hackathon)
    {
        try(PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = ? AND registrazioniAperte=1"))
        {
            ResultSet set = null;

            stmt.setString(1, hackathon);

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

        try(PreparedStatement stmt = connection.prepareStatement("SELECT team.nome as TN, team.voto as TV FROM hackathon JOIN team_hackathon ON hackathon.id = team_hackathon.hackathon " +
                "JOIN team on team_hackathon.team = team.id " +
                "WHERE titolo = ? " +
                "ORDER BY team.voto DESC"))
        {
            ResultSet set = null;

            stmt.setString(1, hackathon);

            set = stmt.executeQuery();

            if(!set.next())
                return new DefaultTableModel(null, columns);

            List<String> c0 = new ArrayList<>();
            List<String> c1 = new ArrayList<>();

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
    public List<Team> getTeamForHackathon(Hackathon hackathon)
    {
        List<Team> r = new ArrayList<>();

        try( PreparedStatement stmt = connection.prepareStatement("SELECT team.id, nome, voto FROM TEAM_HACKATHON " +
                "JOIN hackathon ON hackathon.id = TEAM_HACKATHON.hackathon " +
                "JOIN team ON team.id = TEAM_HACKATHON.team WHERE hackathon.titolo = ?")) {

            stmt.setString(1, hackathon.getTitolo());

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

    public List<Partecipante> getPartecipantiOfTeam(int team)
    {
        List<Partecipante> r = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement("SELECT nome, email, password, cognome FROM partecipante " +
                "JOIN TEAM_PARTECIPANTE ON TEAM_PARTECIPANTE.partecipante = partecipante.email " +
                "WHERE TEAM_PARTECIPANTE.team = ?")) {

            stmt.setInt(1, team);

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

    public List<String> getSedi()
    {
        List<String> r = new ArrayList<>();

        try(PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Sede"))
        {


            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                return r;
            }

            while (rs.next()) {
                r.add(rs.getString("citta") + "," + rs.getString("via") + "," + rs.getString("codicePostale"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return r;
    }
}

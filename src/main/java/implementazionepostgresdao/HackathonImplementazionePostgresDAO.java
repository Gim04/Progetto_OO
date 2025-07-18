package implementazionepostgresdao;

import dao.HackathonDAO;
import database.ConnessioneDatabase;
import model.*;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'interfaccia HackathonDAO per l'accesso ai dati tramite PostgreSQL.
 * Gestisce operazioni di lettura, inserimento e gestione degli hackathon,
 * inclusi team, partecipanti, giudici, organizzatori, documenti e classifiche.
 */
public class HackathonImplementazionePostgresDAO implements HackathonDAO
{
    /**
     * The Connection.
     */
    Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     */
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
    /**
     * Restituisce la lista completa di hackathon presenti nel database.
     * Per ogni hackathon carica anche la sede, i team e i partecipanti.
     *
     * @return lista di oggetti Hackathon, lista vuota se non ci sono hackathon o in caso di errore.
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

    /**
     * Imposta la lista dei partecipanti di un determinato hackathon.
     *
     * @param h hackathon per cui si vogliono caricare i partecipanti.
     */
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

    /**
     * Restituisce la lista degli hackathon associati a un giudice, identificato tramite email.
     *
     * @param emailGiudice email del giudice.
     * @return lista di hackathon a cui il giudice partecipa, lista vuota se non presenti o in caso di errore.
     */
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

    /**
     * Restituisce la lista degli hackathon organizzati da un organizzatore specifico, identificato tramite email.
     *
     * @param email email dell'organizzatore.
     * @return lista di hackathon organizzati, lista vuota se non presenti o in caso di errore.
     */
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


    /**
     * Inserisce un nuovo hackathon nel database con i dati forniti.
     *
     * @param nome nome dell'hackathon.
     * @param dimensioneTeam dimensione massima del team.
     * @param maxIscritti numero massimo di iscritti.
     * @param dataI data di inizio.
     * @param dataF data di fine.
     * @param registrazioni booleano che indica se le registrazioni sono aperte.
     * @param email email dell'organizzatore.
     * @param sede oggetto sede dell'hackathon.
     * @return true se l'inserimento è andato a buon fine, false altrimenti.
     */
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

    /**
     * Restituisce la lista di documenti associati a un team e a un hackathon specifici.
     *
     * @param nome nome del team.
     * @param hackathon titolo dell'hackathon.
     * @return lista di documenti associati, lista vuota se non presenti o in caso di errore.
     */
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

    /**
     * Controlla se le registrazioni per un dato hackathon sono chiuse.
     *
     * @param hackathon titolo dell'hackathon.
     * @return true se le registrazioni sono chiuse o l'hackathon non esiste, false se sono aperte.
     */
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

    /**
     * Calcola e restituisce la classifica dei team di un hackathon sotto forma di DefaultTableModel.
     * La classifica è ordinata per voto decrescente.
     *
     * @param hackathon titolo dell'hackathon.
     * @return DefaultTableModel contenente colonne "Team" e "Voto", o null in caso di errore.
     */
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

    /**
     * Restituisce la lista dei team iscritti a un determinato hackathon.
     *
     * @param hackathon oggetto Hackathon di cui si vogliono ottenere i team.
     * @return lista di team, lista vuota se non presenti o in caso di errore.
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

    /**
     * Restituisce la lista dei partecipanti di un team identificato dall'id.
     *
     * @param team id del team.
     * @return lista di partecipanti del team, lista vuota se non presenti o in caso di errore.
     */
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

    /**
     * Restituisce la lista delle sedi presenti nel database.
     * Ogni sede è rappresentata come stringa nel formato "citta,via,codicePostale".
     *
     * @return lista di sedi, lista vuota se non presenti o in caso di errore.
     */
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

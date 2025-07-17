package implementazionepostgresdao;

import dao.UtenteDAO;
import database.ConnessioneDatabase;
import model.*;
import util.ERuolo;

import java.sql.*;
import java.util.ArrayList;

public class UtenteImplementazionePostgresDAO implements UtenteDAO
{

    Connection connection;

    public UtenteImplementazionePostgresDAO()
    {
        try
        {
            connection = ConnessioneDatabase.Instance.getConnection();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
        +-+-+-+-+-+-+
        |U|t|e|n|t|e|
        +-+-+-+-+-+-+
    */
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
        }finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return null;
    }

    public String registerUser(String nome, String cognome, String email, String password, ERuolo ruolo)
    {
        try( PreparedStatement stmt = connection.prepareStatement("INSERT INTO ? (nome, cognome, email, password) VALUES (?, ?, ?, ?)"))
        {
            stmt.setString(1, ruolo.toString());
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, email);
            stmt.setString(5, password);

            stmt.execute();

        }catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }

        return null;
    }

    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+
        |P|a|r|t|e|c|i|p|a|n|t|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+
    */
    public boolean iscriviPartecipanteAdHackathon(Hackathon hackathon, String email)
    {
        boolean result = false;
        ResultSet rs = null;
        int hackathonID = -1;

        PreparedStatement stmt = null;
        try
        {
            stmt = connection.prepareStatement("SELECT * FROM Hackathon WHERE titolo= ?");
            stmt.setString(1, hackathon.getTitolo());
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Hackathon non trovato!");

            hackathonID = rs.getInt("id");
            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO HACKATHON_PARTECIPANTE VALUES (?, ?)");
            stmt.setInt(1, hackathonID);
            stmt.setString(2, email);

            stmt.execute();

            stmt.close();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return result;
    }

    public boolean invitePartecipanteToTeam(String email, String team, String hackathon)
    {
        boolean result = false;
        ResultSet rs = null;
        int teamID = -1;

        PreparedStatement stmt = null;
        try
        {
            stmt = connection.prepareStatement("SELECT * FROM partecipante JOIN HACKATHON_PARTECIPANTE ON Partecipante.email =  HACKATHON_PARTECIPANTE.partecipante JOIN HACKATHON ON HACKATHON.id = HACKATHON_PARTECIPANTE.hackathon WHERE email= ?");

            stmt.setString(1, email);

            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Partecipante non trovato!");

            stmt.close();

            stmt = connection.prepareStatement("SELECT ID FROM team WHERE nome= ?");

            stmt.setString(1, team);

            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Team non trovato!");
            teamID = rs.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO TEAM_PARTECIPANTE VALUES (?, ?)");

            stmt.setInt(1, teamID);
            stmt.setString(2, email);

            stmt.execute();

            stmt.close();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return result;
    }

    public boolean creaTeam(String nome, String hackathon, String email)
    {
        PreparedStatement stmt = null;
        try
        {
            ResultSet set = null;
            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = ?");

            stmt.setString(1, hackathon);

            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("SELECT * FROM partecipante WHERE email = ?");

            stmt.setString(1, email);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO team(nome) VALUES(?)" ) ;
            stmt.setString(1, nome);
            stmt.execute();

            stmt.close();

            stmt = connection.prepareStatement("SELECT id FROM team WHERE nome = ?");

            stmt.setString(1, nome);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int idTeam = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO TEAM_PARTECIPANTE VALUES(?, ?)");

            stmt.setInt(1, idTeam);
            stmt.setString(2, email);
            stmt.execute();

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO TEAM_HACKATHON VALUES(?, ?)");
            stmt.setInt(1, idTeam);
            stmt.setInt(2, id);
            stmt.execute();

            stmt.close();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    public boolean addDocument(String team, String hackathon, String contenuto)
    {
        PreparedStatement stmt = null;
        try
        {
            ResultSet set = null;

            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = ?");
            stmt.setString(1, hackathon);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int idH = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("SELECT ID FROM team JOIN TEAM_HACKATHON ON TEAM_HACKATHON.hackathon = ? WHERE nome = ?");
            stmt.setInt(1, idH);
            stmt.setString(2, team);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");


            stmt.close();
            stmt = connection.prepareStatement("INSERT INTO documento(team, contenuto) VALUES(?,?)");
            stmt.setInt(1, id);
            stmt.setString(2, contenuto);
            stmt.execute();

            stmt.close();
            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    public ArrayList<Partecipante> getAllPartecipanti()
    {
        ArrayList<Partecipante> r = new ArrayList<>();

        PreparedStatement stmt = null;
        try
        {
            ResultSet set = null;
            stmt = connection.prepareStatement("SELECT * FROM partecipante");
            set = stmt.executeQuery();

            if(!set.next())
                return r;

            do
            {
                r.add(new Partecipante(set.getString("nome"), set.getString("cognome"), set.getString("email"), set.getString("password")));

            }while(set.next());

            stmt.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return r;
    }


    /*
        +-+-+-+-+-+-+-+
        |G|i|u|d|i|c|e|
        +-+-+-+-+-+-+-+
    */
    public boolean insertProblema(String problema, String hackathon)
    {
        PreparedStatement stmt = null;
        try
        {
            ResultSet set = null;
            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = ?");
            stmt.setString(1, hackathon);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("UPDATE hackathon set descrizioneProblema = ? WHERE id = ?");
            stmt.setString(1, problema);
            stmt.setInt(1, id);
            stmt.execute();

            stmt.close();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    public boolean updateCommentOfDocument(String team, String hackathon, String commento, String contenuto)
    {
        PreparedStatement stmt = null;
        try
        {
            int docid = -1;
            ResultSet set = null;

            final String query =
                    "SELECT documento.id as DOCID " +
                            "FROM documento " +
                            "JOIN Team ON documento.team = team.id " +
                            "JOIN TEAM_HACKATHON ON TEAM_HACKATHON.team = team.id " +
                            "JOIN hackathon ON TEAM_HACKATHON.hackathon = hackathon.id " +
                            "WHERE titolo = ? " +
                            "AND nome = ? " +
                            "AND contenuto = ?";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, hackathon);
            stmt.setString(2, team);
            stmt.setString(3, contenuto);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            docid = set.getInt("DOCID");

            stmt.close();

            stmt = connection.prepareStatement("UPDATE documento set commento = ? WHERE id = ?");
            stmt.setString(1, commento);
            stmt.setInt(2, docid);
            stmt.execute();

            stmt.close();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    public boolean insertVoto(String nome, int voto)
    {
        PreparedStatement stmt = null;
        try
        {
            ResultSet set = null;
            stmt = connection.prepareStatement("SELECT ID FROM team WHERE nome = ?");
            stmt.setString(1, nome);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("UPDATE team set voto = ? WHERE id = ?");
            stmt.setInt(1, voto);
            stmt.setInt(2, id);
            stmt.execute();

            stmt.close();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
        |O|r|g|a|n|i|z|z|a|t|o|r|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
    */
    public boolean updateRegistrazioni(boolean registrazione, String hackathon)
    {
        PreparedStatement stmt = null;
        try
        {
            ResultSet set = null;
            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = ?");
            stmt.setString(1, hackathon);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt.close();

            int v = 0;
            if(registrazione)
                v = 1;
            stmt = connection.prepareStatement("UPDATE hackathon set registrazioniAperte = ? WHERE id = ?");
            stmt.setInt(1, v);
            stmt.setInt(2, id);
            stmt.execute();

            stmt.close();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return false;
    }

    public boolean inviteJudgeToHackathon(String email, String hackathon)
    {
        boolean result = false;
        ResultSet rs = null;
        int hackathonID = -1;
        PreparedStatement stmt = null;
        try
        {
            stmt = connection.prepareStatement("SELECT * FROM giudice WHERE email=?");
            stmt.setString(1, email);
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Giudice non trovato!");

            stmt.close();

            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo=?");
            stmt.setString(1, hackathon);
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Hackathon non trovato!");
            hackathonID = rs.getInt("ID");

            stmt.close();

            stmt = connection.prepareStatement("INSERT INTO HACKATHON_GIUDICE VALUES (?,?)");
            stmt.setInt(1, hackathonID);
            stmt.setString(2, email);
            stmt.execute();

            stmt.close();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return result;
    }

    /*
        +-+-+-+-+-+-+-+
        |U|t|i|l|i|t|y|
        +-+-+-+-+-+-+-+
     */
    ResultSet authenticateUserUtil(String table, String email, String password)
    {
        try {
            CallableStatement proc = connection.prepareCall("CALL controllo_date_registrazioni()");
            proc.execute();
            proc.close();

            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM "+table+" WHERE email=? AND password=?"); // Result set verra' liberato nel metodo chiamante!
            stmt.setString(1, email);
            stmt.setString(2, password);
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
}

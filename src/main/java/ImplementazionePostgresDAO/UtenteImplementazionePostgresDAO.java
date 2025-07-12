package ImplementazionePostgresDAO;

import DAO.UtenteDAO;
import Database.ConnessioneDatabase;
import model.Partecipante;
import model.Giudice;
import model.Organizzatore;
import model.Utente;
import util.ERuolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+
        |P|a|r|t|e|c|i|p|a|n|t|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+
    */
    public boolean invitePartecipanteToTeam(String email, String team)
    {
        // PASSA IL NOME DELL'HACKATHON COGLIOOOOOOONEEEEEEEEEEEEE
        boolean result = false;
        ResultSet rs = null;
        int teamID = -1;
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM partecipante WHERE email='" + email + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Partecipante non trovato!");

            stmt = connection.prepareStatement("SELECT ID FROM team WHERE nome='" + team + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Team non trovato!");
            teamID = rs.getInt("ID");

            stmt = connection.prepareStatement("INSERT INTO TEAM_PARTECIPANTE VALUES (" + "'" + teamID + "', '" + email + "')");
            stmt.execute();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public boolean creaTeam(String nome, String hackathon, String email)
    {
        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = '"+ hackathon + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt = connection.prepareStatement("SELECT * FROM partecipante WHERE email = '"+ email + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            stmt = connection.prepareStatement("INSERT INTO team(nome) VALUES('"+nome+ "')" ) ;
            stmt.execute();

            stmt = connection.prepareStatement("SELECT id FROM team WHERE nome = '"+nome+"'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int idTeam = set.getInt("ID");
            stmt = connection.prepareStatement("INSERT INTO TEAM_PARTECIPANTE VALUES('" +idTeam+ "','" + email +"')");
            stmt.execute();

            stmt = connection.prepareStatement("INSERT INTO TEAM_HACKATHON VALUES('" +idTeam+ "','" + id +"')");
            stmt.execute();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean addDocument(String team, String hackathon, String contenuto)
    {
        try
        {
            ResultSet set = null;

            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = '"+ hackathon + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int idH = set.getInt("ID");

            stmt = connection.prepareStatement("SELECT ID FROM team JOIN TEAM_HACKATHON ON TEAM_HACKATHON.hackathon = "+idH+" WHERE nome = '"+ team + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt = connection.prepareStatement("INSERT INTO documento(team, contenuto) VALUES("+id+",'"+contenuto+"')");
            stmt.execute();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Partecipante> getAllPartecipanti()
    {
        ArrayList<Partecipante> r = new ArrayList<>();

        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM partecipante");
            set = stmt.executeQuery();

            if(!set.next())
                return r;

            do
            {
                r.add(new Partecipante(set.getString("nome"), set.getString("cognome"), set.getString("email"), set.getString("password")));

            }while(set.next());
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = '"+ hackathon + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            stmt = connection.prepareStatement("UPDATE hackathon set descrizioneProblema = '"+ problema + "' WHERE id = " + id);
            stmt.execute();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateCommentOfDocument(String team, String hackathon, String commento, String contenuto)
    {
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
                            "WHERE titolo = '" + hackathon + "' " +
                            "AND nome = '" + team + "' " +
                            "AND contenuto = '" + contenuto + "'";
            PreparedStatement stmt = connection.prepareStatement(query);
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            docid = set.getInt("DOCID");

            stmt = connection.prepareStatement("UPDATE documento set commento = '"+ commento + "' WHERE id = " + docid);
            stmt.execute();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
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

    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
        |O|r|g|a|n|i|z|z|a|t|o|r|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
    */
    public boolean updateRegistrazioni(boolean registrazione, String hackathon)
    {
        try
        {
            ResultSet set = null;
            PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo = '"+ hackathon + "'");
            set = stmt.executeQuery();

            if(!set.next())
                return false;

            int id = set.getInt("ID");

            int v = 0;
            if(registrazione)
                v = 1;
            stmt = connection.prepareStatement("UPDATE hackathon set registrazioniAperte = "+ v + " WHERE id = " + id);
            stmt.execute();

            return true;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean inviteJudgeToHackathon(String email, String hackathon)
    {
        boolean result = false;
        ResultSet rs = null;
        int hackathonID = -1;
        try
        {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM giudice WHERE email='" + email + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Giudice non trovato!");

            stmt = connection.prepareStatement("SELECT ID FROM hackathon WHERE titolo='" + hackathon + "'");
            rs = stmt.executeQuery();

            if(!rs.next()) throw new SQLException("Hackathon non trovato!");
            hackathonID = rs.getInt("ID");

            stmt = connection.prepareStatement("INSERT INTO HACKATHON_GIUDICE VALUES (" + "'" + hackathonID + "', '" + email + "')");
            stmt.execute();

            result = true;

        }catch (Exception e)
        {
            e.printStackTrace();
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
}

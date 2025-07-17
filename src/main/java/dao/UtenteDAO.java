package dao;

import model.Hackathon;
import model.Partecipante;
import model.Utente;
import util.ERuolo;

import java.util.ArrayList;

public interface UtenteDAO
{

    /*
        +-+-+-+-+-+-+
        |U|t|e|n|t|e|
        +-+-+-+-+-+-+
    */
    Utente authenticateUser(String email, String password);

    String registerUser(String nome, String cognome, String email, String password, ERuolo ruolo);


    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+
        |P|a|r|t|e|c|i|p|a|n|t|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+
    */
    boolean iscriviPartecipanteAdHackathon(Hackathon hackathon, String email);

    boolean invitePartecipanteToTeam(String email, String team, String hackathon);

    boolean creaTeam(String nome, String hackathon, String email);

    boolean addDocument(String team, String hackathon, String contenuto);

    ArrayList<Partecipante> getAllPartecipanti();

    /*
        +-+-+-+-+-+-+-+
        |G|i|u|d|i|c|e|
        +-+-+-+-+-+-+-+
    */
    boolean insertProblema(String problema, String hackathon);

    boolean updateCommentOfDocument(String team, String hackathon, String commento, String contenuto);

    boolean insertVoto(String nome, int voto);

    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
        |O|r|g|a|n|i|z|z|a|t|o|r|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
    */
    boolean updateRegistrazioni(boolean registrazione, String hackathon);

    boolean inviteJudgeToHackathon(String email, String hackathon);
}

package dao;

import model.Hackathon;
import model.Partecipante;
import model.Utente;
import util.ERuolo;

import java.util.List;


/**
 * Interfaccia DAO per la gestione delle operazioni legate agli utenti del sistema.
 */
public interface UtenteDAO
{

    /*
        +-+-+-+-+-+-+
        |U|t|e|n|t|e|
        +-+-+-+-+-+-+
    */

    /**
     * Autentica un utente sulla base delle credenziali fornite.
     *
     * @param email    email dell'utente.
     * @param password password dell'utente.
     * @return oggetto {@link Utente} se le credenziali sono valide, altrimenti null.
     */
    Utente authenticateUser(String email, String password);

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param nome     nome dell'utente.
     * @param cognome  cognome dell'utente.
     * @param email    email dell'utente.
     * @param password password dell'utente.
     * @param ruolo    ruolo dell'utente (es. PARTECIPANTE, GIUDICE, ORGANIZZATORE).
     * @return stringa che rappresenta l'esito della registrazione (es. successo, errore, ecc.).
     */
    String registerUser(String nome, String cognome, String email, String password, ERuolo ruolo);


    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+
        |P|a|r|t|e|c|i|p|a|n|t|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+
    */

    /**
     * Iscrive un partecipante a un hackathon specifico.
     *
     * @param hackathon oggetto {@link Hackathon} a cui iscriversi.
     * @param email     email del partecipante.
     * @return true se l'iscrizione è avvenuta con successo, false altrimenti.
     */
    boolean iscriviPartecipanteAdHackathon(Hackathon hackathon, String email);

    /**
     * Invia un invito a un partecipante per entrare a far parte di un team.
     *
     * @param email     email del partecipante.
     * @param team      nome del team.
     * @param hackathon nome dell'hackathon.
     * @return true se l'invito è stato inviato con successo, false altrimenti.
     */
    boolean invitePartecipanteToTeam(String email, String team, String hackathon);

    /**
     * Crea un nuovo team per un determinato hackathon.
     *
     * @param nome      nome del team.
     * @param hackathon nome dell'hackathon.
     * @param email     email del creatore del team.
     * @return true se il team è stato creato correttamente, false altrimenti.
     */
    boolean creaTeam(String nome, String hackathon, String email);

    /**
     * Aggiunge un documento a un team in un hackathon.
     *
     * @param team      nome del team.
     * @param hackathon nome dell'hackathon.
     * @param contenuto contenuto del documento.
     * @return true se il documento è stato aggiunto con successo, false altrimenti.
     */
    boolean addDocument(String team, String hackathon, String contenuto);

    /**
     * Restituisce tutti i partecipanti registrati nel sistema.
     *
     * @return lista di oggetti {@link Partecipante}.
     */
    List<Partecipante> getAllPartecipanti();

    /*
        +-+-+-+-+-+-+-+
        |G|i|u|d|i|c|e|
        +-+-+-+-+-+-+-+
    */

    /**
     * Inserisce un nuovo problema da assegnare ai partecipanti di un hackathon.
     *
     * @param problema  descrizione del problema.
     * @param hackathon nome dell'hackathon.
     * @return true se il problema è stato inserito con successo, false altrimenti.
     */
    boolean insertProblema(String problema, String hackathon);

    /**
     * Aggiorna il commento associato a un documento di un team.
     *
     * @param team      nome del team.
     * @param hackathon nome dell'hackathon.
     * @param commento  nuovo commento da inserire.
     * @param contenuto contenuto del documento a cui si riferisce il commento.
     * @return true se il commento è stato aggiornato correttamente, false altrimenti.
     */
    boolean updateCommentOfDocument(String team, String hackathon, String commento, String contenuto);


    /**
     * Inserisce un voto per un team.
     *
     * @param nome nome del team.
     * @param voto valore del voto.
     * @return true se il voto è stato inserito correttamente, false altrimenti.
     */
    boolean insertVoto(String nome, int voto);

    /*
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
        |O|r|g|a|n|i|z|z|a|t|o|r|e|
        +-+-+-+-+-+-+-+-+-+-+-+-+-+
    */

    /**
     * Aggiorna lo stato delle registrazioni per un determinato hackathon.
     *
     * @param registrazione nuovo stato delle registrazioni (true = aperte, false = chiuse).
     * @param hackathon     nome dell'hackathon.
     * @return true se l'aggiornamento è avvenuto correttamente, false altrimenti.
     */
    boolean updateRegistrazioni(boolean registrazione, String hackathon);

    /**
     * Invita un giudice a partecipare a un hackathon.
     *
     * @param email     email del giudice.
     * @param hackathon nome dell'hackathon.
     * @return true se l'invito è stato inviato con successo, false altrimenti.
     */
    boolean inviteJudgeToHackathon(String email, String hackathon);
}

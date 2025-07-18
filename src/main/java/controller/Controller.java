package controller;

import dao.UtenteDAO;
import dao.HackathonDAO;
import implementazionepostgresdao.HackathonImplementazionePostgresDAO;
import implementazionepostgresdao.UtenteImplementazionePostgresDAO;
import model.*;
import util.EDatabaseType;
import util.ERuolo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller principale per la gestione delle operazioni legate agli utenti,
 * hackathon, team, documenti e registrazioni.
 * <p>
 * Questa classe funge da punto centrale per orchestrare le chiamate tra
 * la GUI e i DAO, mantenendo anche una cache locale dei dati caricati.
 */
public class Controller
{
    /**
     * The Partecipanti.
     */
    List<Partecipante> partecipanti;
    /**
     * The Giudici.
     */
    List<Giudice> giudici;
    /**
     * The Organizzatori.
     */
    List<Organizzatore> organizzatori;
    /**
     * The Hackathons.
     */
    List<Hackathon> hackathons;

    /**
     * The Utente.
     */
    Utente utente;

    /**
     * The Utente implementazione dao.
     */
    UtenteDAO utenteImplementazioneDAO;
    /**
     * The Hackathonutente implementazione dao.
     */
    HackathonDAO hackathonutenteImplementazioneDAO;

    /**
     * Costruttore della classe Controller.
     * In base al tipo di database specificato, inizializza i DAO corretti.
     *
     * @param databaseType Tipo di database da utilizzare (POSTGRESQL, NONE, etc.)
     */
    public Controller(EDatabaseType databaseType)
    {
        switch (databaseType)
        {
            case POSTGRESQL:
                utenteImplementazioneDAO = new UtenteImplementazionePostgresDAO();
                hackathonutenteImplementazioneDAO = new HackathonImplementazionePostgresDAO();
                break;
            case NONE:
            default:
                JOptionPane.showMessageDialog(null, "Errore database non valido", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
        }

        partecipanti = new ArrayList<>();
        giudici = new ArrayList<>();
        organizzatori = new ArrayList<>();
        hackathons = new ArrayList<>();
    }

    /**
     * Restituisce la lista locale di hackathon caricati.
     *
     * @return Lista di hackathon attualmente caricati in memoria locale.
     */
    public List<Hackathon> getLocalAllHackathons()
    {
        return hackathons;
    }

    /**
     * Verifica se uno dei team passati appartiene all'hackathon specificato.
     *
     * @param hackathon Titolo dell'hackathon.
     * @param team      Lista di team da confrontare.
     * @return Il team trovato, altrimenti null.
     */
    public Team isLocalTeamInHackathon(String hackathon, List<Team> team)
    {
        if(team == null) return null;
        for(Hackathon h : hackathons)
        {
            if (h.getTitolo().equals(hackathon)) {
                for (Team t : h.getTeams())
                {
                    for(Team m : team)
                    {
                        if(t.getNome().equals(m.getNome()))
                        {
                            return m;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Restituisce i team a cui l'utente corrente appartiene.
     *
     * @return Lista di team dell'utente, null se nessuno.
     */
    public List<Team> getLocalCurrentUserTeam()
    {
        List<Team> teams = new ArrayList<>();
        for(Hackathon hackathon : hackathons)
        {
            for(Team m : hackathon.getTeams())
            {
                for(Partecipante p: m.getPartecipanti())
                {
                    if(p.getEmail().equals(( getCurrentUser()).getEmail()))
                        teams.add(m);
                }
            }
        }

        if(!teams.isEmpty())
            return teams;

        return null;
    }

    /**
     * Verifica se le registrazioni per un hackathon sono aperte.
     *
     * @param hackathon Titolo dell'hackathon.
     * @return true se le registrazioni sono aperte, false altrimenti.
     */
    public boolean getLocalRegistrazioniAperteOfHackathon(String hackathon)
    {
        for(Hackathon h : hackathons)
        {
            if(h.getTitolo().equals(hackathon))
            {
                return h.getRegistrazioniAperte();
            }
        }

        return false;
    }


    /**
     * Imposta lo stato delle registrazioni per un determinato hackathon.
     *
     * @param hackathon     Titolo dell'hackathon.
     * @param registrazione Stato delle registrazioni (true aperte, false chiuse).
     */
    public void setLocalRegistrazioniAperteOfHackathon(String hackathon, boolean registrazione)
    {
        for(Hackathon h : hackathons)
        {
            if(h.getTitolo().equals(hackathon))
            {
                h.setRegistrazioniAperte(registrazione);
            }
        }
    }

    /**
     * Esegue il login dell'utente con email e password.
     *
     * @param email    Email dell'utente.
     * @param password Password dell'utente.
     * @return Oggetto Utente autenticato o null in caso di errore.
     */
    public Utente logUser(String email, String password)
    {
        return utenteImplementazioneDAO.authenticateUser(email, password);
    }

    /**
     * Registra un nuovo utente nel sistema.
     *
     * @param nome     Nome dell'utente.
     * @param cognome  Cognome dell'utente.
     * @param email    Email dell'utente.
     * @param password Password dell'utente.
     * @param ruolo    Ruolo dell'utente (PARTECIPANTE, GIUDICE, ORGANIZZATORE).
     * @return Messaggio di esito dell'operazione.
     */
    public String registerUser(String nome, String cognome, String email, String password, String ruolo)
    {
        try
        {
            ERuolo role = ERuolo.valueOf(ruolo.toUpperCase());
            return utenteImplementazioneDAO.registerUser(nome, cognome, email, password, role);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Imposta l'utente attualmente loggato.
     *
     * @param utente Oggetto Utente corrente.
     */
    public void setCurrentUser(Utente utente)
    {
        this.utente = utente;
    }

    /**
     * Restituisce l'utente attualmente loggato.
     *
     * @return Utente corrente.
     */
    public Utente getCurrentUser()
    {
        return utente;
    }

    /**
     * Aggiorna la lista locale di hackathon per l'utente (tutti gli hackathon).
     */
    public void refreshHackathonList()
    {
        hackathons.clear();
        hackathons = hackathonutenteImplementazioneDAO.getHackathonList();
    }

    /**
     * Aggiorna la lista locale di hackathon per l'utente (tutti gli hackathon).
     */
    public void refreshHackathonListForGiudice()
    {
        hackathons.clear();
        hackathons = hackathonutenteImplementazioneDAO.getHackathonListForJudge(utente.getEmail());
    }

    /**
     * Restituisce i team iscritti a un determinato hackathon.
     *
     * @param name Nome dell'hackathon.
     * @return Lista di team dell'hackathon.
     */
    public List<Team> getLocalTeamsInHackathon(String name)
    {

        for(Hackathon h : hackathons)
        {
            if(h.getTitolo().equals(name))
            {
                return h.getTeams();
            }
        }

        return new ArrayList<>();
    }

    /**
     * Aggiorna la lista locale degli hackathon per un organizzatore.
     */
    public void refreshHackathonListForOrganizzatore()
    {
        hackathons.clear();
        hackathons = hackathonutenteImplementazioneDAO.getHackathonListForOrganizzatore(utente.getEmail());
    }

    /**
     * Invita un giudice a partecipare a un hackathon.
     *
     * @param email  Email del giudice.
     * @param titolo Titolo dell'hackathon.
     * @return true se l'invito è andato a buon fine.
     */
    public boolean inviteJudgeToHackathon(String email, String titolo)
    {
        return utenteImplementazioneDAO.inviteJudgeToHackathon(email, titolo);
    }

    /**
     * Aggiunge un nuovo hackathon al sistema.
     *
     * @param nome           Nome dell'hackathon.
     * @param dimensioneTeam Numero massimo di membri per team.
     * @param maxIscritti    Numero massimo di partecipanti.
     * @param dataI          Data di inizio.
     * @param dataF          Data di fine.
     * @param registrazioni  Stato iniziale delle registrazioni.
     * @param email          Email dell'organizzatore.
     * @param sede           Oggetto Sede.
     * @return true se l'inserimento ha successo.
     */
    public boolean aggiungiHackathon(String nome, int dimensioneTeam, int maxIscritti, LocalDate dataI, LocalDate dataF, boolean registrazioni, String email, Sede sede)
    {
        return hackathonutenteImplementazioneDAO.inserisciHackathon(nome, dimensioneTeam, maxIscritti, dataI, dataF, registrazioni, email, sede);
    }

    /**
     * Inserisce un voto per un team.
     *
     * @param team Nome del team.
     * @param voto Valutazione da assegnare.
     * @return true se l'operazione ha successo.
     */
    public boolean votaTeam(String team, int voto)
    {
        return utenteImplementazioneDAO.insertVoto(team, voto);
    }

    /**
     * Restituisce i documenti associati a un team in un hackathon.
     *
     * @param team      Nome del team.
     * @param hackathon Titolo dell'hackathon.
     * @return Lista di documenti del team.
     */
    public List<Documento> getDocumensOfTeam(String team, String hackathon)
    {
        return hackathonutenteImplementazioneDAO.getDocumenti(team, hackathon);
    }

    /**
     * Aggiorna il commento di un documento.
     *
     * @param team      Nome del team.
     * @param hackathon Nome dell'hackathon.
     * @param commento  Commento da aggiungere.
     * @param contenuto Contenuto del documento.
     * @return true se aggiornato con successo.
     */
    public boolean setCommentoOfDocument(String team, String hackathon, String commento, String contenuto)
    {
        return utenteImplementazioneDAO.updateCommentOfDocument(team, hackathon, commento, contenuto);
    }

    /**
     * Inserisce una descrizione del problema per un hackathon.
     *
     * @param hackathon   Titolo dell'hackathon.
     * @param descrizione Descrizione del problema.
     * @return true se l'inserimento ha successo.
     */
    public boolean setDescrizioneProblema(String hackathon, String descrizione)
    {
        return utenteImplementazioneDAO.insertProblema(descrizione, hackathon);
    }

    /**
     * Aggiorna lo stato delle registrazioni di un hackathon.
     *
     * @param registrazione true se aperte, false se chiuse.
     * @param hackathon     Titolo dell'hackathon.
     * @return true se aggiornamento riuscito.
     */
    public boolean updateRegistrazioniHackathon(boolean registrazione, String hackathon)
    {
        return utenteImplementazioneDAO.updateRegistrazioni(registrazione, hackathon);
    }

    /**
     * Crea un nuovo team e lo assegna all'hackathon specificato.
     *
     * @param nome      Nome del team.
     * @param hackathon Titolo dell'hackathon.
     * @param email     Email del partecipante che crea il team.
     * @return true se il team è stato creato con successo.
     */
    public boolean createTeam(String nome, String hackathon, String email)
    {
        boolean r = utenteImplementazioneDAO.creaTeam(nome, hackathon, email);
        if(r)
        {
            Team t = new Team(nome);
            for(Hackathon h : hackathons)
            {
                if(h.getTitolo().equals(hackathon))
                {
                    t.addPartecipante((Partecipante) getCurrentUser());
                    h.addTeam(t);
                    break;
                }
            }
        }
        return r;
    }

    /**
     * Restituisce un oggetto Partecipante dato un indirizzo email.
     *
     * @param email Email del partecipante.
     * @return Partecipante corrispondente, o null.
     */
    public Partecipante getLocalPartecipanteFromEmail(String email)
    {
        for(Partecipante p : partecipanti)
        {
            if(p.getEmail().equals(email))
                return p;
        }

        return null;
    }

    /**
     * Invita un partecipante a unirsi a un team in un hackathon.
     *
     * @param email     Email del partecipante.
     * @param team      Oggetto Team.
     * @param hackathon Titolo dell'hackathon.
     * @return true se l'invito ha avuto successo.
     */
    public boolean invitePartecipanteToTeam(String email, Team team, String hackathon)
    {
        boolean r = utenteImplementazioneDAO.invitePartecipanteToTeam(email, team.getNome(), hackathon);
        if(r)
        {
            Partecipante p = getLocalPartecipanteFromEmail(email);
            team.addPartecipante(p);
        }
        return r;
    }


    /**
     * Aggiunge un documento a un team in un hackathon.
     *
     * @param team      Nome del team.
     * @param hackathon Titolo dell'hackathon.
     * @param contenuto Contenuto del documento.
     * @return true se aggiunto con successo.
     */
    public boolean addDocument(String team, String hackathon, String contenuto)
    {
        boolean r = utenteImplementazioneDAO.addDocument(team, hackathon, contenuto);
        if(r)
        {
            for(Hackathon h : hackathons)
            {
                if(!h.getTitolo().equals(hackathon)) continue;
                for(Team t : h.getTeams())
                {
                    if(!t.getNome().equals(team)) continue;
                    t.addDocument(new Documento(contenuto));
                }
            }
        }
        return r;
    }


    /**
     * Verifica se le registrazioni per un hackathon sono chiuse.
     *
     * @param hackathon Titolo dell'hackathon.
     * @return true se le registrazioni sono chiuse.
     */
    public boolean checkRegistrazioniChiuse(String hackathon)
    {
        return hackathonutenteImplementazioneDAO.checkRegistrazioniChiuse(hackathon);
    }


    /**
     * Calcola la classifica per un hackathon.
     *
     * @param hackathon Titolo dell'hackathon.
     * @return Tabella con classifica dei team.
     */
    public DefaultTableModel calculateClassifica(String hackathon)
    {
        return hackathonutenteImplementazioneDAO.calculateClassifica(hackathon);
    }

    /**
     * Aggiorna la lista locale dei partecipanti dal database.
     */
    public void updateLocalPartecipanti()
    {
        partecipanti = utenteImplementazioneDAO.getAllPartecipanti();
    }

    /**
     * Verifica se un partecipante è iscritto a un hackathon.
     *
     * @param email     Email del partecipante.
     * @param hackathon Titolo dell'hackathon.
     * @return true se il partecipante è iscritto.
     */
    public boolean isLocalPartecipanteIscrittoAdHackathon(String email, String hackathon)
    {
        for(Hackathon h : hackathons)
        {
            if(h.getTitolo().equals(hackathon))
            {
                for(Partecipante p : h.getPartecipanti())
                {
                    if(p.getEmail().equals(email))
                        return true;
                }
            }
       }

        return false;
    }

    /**
     * Iscrive un partecipante a un hackathon.
     *
     * @param hackathon Oggetto Hackathon.
     * @param email     Email del partecipante.
     * @return true se iscritto con successo.
     */
    public boolean iscriviPartecipanteAdHackathon(Hackathon hackathon, String email)
    {
       return utenteImplementazioneDAO.iscriviPartecipanteAdHackathon(hackathon, email);
    }

    /**
     * Restituisce la descrizione del problema di un hackathon.
     *
     * @param hackathon Titolo dell'hackathon.
     * @return Descrizione del problema o "???" se non trovata.
     */
    public String getLocalDescrizioneProblema(String hackathon)
    {
        for(Hackathon h : hackathons)
        {
            if(h.getTitolo().equals(hackathon))
            {
                return h.getDescrizioneProblema();
            }
        }

        return "???";
    }

    /**
     * Restituisce la lista di tutte le sedi disponibili.
     *
     * @return Lista di nomi delle sedi.
     */
    public List<String> getSedi()
    {
        return hackathonutenteImplementazioneDAO.getSedi();
    }
}

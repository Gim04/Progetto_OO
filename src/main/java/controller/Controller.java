package controller;

import DAO.UtenteDAO;
import DAO.HackathonDAO;
import ImplementazionePostgresDAO.HackathonImplementazionePostgresDAO;
import ImplementazionePostgresDAO.UtenteImplementazionePostgresDAO;
import model.*;
import util.EDatabaseType;
import util.ERuolo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Controller
{
    // DEBUG ONLY
    ArrayList<Partecipante> partecipanti;
    ArrayList<Giudice> giudici;
    ArrayList<Organizzatore> organizzatori;
    ArrayList<Hackathon> hackathons;

    Utente utente;
    // --

    UtenteDAO utenteImplementazioneDAO;
    HackathonDAO hackathonutenteImplementazioneDAO;

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

        hackathons = new ArrayList<>();

        partecipanti = new ArrayList<>();
        giudici = new ArrayList<>();
        organizzatori = new ArrayList<>();
        hackathons = new ArrayList<>();

        /*
        // DEBUG ONLY
        utente = null;

        partecipanti = new ArrayList<>();
        giudici = new ArrayList<>();
        organizzatori = new ArrayList<>();
        hackathons = new ArrayList<>();

        partecipanti.add(new Partecipante("Alice","Rossi", "alice.rossi@example.com", "1234"));
        partecipanti.add(new Partecipante("Marco","Bianchi", "marco.bianchi@example.com", "Sicura123!"));
        partecipanti.add(new Partecipante("Luca","Verdi","luca.verdi@example.com", "P@ssword2024"));
        partecipanti.add(new Partecipante("Giulia","Neri", "giulia.neri@example.com", "Login!2025"));
        giudici.add(new Giudice("Antonio","Poco ","antonio.pocomento@example.com", "ioHoFortun4!"));
        organizzatori.add(new Organizzatore("Giulio","Dardano","giulio.dardano@example.com", "1somorfismo!"));

        //----
        // Creazione della sede
        Sede sede = new Sede("Politecnico di Milano", "Piazza Leonardo da Vinci, 32", 80001);

        // Creazione delle date
        Calendar cal = Calendar.getInstance();

        cal.set(2025, Calendar.JUNE, 15); // 15 giugno 2025
        Date dataInizio = cal.getTime();

        cal.set(2025, Calendar.JUNE, 17); // 17 giugno 2025
        Date dataFine = cal.getTime();

        // Creazione dell'hackathon
        hackathons.add(new Hackathon(
                "HackTheFuture 2025",
                sede,
                4,               // Dimensione team
                100,             // Max iscritti
                dataInizio,
                dataFine
        ));

        Team unina = new Team("Unina");
        Team eureka = new Team("Eureka");

        hackathons.get(0).addTeam(unina);
        hackathons.get(0).addTeam(eureka);

        organizzatori.get(0).apreRegistrazioni(hackathons.get(0), true);

        for(int i = 0; i < partecipanti.size(); i++)
        {
            partecipanti.get(i).iscrizioneHackathon(hackathons.get(0));
        }

        for(int i = 0; i < partecipanti.size(); i++)
        {
            if(i < 2)
            {
                partecipanti.get(i).iscrizioneTeam(unina, hackathons.get(0));
            }else{
                partecipanti.get(i).iscrizioneTeam(eureka, hackathons.get(0));
            }
        }
        //----

        // --*/
    }

    public ArrayList<Partecipante> getAllPartecipantUsers()
    {
        return partecipanti;
    }

    public ArrayList<Giudice> getAllGiudiciUsers()
    {
        return giudici;
    }

    public ArrayList<Organizzatore> getAllOrganizzatoriUsers()
    {
        return organizzatori;
    }

    public ArrayList<Hackathon> getLocalAllHackathons()
    {
        return hackathons;
    }

    public Team getLocalUserTeam()
    {
        for(Hackathon hackathon : hackathons)
        {
            for(Team m : hackathon.getTeams())
            {
                for(Partecipante p : m.getPartecipanti()) {
                    if(p.getEmail().equals(utente.getEmail()))
                        return m;
                }
            }
        }

        return null;
    }

    public void addPartecipante(Partecipante partecipante)
    {
        partecipanti.add(partecipante);
    }

    public Team isLocalTeamInHackathon(String hackathon, ArrayList<Team> team)
    {
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

    public boolean isLocalUserInTeam()
    {
        for(Hackathon hackathon : hackathons)
        {
            for(Team m : hackathon.getTeams())
            {
                for(Partecipante p: m.getPartecipanti()) {
                    if(p.getEmail().equals(( getCurrentUser()).getEmail()))
                        return true;
                }
            }
        }

        return false;
    }

    public boolean isLocalUserInTeam(String h)
    {
        for(Hackathon hackathon : hackathons)
        {
            if(hackathon.getTitolo().equals(h)) {
                for(Team m : hackathon.getTeams())
                {
                    for(Partecipante p: m.getPartecipanti()) {
                        if(p.getEmail().equals((getCurrentUser()).getEmail()))
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public ArrayList<Team> getLocalCurrentUserTeam()
    {
        ArrayList<Team> teams = new ArrayList<>();
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

    public void iscriviPartecipante(Partecipante partecipante, String titolo)
    {
        for(Hackathon h : hackathons)
        {
            if(titolo.equals(h.getTitolo()))
            {
                h.iscriviPartecipante(partecipante);
                break;
            }
        }
    }

    public void changeRegistrationFlag(Organizzatore organizzatore, boolean flag, String titolo)
    {
        for(Hackathon h : hackathons)
        {
            if(titolo.equals(h.getTitolo()))
            {
                organizzatore.apreRegistrazioni(h, flag);
                break;
            }
        }
    }

    public void inviteJudge(Organizzatore organizzatore, String email, String titolo)
    {
        Hackathon target = null;
        for(Hackathon h : hackathons)
        {
            if(titolo.equals(h.getTitolo()))
            {
                target = h;
                break;
            }
        }

        if(target == null) return;

        Giudice targetGiudice = null;
        for(Giudice g : giudici)
        {
            if(email.equals(g.getEmail()))
            {
                targetGiudice = g;
                break;
            }
        }

        if(targetGiudice == null) return;

        organizzatore.invitaGiudice(target, targetGiudice);
    }

    public void createTeam(Partecipante partecipante, String teamName, String titolo)
    {
        Hackathon hackathon = null;
        for(Hackathon h : hackathons) {
            if (titolo.equals(h.getTitolo()))
            {
                if(h.getPartecipanti().contains(partecipante))
                {
                    for(Team t : h.getTeams()) {
                        if(t.getNome().equals(teamName))
                        {
                            return;
                        }
                    }

                    hackathon = h;
                    break;
                }
            }
        }

        if(hackathon == null) return;

        partecipante.creaTeam(teamName, hackathon);
    }

    public void subscribeToTeam(Partecipante partecipante, String teamName, String titolo)
    {
        Hackathon hackathon = null;
        for(Hackathon h : hackathons) {
            if (titolo.equals(h.getTitolo()))
            {
                hackathon = h;
                break;
            }
        }

        if(hackathon == null) return;

        Team target = null;
        for(Team t : hackathon.getTeams())
        {
            if(t.getNome().equals(teamName))
            {
                target = t;
                break;
            }
        }

        if(target == null) return;

        partecipante.iscrizioneTeam(target, hackathon);
    }

    public Utente logUser(String email, String password)
    {
        return utenteImplementazioneDAO.authenticateUser(email, password);
    }

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

    public void setCurrentUser(Utente utente)
    {
        this.utente = utente;
    }
    public Utente getCurrentUser()
    {
        return utente;
    }

    public void getAllPartecipanti()
    {
        partecipanti.clear();
        partecipanti = utenteImplementazioneDAO.getAllPartecipanti();
    }

    public void refreshHackathonList()
    {
        hackathons.clear();
        hackathons = hackathonutenteImplementazioneDAO.getHackathonList();
    }

    public void refreshHackathonListForGiudice()
    {
        hackathons.clear();
        hackathons = hackathonutenteImplementazioneDAO.getHackathonListForJudge(utente.getEmail());
    }

    public ArrayList<Team> getLocalTeamsInHackathon(String name)
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

    public void refreshHackathonListForOrganizzatore()
    {
        hackathons.clear();
        hackathons = hackathonutenteImplementazioneDAO.getHackathonListForOrganizzatore(utente.getEmail());
    }

    public boolean inviteJudgeToHackathon(String email, String titolo)
    {
        return utenteImplementazioneDAO.inviteJudgeToHackathon(email, titolo);
    }

    public boolean aggiungiHackathon(String nome, int dimensioneTeam, int maxIscritti, LocalDate dataI, LocalDate dataF, boolean registrazioni, String email)
    {
        return hackathonutenteImplementazioneDAO.inserisciHackathon(nome, dimensioneTeam, maxIscritti, dataI, dataF, registrazioni, email);
    }

    public boolean votaTeam(String team, int voto)
    {
        return utenteImplementazioneDAO.insertVoto(team, voto);
    }

    public ArrayList<Documento> getDocumensOfTeam(String team, String hackathon)
    {
        return hackathonutenteImplementazioneDAO.getDocumenti(team, hackathon);
    }

    public boolean setCommentoOfDocument(String team, String hackathon, String commento, String contenuto)
    {
        return utenteImplementazioneDAO.updateCommentOfDocument(team, hackathon, commento, contenuto);
    }

    public boolean setDescrizioneProblema(String hackathon, String descrizione)
    {
        return utenteImplementazioneDAO.insertProblema(descrizione, hackathon);
    }

    public boolean updateRegistrazioniHackathon(boolean registrazione, String hackathon)
    {
        return utenteImplementazioneDAO.updateRegistrazioni(registrazione, hackathon);
    }

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

    public Partecipante getLocalPartecipanteFromEmail(String email)
    {
        for(Partecipante p : partecipanti)
        {
            if(p.getEmail().equals(email))
                return p;
        }

        return null;
    }

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

    public boolean checkRegistrazioniChiuse(String hackathon)
    {
        return hackathonutenteImplementazioneDAO.checkRegistrazioniChiuse(hackathon);
    }

    public DefaultTableModel calculateClassifica(String hackathon)
    {
        return hackathonutenteImplementazioneDAO.calculateClassifica(hackathon);
    }

    public void updateLocalPartecipanti()
    {
        partecipanti = utenteImplementazioneDAO.getAllPartecipanti();
    }

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

    public boolean iscriviPartecipanteAdHackathon(Hackathon hackathon, String email)
    {
       return utenteImplementazioneDAO.iscriviPartecipanteAdHackathon(hackathon, email);
    }
}

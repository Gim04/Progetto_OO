package controller;

import ImplementazionePostgresDAO.HackathonImplementazioneDAO;
import ImplementazionePostgresDAO.UtenteImplementazioneDAO;
import model.*;
import util.ERuolo;

import javax.swing.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Controller
{
    // DEBUG ONLY
    ArrayList<Partecipante> partecipanti;
    ArrayList<Giudice> giudici;
    ArrayList<Organizzatore> organizzatori;
    ArrayList<Hackathon> hackathons;

    Utente utente;
    // --

    UtenteImplementazioneDAO utenteImplementazioneDAO;
    HackathonImplementazioneDAO hackathonutenteImplementazioneDAO;

    public Controller()
    {
        utenteImplementazioneDAO = new UtenteImplementazioneDAO();
        hackathonutenteImplementazioneDAO = new HackathonImplementazioneDAO();

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

        return new ArrayList<Team>();
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
        return hackathonutenteImplementazioneDAO.insertVoto(team, voto);
    }
}

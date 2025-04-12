package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main {

    //test
    public static ArrayList<Hackathon> hackathons;

    public static void createHackathon(Hackathon hackathon)
    {
        hackathons.add(hackathon);
        System.out.println("Hackaton '" + hackathon.getTitolo() + "' creato!");
    }

    public static void iscriviPartecipanteAlTeam(Partecipante partecipante, String titolo, String nome)
    {
        for (Hackathon hackathon : hackathons)
        {
            if(hackathon.getTitolo().equals(titolo))
            {
                final Team team = hackathon.getTeam(nome);
                if(team == null) return;
                if(team.getDimensioneTeam() <= hackathon.getDimensioneTeam())
                {
                    hackathon.addPartecipante(partecipante, team);
                    System.out.println("Partecipante '" + partecipante.getNome() + "' aggiunto al team '" + team.getNome() + "'!");
                    return;
                }
            }
        }
    }

    public static void aggiungiTeam(Team team, String nome)
    {
        Hackathon hackathon = Main.findHackathonByName(nome);

        for(Team t : hackathon.getTeams())
        {
            if(t.getNome().equals(team.getNome()))
            {
                return;
            }
        }

        hackathon.addTeam(team);
        System.out.println("Team '" + team.getNome() + "' aggiunto!");
    }

    public static Hackathon findHackathonByName(String name)
    {
        for (Hackathon hackathon : hackathons)
        {
            if(hackathon.getTitolo().equals(name))
            {
                return hackathon;
            }
        }

        return null;
    }

    public static void printAll()
    {
        for (Hackathon hackathon : hackathons)
        {
            final ArrayList<Team> teams = hackathon.getTeams();
            System.out.println("---------Teams--------");
            for (Team team : teams)
            {
                System.out.println("Team: " + team.getNome());
                System.out.println("Numero iscritti: " + team.getDimensioneTeam());
                for(Partecipante p : team.getPartecipanti())
                {
                    System.out.println("\t" + p.getEmail());
                }
            }
            final ArrayList<Partecipante> partecipanti = hackathon.getPartecipanti();
            System.out.println("Numero Partecipanti: " + partecipanti.size());
            for (Partecipante p : partecipanti)
            {
                System.out.println("Partecipante: " + p.getNome());
            }
        }

    }

    public static void main(String[] args)
    {
        hackathons = new ArrayList<>();

        Partecipante alice              = (Partecipante) Utente.login("alice.rossi@example.com", "Passw0rd!",1);
        Partecipante marco              = (Partecipante) Utente.login("marco.bianchi@example.com", "Sicura123!", 1);
        Partecipante luca               = (Partecipante) Utente.login("luca.verdi@example.com", "P@ssword2024", 1);
        Partecipante giulia             = (Partecipante) Utente.login("giulia.neri@example.com", "Login!2025", 1);

        // Creazione della sede
        Sede sede = new Sede("Politecnico di Milano", "Piazza Leonardo da Vinci, 32", 80001);

        // Creazione delle date
        Calendar cal = Calendar.getInstance();

        cal.set(2025, Calendar.JUNE, 15); // 15 giugno 2025
        Date dataInizio = cal.getTime();

        cal.set(2025, Calendar.JUNE, 17); // 17 giugno 2025
        Date dataFine = cal.getTime();

        // Creazione dell'hackathon
        Hackathon hackathon = new Hackathon(
                "HackTheFuture 2025",
                sede,
                4,               // Dimensione team
                100,             // Max iscritti
                dataInizio,
                dataFine
        );

        hackathons.add(hackathon);

        alice.iscrizioneHackathon("HackTheFuture 2025");
        marco.iscrizioneHackathon("HackTheFuture 2025");
        luca.iscrizioneHackathon("HackTheFuture 2025");
        giulia.iscrizioneHackathon("HackTheFuture 2025");
        
        alice.creaTeam("Unina", "HackTheFuture 2025");
        luca.creaTeam("Eureka", "HackTheFuture 2025");

        alice.iscrizioneTeam("Unina", "HackTheFuture 2025");
        marco.iscrizioneTeam("Unina", "HackTheFuture 2025");
        luca.iscrizioneTeam("Eureka", "HackTheFuture 2025");
        giulia.iscrizioneTeam("Eureka", "HackTheFuture 2025");

        Main.printAll();
    }
}
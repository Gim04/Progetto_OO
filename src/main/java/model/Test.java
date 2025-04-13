package model;

import java.util.ArrayList;

public class Test
{
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
                if(team.getDimensioneTeam() <= hackathon.getDimensioneTeam() && hackathon.getRegistrazioniAperte())
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
        Hackathon hackathon = Test.findHackathonByName(nome);

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
}

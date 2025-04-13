package model;

public class Partecipante extends Utente
{
    public Partecipante(String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }

    public void iscrizioneTeam(String nome, String hackathon)
    {
        Test.iscriviPartecipanteAlTeam(this, hackathon, nome);
    }

    public void iscrizioneTeam(Team team, Hackathon hackathon)
    {
        if(team == null) return;
        if(team.getDimensioneTeam() <= hackathon.getDimensioneTeam() && hackathon.getRegistrazioniAperte())
        {
            hackathon.addPartecipante(this, team);
            System.out.println("Partecipante '" + this.getNome() + "' aggiunto al team '" + team.getNome() + "'!");
        }
    }

    public void iscrizioneHackathon(Hackathon h)
    {
        if(h != null)
            h.iscriviPartecipante(this);
    }

    public Team creaTeam(String nome, Hackathon h)
    {
        if(h == null)
        {
            return null;
        }

        for(Team t : h.getTeams())
        {
            if(t.getNome().equals(nome))
            {
                return null;
            }
        }

        Team t = new Team(nome);
        h.addTeam(t);

        return t;
    }

}

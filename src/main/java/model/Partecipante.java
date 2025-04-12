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

    public void iscrizioneHackathon(String hackathon)
    {
        final Hackathon h = Test.findHackathonByName(hackathon);
        if(h != null)
            h.iscriviPartecipante(this);
    }

    public void creaTeam(String nome, String hackathon)
    {
        final Hackathon h = Test.findHackathonByName(hackathon);
        if(h == null)
        {
            return;
        }

        for(Team t : h.getTeams())
        {
            if(t.getNome().equals(nome))
            {
                return;
            }
        }

        h.addTeam(new Team(nome));
    }

}

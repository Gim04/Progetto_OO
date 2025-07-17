package model;

public class Partecipante extends Utente
{
    public Partecipante(String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }

    public void iscrizioneTeam(Team team, Hackathon hackathon)
    {
        if(team == null) return;
        if(team.getDimensioneTeam() <= hackathon.getDimensioneTeam() && hackathon.getRegistrazioniAperte())
        {
            hackathon.addPartecipante(this, team);
        }
    }

}

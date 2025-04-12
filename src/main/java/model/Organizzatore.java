package model;

public class Organizzatore extends Utente
{

    public Organizzatore(String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }

    public void apreRegistrazioni(Hackathon hackathon, boolean apri)
    {
        hackathon.setRegistrazioniAperte(apri);
    }
}

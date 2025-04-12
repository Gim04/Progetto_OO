package model;

public class Giudice extends Utente
{
    public Giudice (String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }

    public void assegnaVoto(int voto, Team team)
    {
        team.setVoto(voto);
    }

    public void pubblicaProblema(Hackathon hackathon, String problema)
    {
        hackathon.setDescrizioneProblema(problema);
    }

    public void commentaDocumento(Documento documento, String commento)
    {
        documento.setCommento(commento);
    }
}

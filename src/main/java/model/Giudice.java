package model;

/**
 * Classe che rappresenta un giudice, estende la classe Utente.
 * Un giudice può partecipare alle attività di valutazione negli hackathon.
 */
public class Giudice extends Utente
{
    /**
     * Costruttore per creare un giudice con nome, cognome, email e password.
     *
     * @param nome     Nome del giudice.
     * @param cognome  Cognome del giudice.
     * @param email    Email del giudice.
     * @param password Password del giudice.
     */
    public Giudice (String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }
}

package model;

/**
 * Classe che rappresenta un organizzatore, estende la classe Utente.
 * Un organizzatore gestisce e coordina gli hackathon.
 */
public class Organizzatore extends Utente
{
    /**
     * Costruttore per creare un organizzatore con nome, cognome, email e password.
     *
     * @param nome     Nome dell'organizzatore.
     * @param cognome  Cognome dell'organizzatore.
     * @param email    Email dell'organizzatore.
     * @param password Password dell'organizzatore.
     */
    public Organizzatore(String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }
}

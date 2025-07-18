package model;

/**
 * Classe che rappresenta un partecipante, estende la classe Utente.
 * Un partecipante partecipa agli hackathon e può iscriversi a team.
 */
public class Partecipante extends Utente
{
    /**
     * Costruttore per creare un partecipante con nome, cognome, email e password.
     *
     * @param nome     Nome del partecipante.
     * @param cognome  Cognome del partecipante.
     * @param email    Email del partecipante.
     * @param password Password del partecipante.
     */
    public Partecipante(String nome, String cognome, String email, String password)
    {
        super(nome, cognome, email, password);
    }

}

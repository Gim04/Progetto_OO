package model;

/**
 * Classe base che rappresenta un utente generico del sistema.
 * Un utente è caratterizzato da nome, cognome, email e password.
 */
public class Utente
{
    private String nome;
    private String cognome;
    private String email;
    private String password;

    /**
     * Costruttore che crea un utente con i dati specificati.
     *
     * @param nome     Nome dell'utente.
     * @param cognome  Cognome dell'utente.
     * @param email    Email dell'utente, usata come identificativo.
     * @param password Password dell'utente.
     */
    public Utente(String nome, String cognome, String email, String password)
    {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente.
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente.
     */
    public String getCognome()
    {
        return cognome;
    }


    /**
     * Restituisce l'email dell'utente.
     *
     * @return L 'email dell'utente.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return La password dell'utente.
     */
    public String getPassword()
    {
        return password;
    }
}



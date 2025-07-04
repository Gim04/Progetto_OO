package model;

public class Utente
{
    private String nome;
    private String cognome;
    private String email;
    private String password;

    public Utente(String nome, String cognome, String email, String password)
    {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    public String getNome()
    {
        return nome;
    }

    public String getCognome()
    {
        return cognome;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }
}



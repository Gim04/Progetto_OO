package model;

import java.util.List;
import java.util.ArrayList;


/**
 * Classe che rappresenta un team partecipante a un hackathon.
 * Un team ha un nome, un voto, una lista di partecipanti e una lista di documenti.
 */
public class Team
{
    private String nome;
    private int voto;
    private List<Partecipante> partecipanti;
    private List<Documento> documenti;

    /**
     * Costruttore che crea un team con un nome specificato.
     *
     * @param nome Nome del team.
     */
    public Team(String nome)
    {
        this.nome = nome;

        partecipanti = new ArrayList<>();
        documenti = new ArrayList<>();
    }

    /**
     * Aggiunge un partecipante al team.
     *
     * @param partecipante Il partecipante da aggiungere.
     */
    public void addPartecipante(Partecipante partecipante)
    {
        this.partecipanti.add(partecipante);
    }

    /**
     * Imposta il voto del team.
     *
     * @param voto Voto da assegnare al team.
     */
    public void setVoto(int voto)
    {
        this.voto = voto;
    }

    /**
     * Restituisce il voto del team.
     *
     * @return Il voto attuale del team.
     */
    public int getVoto()
    {
         return voto;
    }

    /**
     * Restituisce il nome del team.
     *
     * @return Il nome del team.
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * Restituisce la lista dei partecipanti del team.
     *
     * @return Lista dei partecipanti.
     */
    public List<Partecipante> getPartecipanti()
    {
        return partecipanti;
    }

    /**
     * Imposta la lista dei partecipanti del team.
     *
     * @param partecipantiOfTeam Nuova lista di partecipanti da assegnare al team.
     */
    public void setPartecipanti(List<Partecipante> partecipantiOfTeam) {
        this.partecipanti = partecipantiOfTeam;
    }

    /**
     * Aggiunge un documento al team.
     *
     * @param documento Documento da aggiungere.
     */
    public void addDocument(Documento documento)
    {
        documenti.add(documento);
    }
}

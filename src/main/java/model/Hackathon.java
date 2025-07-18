package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * Rappresenta un hackathon con tutte le sue proprietà come titolo, sede,
 * dimensione dei team, iscritti, date, descrizione del problema e registrazioni.
 */
public class Hackathon
{
    private String titolo;
    private Sede sede;
    private int dimensioneTeam;
    private int maxIscritti;
    private Date dataInizio;
    private Date dataFine;
    private String descrizioneProblema;
    private boolean registrazioniAperte = false;

    private List<Team> teams;
    private List<Partecipante> partecipanti;
    private List<Giudice> giudici;

    /**
     * Costruisce un nuovo hackathon con i parametri specificati.
     *
     * @param titolo              il titolo dell'hackathon
     * @param sede                la sede in cui si svolge l'hackathon
     * @param dimensioneTeam      numero massimo di membri per team
     * @param maxIscritti         numero massimo di partecipanti iscritti
     * @param dataInizio          data di inizio dell'hackathon
     * @param dataFine            data di fine dell'hackathon
     * @param registrazioniAperte indica se le registrazioni sono aperte o chiuse
     */
    public Hackathon(String titolo, Sede sede, int dimensioneTeam, int maxIscritti, Date dataInizio, Date dataFine, boolean registrazioniAperte)
    {
        this.titolo = titolo;
        this.sede = sede;
        this.dimensioneTeam = dimensioneTeam;
        this.maxIscritti = maxIscritti;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.registrazioniAperte = registrazioniAperte;

        teams = new ArrayList<>();
        partecipanti = new ArrayList<>();
        giudici = new ArrayList<>();
    }

    /**
     * Restituisce la lista dei team iscritti all'hackathon.
     *
     * @return lista dei team
     */
    public List<Team> getTeams()
    {
        return teams;
    }

    /**
     * Restituisce la lista dei partecipanti iscritti all'hackathon.
     *
     * @return lista dei partecipanti
     */
    public List<Partecipante> getPartecipanti()
    {
        return partecipanti;
    }

    /**
     * Cerca un team per nome nella lista dei team iscritti.
     *
     * @param nome il nome del team da cercare
     * @return il team se trovato, altrimenti null
     */
    public Team getTeam(String nome)
    {
        for(Team t : teams)
        {
            if(t.getNome().equals(nome))
            {
                return t;
            }
        }

        return null;
    }

    /**
     * Aggiunge un team alla lista dei team iscritti.
     *
     * @param team il team da aggiungere
     */
    public void addTeam(Team team)
    {
        teams.add(team);
    }

    /**
     * Aggiunge un partecipante a un team, se il partecipante è già iscritto all'hackathon.
     *
     * @param partecipante il partecipante da aggiungere
     * @param team         il team a cui aggiungere il partecipante
     */
    public void addPartecipante(Partecipante partecipante, Team team)
    {
        if(isSubscribed(partecipante))
            team.addPartecipante(partecipante);
        else
            System.out.println("Impossibile aggiungere  '" + partecipante.getNome() + "', non iscritto all'hackathon '"+titolo+"'!");
    }

    /**
     * Iscrive un partecipante all'hackathon se le registrazioni sono aperte e c'è posto.
     *
     * @param partecipante il partecipante da iscrivere
     */
    public void iscriviPartecipante(Partecipante partecipante)
    {
        if(!registrazioniAperte)
        {
            System.out.println("Impossibile aggiungere '" + partecipante.getNome() + "', registrazione chiuse!");
            return;
        }

        if(partecipanti.size() <= maxIscritti)
            partecipanti.add(partecipante);
        else
            System.out.println("Impossibile aggiungere '" + partecipante.getNome() + "', hackathon pieno!");
    }

    /**
     * Aggiunge un partecipante direttamente alla lista dei partecipanti.
     *
     * @param partecipante il partecipante da aggiungere
     */
    public void addPartecipanteToList(Partecipante partecipante)
    {
        partecipanti.add(partecipante);
    }

    /**
     * Imposta la lista dei team iscritti.
     *
     * @param teams la lista dei team
     */
    public void setTeams(List<Team> teams)
    {
        this.teams = teams;
    }

    /**
     * Imposta la descrizione del problema dell'hackathon.
     *
     * @param descrizioneProblema la descrizione del problema
     */
    public void setDescrizioneProblema(String descrizioneProblema)
    {
        this.descrizioneProblema = descrizioneProblema;
    }

    /**
     * Restituisce il titolo dell'hackathon.
     *
     * @return il titolo
     */
    public String getTitolo()
    {
        return titolo;
    }

    /**
     * Imposta lo stato delle registrazioni (aperte/chiuse).
     *
     * @param registrazioniAperte true se le registrazioni sono aperte, false altrimenti
     */
    public void setRegistrazioniAperte(boolean registrazioniAperte)
    {
        this.registrazioniAperte = registrazioniAperte;
    }

    /**
     * Indica se le registrazioni sono aperte.
     *
     * @return true se aperte, false se chiuse
     */
    public boolean getRegistrazioniAperte()
    {
        return registrazioniAperte;
    }

    /**
     * Restituisce la dimensione massima dei team.
     *
     * @return la dimensione massima dei team
     */
    public int getDimensioneTeam()
    {
        return dimensioneTeam;
    }

    /**
     * Verifica se un partecipante è già iscritto all'hackathon.
     *
     * @param partecipante il partecipante da verificare
     * @return true se iscritto, false altrimenti
     */
    public boolean isSubscribed(Partecipante partecipante)
    {
        for(Partecipante p : partecipanti)
        {
            if(p.getEmail().equals(partecipante.getEmail()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Restituisce la data di inizio dell'hackathon.
     *
     * @return la data di inizio
     */
    public Date getDataInizio(){return dataInizio;}

    /**
     * Restituisce la data di fine dell'hackathon.
     *
     * @return la data di fine
     */
    public Date getDataFine(){return dataFine;}

    /**
     * Restituisce la descrizione del problema dell'hackathon.
     *
     * @return la descrizione del problema
     */
    public String getDescrizioneProblema(){return descrizioneProblema;}

    /**
     * Iscrive un giudice all'hackathon.
     *
     * @param g il giudice da iscrivere
     */
    public void iscriviGiudice(Giudice g)
    {
        giudici.add(g);
    }
}

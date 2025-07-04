package model;

import java.util.ArrayList;
import java.util.Date;

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

    private ArrayList<Team> teams;
    private ArrayList<Partecipante> partecipanti;
    private ArrayList<Giudice> giudici;

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

    public ArrayList<Team> getTeams()
    {
        return teams;
    }
    public ArrayList<Partecipante> getPartecipanti()
    {
        return partecipanti;
    }

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

    public void addTeam(Team team)
    {
        teams.add(team);
    }

    public void addPartecipante(Partecipante partecipante, Team team)
    {
        if(isSubscribed(partecipante))
            team.addPartecipante(partecipante);
        else
            System.out.println("Impossibile aggiungere '" + partecipante.getNome() + "', non iscritto all'hackathon '"+titolo+"'!");
    }

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

    public void setTeams(ArrayList<Team> teams)
    {
        this.teams = teams;
    }

    public void setDescrizioneProblema(String descrizioneProblema)
    {
        this.descrizioneProblema = descrizioneProblema;
    }

    public String getTitolo()
    {
        return titolo;
    }

    public void setRegistrazioniAperte(boolean registrazioniAperte)
    {
        this.registrazioniAperte = registrazioniAperte;

        if(registrazioniAperte == false)
        {
            //classifica.updateClassifica(teams);
        }
    }

    public boolean getRegistrazioniAperte()
    {
        return registrazioniAperte;
    }

    public int getDimensioneTeam()
    {
        return dimensioneTeam;
    }

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

    public Date getDataInizio(){return dataInizio;}
    public Date getDataFine(){return dataFine;}

    public void iscriviGiudice(Giudice g)
    {
        giudici.add(g);
    }
}

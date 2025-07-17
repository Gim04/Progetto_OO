package model;

import java.util.List;
import java.util.ArrayList;

public class Team
{
    private String nome;
    private int voto;
    private List<Partecipante> partecipanti;
    private List<Documento> documenti;

    public Team(String nome)
    {
        this.nome = nome;

        partecipanti = new ArrayList<>();
        documenti = new ArrayList<>();
    }

    public void addPartecipante(Partecipante partecipante)
    {
        this.partecipanti.add(partecipante);
    }

    public void setVoto(int voto)
    {
        this.voto = voto;
    }
    public int getVoto()
    {
         return voto;
    }

    public String getNome()
    {
        return nome;
    }

    public List<Documento> getDocumenti()
    {
        return documenti;
    }

    public int getDimensioneTeam()
    {
        return partecipanti.size();
    }

    public List<Partecipante> getPartecipanti()
    {
        return partecipanti;
    }

    public void setPartecipanti(List<Partecipante> partecipantiOfTeam) {
        this.partecipanti = partecipantiOfTeam;
    }

    public void addDocument(Documento documento)
    {
        documenti.add(documento);
    }
}

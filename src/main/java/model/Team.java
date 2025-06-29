package model;

import java.util.ArrayList;

public class Team
{
    private String nome;
    private int voto;
    private ArrayList<Partecipante> partecipanti;
    private ArrayList<Documento> documenti;

    public Team(String nome)
    {
        this.nome = nome;

        partecipanti = new ArrayList<>();
        documenti = new ArrayList<>();
    }

    public void caricaDocumento(Documento documento)
    {
        this.documenti.add(documento);
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

    public ArrayList<Documento> getDocumenti()
    {
        return documenti;
    }

    public int getDimensioneTeam()
    {
        return partecipanti.size();
    }

    public ArrayList<Partecipante> getPartecipanti()
    {
        return partecipanti;
    }

    public void setPartecipanti(ArrayList<Partecipante> partecipantiOfTeam) {
        this.partecipanti = partecipantiOfTeam;
    }
}

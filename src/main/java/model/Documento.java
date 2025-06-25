package model;

public class Documento
{
    private String commento;
    private String contenuto;

    public Documento(String contenuto)
    {
        this.contenuto = contenuto;
    }

    public void setCommento(String commento)
    {
        this.commento = commento;
    }

    public String getContenuto()
    {
        return contenuto;
    }

    public String getCommento()
    {
        return commento;
    }
}

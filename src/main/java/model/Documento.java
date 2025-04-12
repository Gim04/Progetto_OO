package model;

public class Documento
{
    private String commento;
    private String contenuto;

    public Documento(String contenuto)
    {
        this.contenuto = contenuto;
    }

    void setCommento(String commento)
    {
        this.commento = commento;
    }

    String getContenuto()
    {
        return contenuto;
    }

    String getCommento()
    {
        return commento;
    }
}

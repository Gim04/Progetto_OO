package model;

/**
 * Classe che rappresenta un documento con un contenuto e un commento opzionale.
 */
public class Documento
{
    private String commento;
    private String contenuto;

    /**
     * Costruisce un documento con il contenuto specificato.
     *
     * @param contenuto il contenuto del documento
     */
    public Documento(String contenuto)
    {
        this.contenuto = contenuto;
    }

    /**
     * Imposta il commento associato al documento.
     *
     * @param commento il commento da associare
     */
    public void setCommento(String commento)
    {
        this.commento = commento;
    }

    /**
     * Restituisce il contenuto del documento.
     *
     * @return il contenuto del documento
     */
    public String getContenuto()
    {
        return contenuto;
    }

    /**
     * Restituisce il commento associato al documento.
     *
     * @return il commento del documento, oppure null se non impostato
     */
    public String getCommento()
    {
        return commento;
    }
}

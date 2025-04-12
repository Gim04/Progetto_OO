package model;

public class Sede
{
    private String via;
    private String citta;
    private int codicePostale;

    public Sede(String via, String citta, int codicePostale)
    {
        this.via = via;
        this.citta = citta;
        this.codicePostale = codicePostale;
    }

    public String getVia() {
        return via;
    }

    public String getCitta() {
        return citta;
    }

    public int getCodicePostale() {
        return codicePostale;
    }

}

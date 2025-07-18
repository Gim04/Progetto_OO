package model;

/**
 * Classe che rappresenta una sede fisica con via, città e codice postale.
 */
public class Sede
{
    private String via;
    private String citta;
    private int codicePostale;

    /**
     * Costruttore per creare una sede con via, città e codice postale.
     *
     * @param via           Via della sede.
     * @param citta         Città della sede.
     * @param codicePostale Codice postale della sede.
     */
    public Sede(String via, String citta, int codicePostale)
    {
        this.via = via;
        this.citta = citta;
        this.codicePostale = codicePostale;
    }

    /**
     * Restituisce la via della sede.
     *
     * @return La via della sede.
     */
    public String getVia()
    {
        return via;
    }

    /**
     * Restituisce la città della sede.
     *
     * @return La città della sede.
     */
    public String getCitta()
    {
        return citta;
    }

    /**
     * Restituisce il codice postale della sede.
     *
     * @return Il codice postale della sede.
     */
    public int getCodicePostale()
    {
        return codicePostale;
    }
}

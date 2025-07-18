package util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Classe Theme che contiene costanti di stile utilizzate nell'applicazione,
 * come colori, font e icone.
 * Permette di centralizzare la gestione dell'aspetto grafico per una facile manutenzione.
 */
public class Theme
{
    /** Font di default usato nell'applicazione */
    private static final String DEFAULT_FONT = "SansSerif";

    /**
     * Colore principale per azioni primarie (verde chiaro)
     */
    public static final Color actionColor = new Color(48, 198, 30);

    /**
     * Variante più chiara del colore principale per azioni
     */
    public static final Color actionColor2 = new Color(66, 209, 49);

    /**
     * Colore secondario principale (blu scuro)
     */
    public static final Color secondaryColor = new Color(9, 28, 186);

    /**
     * Variante più chiara del colore secondario
     */
    public static final Color secondaryColor2 = new Color(9, 34, 237);

    /**
     * Colore di sfondo principale (rosso scuro)
     */
    public static final Color backColor = new Color(200, 0, 0);

    /**
     * Variante più chiara del colore di sfondo
     */
    public static final Color backColor2 = new Color(255, 77, 77);

    /**
     * Colore grigio chiaro usato per sfondi secondari
     */
    public static final Color gray = new Color(245, 245, 245);

    /**
     * Variante più scura del colore grigio chiaro
     */
    public static final Color gray2 = new Color(230, 230, 230);

    /**
     * Font per intestazioni principali
     */
    public static final Font header = new Font(DEFAULT_FONT, Font.BOLD, 24);

    /**
     * Font per paragrafi di testo
     */
    public static final Font paragraph = new Font(DEFAULT_FONT, Font.PLAIN, 14);

    /**
     * Font per titoli di liste hackathon
     */
    public static final Font hackathon_list_title = new Font(DEFAULT_FONT, Font.PLAIN, 16);

    /**
     * Font per paragrafi nelle liste hackathon
     */
    public static final Font hackathon_list_p = new Font(DEFAULT_FONT, Font.PLAIN, 12);

    /**
     * Icona per azione "aggiungi"
     */
    public static final ImageIcon ICON_ADD = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/add.png")));

    /**
     * Icona per commenti
     */
    public static final ImageIcon ICON_COMMENT = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/comment.png")));

    /**
     * Icona per documenti
     */
    public static final ImageIcon ICON_DOCS = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/docs.png")));

    /**
     * Icona per gruppi
     */
    public static final ImageIcon ICON_GROUP = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/group.png")));

    /**
     * Icona per aggiunta persona
     */
    public static final ImageIcon ICON_PERSON_ADD = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/person_add.png")));

    /**
     * Icona per sottrazione o rimozione
     */
    public static final ImageIcon ICON_SUB = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/sub.png")));

    /**
     * Icona per sblocco
     */
    public static final ImageIcon ICON_UNLOCK = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/unlock.png")));

    /**
     * Icona per voto o valutazione
     */
    public static final ImageIcon ICON_VOTE = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/vote.png")));

    /**
     * Icona per leaderboard o classifica
     */
    public static final ImageIcon ICON_LEADERBOARD = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/leaderboard.png")));

    /**
     * Icona per azione annulla (undo)
     */
    public static final ImageIcon ICON_UNDO = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/undo.png")));
}

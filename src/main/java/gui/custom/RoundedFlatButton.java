package gui.custom;

import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * {@code RoundedFlatButton} è un pulsante personalizzato con forma circolare,
 * che cambia colore al passaggio del mouse (hover) e supporta icone opzionali.
 * Lo stile è flat.
 */
public class RoundedFlatButton extends JButton
{

    /**
     * Colore di base del pulsante.
     */
    Color baseColor = Color.WHITE;

    /**
     * Colore mostrato quando il mouse è sopra il pulsante.
     */
    Color hoverColor = Color.DARK_GRAY;

    /**
     * Costruttore predefinito. Crea un pulsante rotondo bianco con hover grigio scuro.
     */
    public RoundedFlatButton()
    {
        super();
        initButton();
    }

    /**
     * Costruttore con specifica dei colori.
     *
     * @param baseColor  Il colore di base del pulsante.
     * @param hoverColor Il colore del pulsante quando il mouse ci passa sopra.
     */
    public RoundedFlatButton(Color baseColor, Color hoverColor)
    {
        super();
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        initButton();
    }

    /**
     * Costruttore con specifica dei colori e icona.
     *
     * @param baseColor  Il colore di base del pulsante.
     * @param hoverColor Il colore del pulsante quando il mouse ci passa sopra.
     * @param icon       L'icona da visualizzare al centro del pulsante.
     */
    public RoundedFlatButton(Color baseColor, Color hoverColor, ImageIcon icon)
    {
        super(icon);
        this.baseColor = baseColor;
        this.hoverColor = hoverColor;
        initButton();
    }

    /**
     * Inizializza lo stile e il comportamento del pulsante.
     */
    public void initButton()
    {
        this.setPreferredSize(new Dimension(32, 32));
        this.setFont(Theme.paragraph);
        this.setForeground(Color.BLACK);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setRolloverEnabled(true);
    }

    /**
     * Override del metodo di disegno del componente.
     * Disegna un cerchio colorato in base allo stato (hover o normale).
     *
     * @param g L'oggetto {@code Graphics} su cui disegnare.
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isEnabled())
            g2.setColor(getModel().isRollover() ? hoverColor : baseColor);
        else
            g2.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, getWidth(), getHeight(), Color.GRAY));

        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
        super.paintComponent(g);
    }

    /**
     * Override del metodo per evitare di disegnare bordi visibili.
     *
     * @param g Oggetto {@code Graphics} utilizzato per il disegno.
     */
    @Override
    protected void paintBorder(Graphics g)
    {
        // Nessun bordo
    }

    /**
     * Definisce l'area cliccabile del pulsante come un cerchio.
     *
     * @param x Coordinata x del punto da testare.
     * @param y Coordinata y del punto da testare.
     * @return {@code true} se il punto è all'interno del cerchio, altrimenti {@code false}.
     */
    @Override
    public boolean contains(int x, int y)
    {
        final double radius = getWidth() / 2.0;
        return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= radius * radius;
    }

}

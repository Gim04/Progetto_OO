package gui.custom;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

/**
 * Implementazione personalizzata di una scrollbar piatta e minimalista.
 * Rimuove i pulsanti standard di incremento/decremento e definisce uno stile semplice per il thumb e il track.
 */
public class FlatScrollBarUI extends BasicScrollBarUI
{
    /** Spessore minimo del thumb della scrollbar */
    private static final int THUMB_THICKNESS = 8;

    /**
     * Configura i colori della scrollbar, impostando il colore del thumb e del track.
     */
    @Override
    protected void configureScrollBarColors()
    {
        thumbColor = new Color(180, 180, 180);
        trackColor = new Color(240, 240, 240);
    }

    /**
     * Restituisce la dimensione minima del thumb della scrollbar.
     *
     * @return Dimensione minima del thumb.
     */
    @Override
    protected Dimension getMinimumThumbSize()
    {
        return new Dimension(THUMB_THICKNESS, THUMB_THICKNESS);
    }

    /**
     * Restituisce la dimensione preferita della scrollbar, dipendente dall'orientamento.
     *
     * @param c Componente Swing associata.
     * @return Dimensione preferita (larghezza se verticale, altezza se orizzontale).
     */
    @Override
    public Dimension getPreferredSize(JComponent c)
    {
        // Verticale: larghezza sottile : altezza sottile
        return (scrollbar.getOrientation() == VERTICAL)
                ? new Dimension(THUMB_THICKNESS, 0)
                : new Dimension(0, THUMB_THICKNESS);
    }

    /**
     * Crea un pulsante invisibile per sostituire il pulsante di decremento della scrollbar.
     *
     * @param orientation Orientamento del pulsante.
     * @return Pulsante vuoto e invisibile.
     */
    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createNullButton();
    }

    /**
     * Crea un pulsante invisibile per sostituire il pulsante di incremento della scrollbar.
     *
     * @param orientation Orientamento del pulsante.
     * @return Pulsante vuoto e invisibile.
     */
    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createNullButton();
    }

    /**
     * Crea un pulsante nullo (dimensioni 0 e invisibile) per eliminare i pulsanti standard della scrollbar.
     *
     * @return JButton invisibile e di dimensione nulla.
     */
    private JButton createNullButton()
    {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0,0));
        button.setMinimumSize(new Dimension(0,0));
        button.setMaximumSize(new Dimension(0,0));
        button.setVisible(false);
        return button;
    }

    /**
     * Disegna il thumb della scrollbar con lo stile personalizzato.
     *
     * @param g Graphics context.
     * @param c Componente a cui appartiene.
     * @param thumbBounds Dimensione e posizione del thumb.
     */
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(thumbColor);
        g2d.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        g2d.dispose();
    }

    /**
     * Disegna il track della scrollbar con lo stile personalizzato.
     *
     * @param g Graphics context.
     * @param c Componente a cui appartiene.
     * @param trackBounds Dimensione e posizione del track.
     */
    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(trackColor);
        g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2d.dispose();

    }
}

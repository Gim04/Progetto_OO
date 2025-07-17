package gui.custom;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class FlatScrollBarUI extends BasicScrollBarUI
{
    private static final int THUMB_THICKNESS = 8;

    @Override
    protected void configureScrollBarColors()
    {
        thumbColor = new Color(180, 180, 180);
        trackColor = new Color(240, 240, 240);
    }

    @Override
    protected Dimension getMinimumThumbSize()
    {
        return new Dimension(THUMB_THICKNESS, THUMB_THICKNESS);
    }

    @Override
    public Dimension getPreferredSize(JComponent c)
    {
        // Verticale: larghezza sottile : altezza sottile
        return (scrollbar.getOrientation() == VERTICAL)
                ? new Dimension(THUMB_THICKNESS, 0)
                : new Dimension(0, THUMB_THICKNESS);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createNullButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createNullButton();
    }

    private JButton createNullButton()
    {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0,0));
        button.setMinimumSize(new Dimension(0,0));
        button.setMaximumSize(new Dimension(0,0));
        button.setVisible(false);
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(thumbColor);
        g2d.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        g2d.dispose();
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(trackColor);
        g2d.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2d.dispose();

    }
}

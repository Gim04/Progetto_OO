package gui.custom;

import javax.swing.*;
import java.awt.*;

public class FlatCheckBox extends JCheckBox {

    public FlatCheckBox() {
        super();

        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        int boxSize = 16;
        int x = 0;
        int y = (getHeight() - boxSize) / 2;

        // Colori
        Color borderColor = isEnabled() ? Color.DARK_GRAY : Color.LIGHT_GRAY;
        Color fillColor = isSelected() ? new Color(50, 150, 255) : Color.WHITE;
        Color checkColor = Color.WHITE;

        // Disegna casella
        g2.setColor(fillColor);
        g2.fillRect(x, y, boxSize, boxSize);

        g2.setColor(borderColor);
        g2.drawRect(x, y, boxSize, boxSize);

        // Disegna check se selezionato
        if (isSelected()) {
            g2.setStroke(new BasicStroke(2));
            g2.setColor(checkColor);
            g2.drawLine(x + 3, y + 8, x + 7, y + 12);
            g2.drawLine(x + 7, y + 12, x + 13, y + 4);
        }

        g2.dispose();
    }
}
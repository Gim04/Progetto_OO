package gui.custom;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RoundedFlatButton extends JButton
{

    Color baseColor = Color.WHITE;
    Color hoverColor = Color.DARK_GRAY;

    public RoundedFlatButton()
    {
        super();

        initButton();
    }

    public RoundedFlatButton(Color baseColor, Color hoverColor)
    {
        super();

        this.baseColor = baseColor;
        this.hoverColor = hoverColor;

        initButton();
    }

    public RoundedFlatButton(Color baseColor, Color hoverColor, ImageIcon icon)
    {
        super(icon);

        this.baseColor = baseColor;
        this.hoverColor = hoverColor;

        initButton();
    }

    public void initButton()
    {
        this.setPreferredSize(new Dimension(32, 32));
        this.setFont(new Font("SansSerif", Font.PLAIN, 14));
        this.setForeground(Color.BLACK);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setOpaque(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        this.setRolloverEnabled(true);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(isEnabled())
            g2.setColor(getModel().isRollover() ? hoverColor : baseColor);
        else
            g2.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, getWidth(), getHeight(), Color.GRAY));

        g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g)
    {
        // Nessun bordo
    }

    @Override
    public boolean contains(int x, int y)
    {
        final double radius = getWidth() / 2.0;
        return Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= radius * radius; // x^2 + y^2 = k -> sin^2(x) + cos^2(x) = 1
    }

}

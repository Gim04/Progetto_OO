package gui.custom;

import javax.swing.*;
import java.awt.*;

public class FlatButton extends JButton
{
    public FlatButton()
    {
        super();

        setFocusPainted(false);
        setBackground(new Color(70,130,180));
        setForeground(Color.WHITE);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}

package gui.custom;

import util.Theme;

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
        setFont(Theme.paragraph);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    public FlatButton(String text)
    {
        super(text);

        setFocusPainted(false);
        setBackground(new Color(70,130,180));
        setForeground(Color.WHITE);
        setFont(Theme.paragraph);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }
}

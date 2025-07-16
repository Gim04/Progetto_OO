package gui.custom;

import javax.swing.*;
import java.awt.*;

public class FlatTextField extends JTextField
{
    public FlatTextField()
    {
        super();

        setFont(new Font("SansSerif", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200),1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        setBackground(Color.WHITE);
        setForeground(Color.DARK_GRAY);
        setCaretColor(Color.DARK_GRAY);
        setColumns(16);
    }
}

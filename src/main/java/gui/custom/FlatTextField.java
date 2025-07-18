package gui.custom;

import util.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * {@code FlatTextField} è un campo di testo personalizzato con uno stile flat.
 */
public class FlatTextField extends JTextField
{
    /**
     * Costruttore predefinito del campo di testo flat.
     * Applica uno stile personalizzato con font, bordo, padding, colori e larghezza fissa.
     */
    public FlatTextField()
    {
        super();

        setFont(Theme.paragraph);
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

package gui.custom;

import util.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Bottone personalizzato con stile flat, colore uniforme e margini predefiniti.
 * Estende {@link JButton} e applica uno stile coerente con il tema grafico dell'applicazione.
 */
public class FlatButton extends JButton
{
    /**
     * Costruttore senza testo del bottone.
     * Inizializza il bottone con uno stile flat predefinito.
     */
    public FlatButton()
    {
        super();

        setFocusPainted(false);
        setBackground(new Color(70,130,180));
        setForeground(Color.WHITE);
        setFont(Theme.paragraph);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    /**
     * Costruttore con testo del bottone.
     * Inizializza il bottone con uno stile flat predefinito e il testo specificato.
     *
     * @param text il testo da visualizzare sul bottone.
     */
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
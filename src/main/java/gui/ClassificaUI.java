package gui;

import controller.Controller;
import gui.base.TableForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Interfaccia grafica per la visualizzazione della classifica di un hackathon.
 * Estende {@link TableForm} e imposta un modello di tabella personalizzato per mostrare i risultati.
 */
public class ClassificaUI extends TableForm
{
    /**
     * Costruttore della classe ClassificaUI.
     * Inizializza la vista con il modello di tabella contenente la classifica.
     *
     * @param ctrl       Controller principale dell'applicazione.
     * @param jframe     Finestra principale su cui visualizzare il form.
     * @param tableModel Modello della tabella che rappresenta la classifica.
     */
    public ClassificaUI(Controller ctrl, JFrame jframe, DefaultTableModel tableModel)
    {
        super(ctrl, jframe);

        table.setModel(tableModel);
        table.revalidate();
        table.repaint();
    }
}

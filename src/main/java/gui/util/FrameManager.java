package gui.util;

import database.ConnessioneDatabase;
import controller.Controller;
import util.EDatabaseType;

import javax.swing.*;

/**
 * Gestore centrale per la finestra principale dell'applicazione.
 * Si occupa di inizializzare e gestire il cambio di pannelli (JComponent)
 * all'interno del JFrame principale, oltre a mantenere un riferimento al controller e alla connessione.
 */
public class FrameManager
{
    /**
     * Istanza singleton del FrameManager
     */
    public static FrameManager Instance = null;

    /**
     * Riferimento alla finestra principale dell'applicazione
     */
    JFrame frame = null;

    /**
     * Componente attualmente visualizzata nel frame
     */
    JComponent rootPanel = null;

    /**
     * Controller centrale per la logica dell'applicazione
     */
    Controller controller = null;

    /**
     * Connessione al database
     */
    ConnessioneDatabase connessioneDatabase = null;

    /**
     * Costruttore del FrameManager.
     * Imposta la finestra principale e inizializza controller e connessione al database.
     * Utilizza il pattern Singleton per garantire un'unica istanza.
     *
     * @param frame JFrame principale da gestire.
     * @param type  Tipo di database da utilizzare.
     * @throws IllegalStateException se il FrameManager è già stato inizializzato.
     */
    public FrameManager(JFrame frame, EDatabaseType type)
    {
        if(Instance != null)
        {
            throw new IllegalStateException("FrameManager has already been initialized");
        }

        Instance = this;
        this.frame = frame;

        connessioneDatabase = new ConnessioneDatabase();
        controller = new Controller(type);
    }

    /**
     * Cambia il contenuto visibile nel frame principale con il nuovo pannello specificato.
     *
     * @param next Il nuovo componente da visualizzare.
     */
    public void switchFrame(JComponent next)
    {
        rootPanel = next;

        rootPanel.setVisible(true);
        frame.setContentPane(rootPanel);
        frame.getGlassPane().setVisible(false);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Restituisce il controller associato a questo FrameManager.
     *
     * @return Istanza del Controller.
     */
    public Controller getController()
    {
        return controller;
    }
}
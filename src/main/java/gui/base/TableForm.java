package gui.base;
import controller.Controller;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Classe astratta che rappresenta una form con tabella integrata
 */
public class TableForm extends JPanel {

    /**
     * Controller associato alla form.
     */
    protected Controller controller;

    /**
     * Finestra principale che ospita la form.
     */
    protected JFrame frame;

    /**
     * Tabella Swing per visualizzare i dati.
     */
    protected JTable table;

    /**
     * Modello della tabella per gestire righe e colonne.
     */
    protected DefaultTableModel tableModel;

    /**
     * Pannello contenente i pulsanti.
     */
    protected JPanel btnPanel;

    /**
     * Scroll pane per rendere la tabella scrollabile.
     */
    protected JScrollPane scrollPane;

    /**
     * Costruttore della classe TableForm.
     *
     * @param c  il controller da associare alla form.
     * @param jf il JFrame principale che contiene questa form.
     */
    public TableForm(Controller c, JFrame jf) {
        this.frame = jf;
        this.controller = c;

        setLayout(new BorderLayout());

        // Modello della tabella non modificabile
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(btnPanel, BorderLayout.NORTH);
    }
}
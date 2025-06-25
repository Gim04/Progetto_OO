package gui.base;
import controller.Controller;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableForm extends JPanel {

    protected Controller controller;
    protected JFrame frame;

    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JPanel btnPanel;

    public TableForm(Controller c, JFrame jf) {
        this.frame = jf;
        this.controller = c;

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(btnPanel, BorderLayout.SOUTH);
    }
}
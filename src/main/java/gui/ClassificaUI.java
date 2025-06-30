package gui;

import controller.Controller;
import gui.base.TableForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClassificaUI extends TableForm
{

    public ClassificaUI(Controller ctrl, JFrame jframe, DefaultTableModel tableModel)
    {
        super(ctrl, jframe);

        table.setModel(tableModel);
        table.revalidate();
        table.repaint();
    }
}
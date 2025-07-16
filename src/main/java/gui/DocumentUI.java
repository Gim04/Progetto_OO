package gui;

import controller.Controller;
import gui.base.TableForm;
import gui.custom.RoundedFlatButton;
import model.Documento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

import model.Giudice;
import model.Partecipante;

public class DocumentUI extends TableForm
{
    RoundedFlatButton btnAddComment;
    RoundedFlatButton btnAddDocument;

    public DocumentUI(Controller ctrl, JFrame jframe, String team, String hackathon)
    {
        super(ctrl, jframe);

        refreshUILocalTable(team, hackathon);

        if(controller.getCurrentUser() instanceof Giudice) {

            btnAddComment = new RoundedFlatButton(new Color(48, 198, 30), new Color(66, 209, 49), new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/add.png"))));
            btnAddComment.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (table.getSelectedRow() < 0) {
                        JOptionPane.showMessageDialog(frame, "Devi selezionare un documento", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    final String input = JOptionPane.showInputDialog("Commento:");
                    if (input != null) {
                        controller.setCommentoOfDocument(team, hackathon, input, table.getValueAt(table.getSelectedRow(), 0).toString());

                        JOptionPane.showMessageDialog(null, "Commento assegnato!");
                        refreshUILocalTable(team, hackathon);
                    } else {
                        JOptionPane.showMessageDialog(null, "Operazione annullata.");
                    }
                }
            });

            btnPanel.add(btnAddComment);
        }
        else if(controller.getCurrentUser() instanceof Partecipante)
        {
            btnAddDocument = new RoundedFlatButton(new Color(48, 198, 30), new Color(66, 209, 49), new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/add.png"))));
            btnAddDocument.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    final String input = JOptionPane.showInputDialog("Contenuto documento:");
                    if (input != null) {
                        controller.addDocument(team, hackathon, input);

                        JOptionPane.showMessageDialog(null, "Documento aggiunto!");
                        refreshUILocalTable(team, hackathon);
                    } else {
                        JOptionPane.showMessageDialog(null, "Operazione annullata.");
                    }
                }
            });

            btnPanel.add(btnAddDocument);
        }
    }

    public void refreshUILocalTable(String team, String hackathon)
    {
        final ArrayList<Documento> documenti = controller.getDocumensOfTeam(team, hackathon);

        String[] columns = {"Contenuto", "Commento"};
        String[][] data = new String[documenti.size()][columns.length];
        int i = 0;
        for (Documento d : documenti)
        {
            data[i][0] = d.getContenuto();
            data[i][1] = d.getCommento();
            i++;
        }

        table.setModel(new DefaultTableModel(data, columns));
        table.revalidate();
        table.repaint();

    }
}

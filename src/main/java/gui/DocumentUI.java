package gui;

import controller.Controller;
import gui.base.TableForm;
import gui.custom.RoundedFlatButton;
import gui.util.FrameManager;
import model.Documento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;
import java.awt.Color;

import model.Giudice;
import model.Partecipante;
import util.Theme;

public class DocumentUI extends TableForm
{
    RoundedFlatButton btnAddComment;
    RoundedFlatButton btnAddDocument;
    RoundedFlatButton backButton;

    public DocumentUI(Controller ctrl, JFrame jframe, String team, String hackathon)
    {
        super(ctrl, jframe);

        refreshUILocalTable(team, hackathon);

        backButton = new RoundedFlatButton(Theme.backColor, Theme.backColor2, Theme.ICON_UNDO);

        if(controller.getCurrentUser() instanceof Giudice) {

            backButton.addActionListener(e -> {
                FrameManager.Instance.switchFrame(new GiudiceTeamGui(frame, controller, hackathon));
            });

            btnAddComment = new RoundedFlatButton(new Color(48, 198, 30), new Color(66, 209, 49), Theme.ICON_ADD);
            btnAddComment.addActionListener( e-> {
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

            });

            btnPanel.add(btnAddComment);
            btnPanel.add(backButton);
        }
        else if(controller.getCurrentUser() instanceof Partecipante)
        {
            backButton.addActionListener(e -> {
                FrameManager.Instance.switchFrame(new TeamUI(controller, frame, team, hackathon));
            });

            btnAddDocument = new RoundedFlatButton(Theme.actionColor, Theme.actionColor2, Theme.ICON_ADD);
            btnAddDocument.addActionListener(e-> {
                final String input = JOptionPane.showInputDialog("Contenuto documento:");
                if (input != null) {
                    controller.addDocument(team, hackathon, input);

                    JOptionPane.showMessageDialog(null, "Documento aggiunto!");
                    refreshUILocalTable(team, hackathon);
                } else {
                    JOptionPane.showMessageDialog(null, "Operazione annullata.");
                }
            });

            btnPanel.add(btnAddDocument);
        }

        btnPanel.add(backButton);
    }

    public void refreshUILocalTable(String team, String hackathon)
    {
        final List<Documento> documenti = controller.getDocumensOfTeam(team, hackathon);

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

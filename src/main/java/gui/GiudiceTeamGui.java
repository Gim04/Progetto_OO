package gui;

import controller.Controller;
import gui.custom.RoundedFlatButton;
import gui.util.FrameManager;
import model.Team;
import util.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interfaccia grafica che consente al giudice di:
 * - visualizzare i team partecipanti a un hackathon,
 * - assegnare voti ai team selezionati,
 * - accedere all'interfaccia di commento dei documenti.
 */
public class GiudiceTeamGui extends JPanel
{
    /** Pannello che contiene i pulsanti di azione */
    private JPanel btnPanel;

    /** Pulsante per assegnare un voto a un team */
    private RoundedFlatButton votaTeamButton;

    /** Pulsante per accedere alla schermata di commento dei documenti del team */
    private RoundedFlatButton commentaDocumentoButton;

    /** Pulsante per tornare alla schermata precedente */
    private RoundedFlatButton backButton;

    /** Tabella contenente i team e i relativi voti */
    private JTable table1;

    /**
     * Finestra principale dell'applicazione
     */
    JFrame frame;

    /**
     * Controller dell'applicazione per gestire i dati
     */
    Controller controller;

    /**
     * Costruttore di GiudiceTeamGui.
     * Imposta la visualizzazione dei team di un hackathon, consente di votarli e accedere ai documenti.
     *
     * @param frame      JFrame principale dell'applicazione.
     * @param controller Controller utilizzato per gestire logica e dati.
     * @param hackathon  Titolo dell'hackathon attivo.
     */
    public GiudiceTeamGui(JFrame frame, Controller controller, String hackathon) {
        this.frame = frame;
        this.controller = controller;

        setLayout(new BorderLayout(5, 5));

        final JScrollPane scrollPane1 = new JScrollPane();
        table1 = new JTable();
        scrollPane1.setViewportView(table1);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        refreshUILocalTable(hackathon);

        backButton = new RoundedFlatButton(Theme.backColor, Theme.backColor2, Theme.ICON_UNDO);

        backButton.addActionListener(e -> {
            FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame));
        });

        votaTeamButton = new RoundedFlatButton(new Color(9, 28, 186, 255), new Color(9, 34, 237, 255), Theme.ICON_VOTE);

        votaTeamButton.addActionListener(e -> {
            if (table1.getSelectedRow() < 0) {
                JOptionPane.showMessageDialog(frame, "Devi selezionare un team", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final String input = JOptionPane.showInputDialog("Voto:");
            if (input != null) {

                int voto;
                try {
                    voto = Integer.parseInt(input);
                    controller.votaTeam(table1.getValueAt(table1.getSelectedRow(), 0).toString(), voto);
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Formato del voto non valido!", "Error ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "Hai assegnato al team " + table1.getValueAt(table1.getSelectedRow(), 0).toString() + " il voto " + voto);
                refreshUILocalTable(hackathon);
            } else {
                JOptionPane.showMessageDialog(null, "Operazione annullata.");
            }
        });

        commentaDocumentoButton = new RoundedFlatButton(new Color(48, 198, 30), new Color(66, 209, 49), Theme.ICON_COMMENT);
        commentaDocumentoButton.addActionListener( e-> {
                if (table1.getSelectedRow() < 0) {
                    JOptionPane.showMessageDialog(frame, "Devi selezionare un team", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                FrameManager.Instance.switchFrame(new DocumentUI(controller, frame, table1.getValueAt(table1.getSelectedRow(), 0).toString(), hackathon));

        });

        btnPanel.add(votaTeamButton);
        btnPanel.add(commentaDocumentoButton);
        btnPanel.add(backButton);

        add(scrollPane1, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    /**
     * Aggiorna i dati visualizzati nella tabella con i team dell'hackathon corrente.
     * Mostra nome del team e voto assegnato.
     *
     * @param hackathon Titolo dell'hackathon di riferimento.
     */
    public void refreshUILocalTable(String hackathon) {
        controller.refreshHackathonListForGiudice();
        final List<Team> teams = controller.getLocalTeamsInHackathon(hackathon);

        String[] columns = {"Team", "Voto"};
        String[][] data = new String[teams.size()][columns.length];
        int i = 0;
        for (Team m : teams) {
            data[i][0] = m.getNome();
            data[i][1] = String.valueOf(m.getVoto());
            i++;
        }

        table1.setModel(new DefaultTableModel(data, columns));
    }
}

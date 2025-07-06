package gui;

import controller.Controller;
import gui.custom.FlatScrollBarUI;
import gui.custom.RoundedFlatButton;
import gui.util.FrameManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class HackathonList extends JPanel
{
    private Controller controller;
    private JFrame frame;

    private JList hackathonList;
    private JPanel btnPanel;

    // Pulsanti partecipante
    private JButton creaTeam;
    private JButton invitaPartecipanteAlTeam;
    private JButton viewClassifica;
    //

    // Pulsanti giudice
    private JButton visualizzaTeam;
    private JButton pubblicaProblema;
    //

    // Pulsanti organizzatore
    private JButton invitaGiudice;
    private JButton creaHackathon;
    private JButton chiudiRegistrazioni;
    private JButton documenti;
    //

    private JPanel rowsViewport;
    private JScrollPane scrollPane;

    public HackathonList(ArrayList<Hackathon> hackathons, Controller c, JFrame f) {
        controller = c;
        frame = f;

        this.setLayout(new BorderLayout());

        rowsViewport = new JPanel();
        rowsViewport.setLayout(new BoxLayout(rowsViewport, BoxLayout.Y_AXIS));
        rowsViewport.setBackground(Color.WHITE);

        refreshLocalUIHackathonList(hackathons);

        scrollPane = new JScrollPane(rowsViewport);
        scrollPane.getVerticalScrollBar().setUI(new FlatScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(5);
        scrollPane.getHorizontalScrollBar().setUI(new FlatScrollBarUI());
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);

        /*
        viewClassifica = new JButton("View Classifica");
        viewClassifica.addActionListener(e -> {
            if (hackathonList.getSelectedValue() != null) {
                JFrame out = new JFrame("Classifica");
                out.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                out.setSize(240, 180);
                out.setLocationRelativeTo(null);
                out.setType(Window.Type.UTILITY);
                out.setContentPane(new ClassificaUI(controller, frame, controller.calculateClassifica(hackathonList.getSelectedValue().toString())));
                out.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        if (controller.getCurrentUser() instanceof Partecipante) {
            creaTeam = new JButton("Crea Team");
            invitaPartecipanteAlTeam = new JButton("Invita Partecipante");
            documenti = new JButton("Documenti");

            creaTeam.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
                    if (controller.checkRegistrazioniChiuse(hackathonList.getSelectedValue().toString())) {
                        JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    final String input = JOptionPane.showInputDialog("Nome team:");
                    if (input != null) {
                        if (controller.createTeam(input, hackathonList.getSelectedValue().toString(), controller.getCurrentUser().getEmail()))
                            JOptionPane.showMessageDialog(null, "Team creato!");
                        else
                            JOptionPane.showMessageDialog(frame, "Non e' stato possibile creare il team!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Operazione annullata.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            invitaPartecipanteAlTeam.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
                    if (controller.checkRegistrazioniChiuse(hackathonList.getSelectedValue().toString())) {
                        JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!controller.isLocalUserInTeam()) {
                        JOptionPane.showMessageDialog(frame, "Devi stare in un team!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Team t = controller.getLocalUserTeam();
                    if (t == null || !controller.isLocalTeamInHackathon(hackathonList.getSelectedValue().toString(), t.getNome())) {
                        JOptionPane.showMessageDialog(frame, "Hackathon errato!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    final String input = JOptionPane.showInputDialog("Email:");
                    if (input != null) {

                        if (!input.contains("@") || !input.contains(".")) {
                            JOptionPane.showMessageDialog(frame, "Mail non valida!");
                            return;
                        }

                        if (controller.invitePartecipanteToTeam(input, t))
                            JOptionPane.showMessageDialog(frame, "Partecipante aggiunto al team '" + t.getNome() + "'");
                        else
                            JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Operazione annullata.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            documenti.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
                    if (controller.isLocalUserInTeam(hackathonList.getSelectedValue().toString())) {
                        Team t = controller.getLocalCurrentUserTeam();
                        FrameManager.Instance.switchFrame(new DocumentUI(controller, frame, t.getNome(), hackathonList.getSelectedValue().toString()));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Devi far parte di un team!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4));

            btnPanel.add(creaTeam, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            btnPanel.add(invitaPartecipanteAlTeam, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            btnPanel.add(documenti, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            btnPanel.add(viewClassifica, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
        } else if (controller.getCurrentUser() instanceof Giudice) {
            visualizzaTeam = new JButton("Visualizza Teams");
            pubblicaProblema = new JButton("Pubblica Problema");

            visualizzaTeam.addActionListener(e ->
            {
                if (hackathonList.getSelectedValue() != null) {
                    if (controller.checkRegistrazioniChiuse(hackathonList.getSelectedValue().toString())) {
                        JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    FrameManager.Instance.switchFrame(new GiudiceTeamGui(f, controller, hackathonList.getSelectedValue().toString()).$$$getRootComponent$$$());
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            pubblicaProblema.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
                    if (controller.checkRegistrazioniChiuse(hackathonList.getSelectedValue().toString())) {
                        JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    final String input = JOptionPane.showInputDialog("Problema:");
                    if (input != null) {
                        if (controller.setDescrizioneProblema(hackathonList.getSelectedValue().toString(), input))
                            JOptionPane.showMessageDialog(null, "Descrizione problema assegnata!");
                        else
                            JOptionPane.showMessageDialog(frame, "Non e' stato possibile impostare la descrizione del problema!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Operazione annullata.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3));

            btnPanel.add(visualizzaTeam, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            btnPanel.add(pubblicaProblema, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));
            btnPanel.add(viewClassifica, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

        } else {
            invitaGiudice = new JButton("Invita Giudice");
            creaHackathon = new JButton("Crea Hackathon");
            chiudiRegistrazioni = new JButton("Chiudi Registrazioni");
            chiudiRegistrazioni.setEnabled(false);

            invitaGiudice.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
                    if (controller.checkRegistrazioniChiuse(hackathonList.getSelectedValue().toString())) {
                        JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    final String input = JOptionPane.showInputDialog("Email:");
                    if (input != null) {

                        if (!input.contains("@") || !input.contains(".")) {
                            JOptionPane.showMessageDialog(frame, "Mail non valida!");
                            return;
                        }

                        if (controller.inviteJudgeToHackathon(input, hackathonList.getSelectedValue().toString()))
                            JOptionPane.showMessageDialog(frame, "Giudice invitato!");
                        else
                            JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!");

                    } else {
                        JOptionPane.showMessageDialog(null, "Operazione annullata.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            creaHackathon.addActionListener(e -> {
                FrameManager.Instance.switchFrame(new CreaHackathon(f, controller, this).$$$getRootComponent$$$());
                frame.setVisible(true);
            });

            hackathonList.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (hackathonList.getSelectedValue() != null) {
                        if (controller.getLocalRegistrazioniAperteOfHackathon(hackathonList.getSelectedValue().toString()) == true) {
                            chiudiRegistrazioni.setEnabled(true);
                        } else {
                            chiudiRegistrazioni.setEnabled(false);
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            chiudiRegistrazioni.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
                    if (JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler chiudere le registrazioni per '" + hackathonList.getSelectedValue().toString() + "'?") == 0) {
                        controller.updateRegistrazioniHackathon(false, hackathonList.getSelectedValue().toString());
                        controller.setLocalRegistrazioniAperteOfHackathon(hackathonList.getSelectedValue().toString(), false);
                        chiudiRegistrazioni.setEnabled(false);
                        JOptionPane.showMessageDialog(frame, "Registrazioni chiude per '" + hackathonList.getSelectedValue().toString() + "'");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 4));

            btnPanel.add(creaHackathon,
                    new com.intellij.uiDesigner.core.GridConstraints(
                            0, 0, 1, 1,
                            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                            null, null, null
                    )
            );

            btnPanel.add(invitaGiudice,
                    new com.intellij.uiDesigner.core.GridConstraints(
                            0, 1, 1, 1,
                            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                            null, null, null
                    )
            );

            btnPanel.add(chiudiRegistrazioni,
                    new com.intellij.uiDesigner.core.GridConstraints(
                            0, 2, 1, 1,
                            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                            null, null, null
                    )
            );
            btnPanel.add(viewClassifica, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
                    com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
                    com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
                    null, null, null));

        }
        */


    }

    public void refreshLocalUIHackathonList(ArrayList<Hackathon> hackathons)
    {
        this.removeAll();

        int i = 0;
        for(Hackathon h : hackathons)
        {
            JPanel row = createRow(h.getTitolo(), (i % 2 == 0) ? new Color(245, 245, 245) : new Color(230, 230, 230), h.getDataInizio().toString() + " - " + h.getDataFine());
            rowsViewport.add(row);
            i++;
        }
    }

    private JPanel createRow(String title, Color background, String date)
    {

        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        row.setBackground(background);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        // JPANEL testo
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JLabel subtitleLabel = new JLabel(date);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitleLabel);

        row.add(textPanel, BorderLayout.WEST);

        // JPANEL bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        // BOTTONI DI TEST -- TODO REMOVE
        JButton b = new RoundedFlatButton(Color.RED, Color.PINK);
        b.addActionListener(e -> {
            Team t = controller.getLocalCurrentUserTeam();
            if(t == null)
            {
                JOptionPane.showMessageDialog(frame, "Non stai in nessun team!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            FrameManager.Instance.switchFrame(new TeamUI(controller, frame, t.getNome(), title));
        });
        buttonPanel.add(b);
        // ---
        row.add(buttonPanel, BorderLayout.EAST);

        return row;
    }

}

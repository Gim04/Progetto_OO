package gui;

import controller.Controller;
import gui.util.FrameManager;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class HackathonList {
    private Controller controller;
    private JFrame frame;

    private JList hackathonList;
    private JPanel btnPanel;
    private JPanel root;

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

    private JScrollPane scrollPaneBar1;

    public HackathonList(ArrayList<Hackathon> hackathons, Controller c, JFrame f) {
        controller = c;
        frame = f;

        hackathonList.setModel(new DefaultListModel<String>());

        refreshLocalUIHackathonList();

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
                        FrameManager.Instance.switchFrame(new DocumentUi(controller, frame, t.getNome(), hackathonList.getSelectedValue().toString()));
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
    }

    public void refreshLocalUIHackathonList() {
        for (Hackathon h : controller.getLocalAllHackathons())
            ((DefaultListModel<String>) hackathonList.getModel()).addElement(h.getTitolo());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        btnPanel = new JPanel();
        btnPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        root.add(btnPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 32), null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        root.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        hackathonList = new JList();
        scrollPane1.setViewportView(hackathonList);
        final JLabel label1 = new JLabel();
        label1.setText("HACKATHON");
        root.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}

package gui;

import controller.Controller;
import gui.util.FrameManager;
import model.Giudice;
import model.Hackathon;
import model.Organizzatore;
import model.Team;

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

    // Pulsanti giudici
    private JButton visualizzaTeam;
    private JButton pubblicaProblema;
    //

    // Pulsanti organizzatore
    private JButton invitaGiudice;
    private JButton creaHackathon;
    private JButton chiudiRegistrazioni;
    //

    private JScrollPane scrollPaneBar1;

    public HackathonList(ArrayList<Hackathon> hackathons, Controller c, JFrame f) {
        controller = c;
        frame = f;

        hackathonList.setModel(new DefaultListModel<String>());

        refreshLocalUIHackathonList();

        if (controller.getCurrentUser() instanceof Giudice) {
            visualizzaTeam = new JButton("Visualizza Teams");
            pubblicaProblema = new JButton("Pubblica Problema");

            visualizzaTeam.addActionListener(e ->
            {
                if (hackathonList.getSelectedValue() != null) {
                    FrameManager.Instance.switchFrame(new GiudiceTeamGui(f, controller, hackathonList.getSelectedValue().toString()).$$$getRootComponent$$$());
                } else {
                    JOptionPane.showMessageDialog(frame, "Nessun hackathon selezionato!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            pubblicaProblema.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
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

            btnPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2));

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

        } else if (controller.getCurrentUser() instanceof Organizzatore) {
            invitaGiudice = new JButton("Invita Giudice");
            creaHackathon = new JButton("Crea Hackathon");
            chiudiRegistrazioni = new JButton("Chiudi Registrazioni");
            chiudiRegistrazioni.setEnabled(false);

            invitaGiudice.addActionListener(e -> {
                if (hackathonList.getSelectedValue() != null) {
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

            btnPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3));

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

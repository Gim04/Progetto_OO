package gui;


import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

import gui.custom.RoundedFlatButton;
import gui.util.FrameManager;
import model.Partecipante;


public class TeamUI extends JPanel {

    private JPanel topPanel;
    private JPanel btnPanel;
    private RoundedFlatButton invitePartecipante;
    private RoundedFlatButton addDocument;
    private JList<String> partecipanti;

    private Controller controller;
    private JFrame frame;

    private JLabel nomeTeam;

    public TeamUI(Controller c, JFrame f, String nomeTeam, String hackathon)
    {
        this.controller = c;
        this.frame = f;
        this.nomeTeam = new JLabel(nomeTeam);
        this.nomeTeam.setFont(new Font("SansSerif", Font.BOLD, 24));

        partecipanti = new JList<>();
        partecipanti.setFont(new Font("SansSerif", Font.BOLD, 16));

        refreshUILocalTable();

        topPanel = new JPanel(new BorderLayout());
        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        setLayout(new BorderLayout());

        invitePartecipante = new RoundedFlatButton(new Color(55, 241, 33), new Color(141, 255, 128), new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/add.png"))));
        addDocument = new RoundedFlatButton(new Color(0,255,255), new Color(128,211,255), new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/docs.png"))));

        invitePartecipante.addActionListener(e -> {
            if (controller.checkRegistrazioniChiuse(hackathon))
            {
                JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            final String input = JOptionPane.showInputDialog("Email:");
            if (input != null)
            {

                if (!input.contains("@") || !input.contains("."))
                {
                    JOptionPane.showMessageDialog(frame, "Mail non valida!");
                    return;
                }

                if (controller.invitePartecipanteToTeam(input, controller.getLocalCurrentUserTeam())) {
                    JOptionPane.showMessageDialog(frame, "Partecipante aggiunto al team '" + nomeTeam + "'");
                    refreshUILocalTable();
                }else
                    JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!");
            }
        });

        addDocument.addActionListener((e-> {
            FrameManager.Instance.switchFrame(new DocumentUI(controller, f, nomeTeam, hackathon));
        }));

        topPanel.add(this.nomeTeam, BorderLayout.WEST);
        topPanel.add(addDocument, BorderLayout.EAST);

        btnPanel.add(invitePartecipante);

        add(topPanel, BorderLayout.NORTH);
        add(partecipanti, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        
    }

    public void refreshUILocalTable()
    {
        String[] s = new String[controller.getLocalCurrentUserTeam().getPartecipanti().size()];
        int i = 0;
        for(Partecipante p : controller.getLocalCurrentUserTeam().getPartecipanti())
        {
            s[i] = p.getEmail();
            i++;
        }

        partecipanti.setListData(s);
        partecipanti.revalidate();
        partecipanti.repaint();
    }

}

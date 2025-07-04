package gui;


import controller.Controller;

import javax.swing.*;
import java.awt.*;

import gui.custom.RoundedFlatButton;



public class TeamUI extends JPanel {

    private JPanel panel;
    private RoundedFlatButton invitePartecipante;
    private RoundedFlatButton addDocument;

    private Controller controller;
    private Frame frame;

    private JLabel nomeTeam;


    public TeamUI(Controller c, Frame f, String nomeTeam, String hackathon)
    {
        this.controller = c;
        this.frame = f;
        this.nomeTeam = new JLabel(nomeTeam);
        this.setFont(new Font("SandSerif", Font.BOLD, 24));

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

                if (controller.invitePartecipanteToTeam(input, controller.getLocalCurrentUserTeam()))
                    JOptionPane.showMessageDialog(frame, "Partecipante aggiunto al team '" + nomeTeam + "'");
                else
                    JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!");
            }
        });
        
    }




}

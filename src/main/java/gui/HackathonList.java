package gui;

import controller.Controller;
import gui.custom.FlatScrollBarUI;
import gui.custom.RoundedFlatButton;
import gui.util.FrameManager;
import model.*;
import util.Theme;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta la lista degli hackathon.
 * Viene mostrata all'utente in base al suo ruolo (Partecipante, Giudice, Organizzatore),
 * e permette azioni contestuali come iscrizione, creazione team, pubblicazione problemi, ecc.
 */
public class HackathonList extends JPanel
{
    private Controller controller;
    private JFrame frame;

    private JPanel btnPanel;

    private JPanel rowsViewport;
    private JScrollPane scrollPane;

    /**
     * Costruttore della classe HackathonList.
     *
     * @param hackathons Lista di hackathon da mostrare.
     * @param c          Controller principale dell'applicazione.
     * @param f          Finestra principale JFrame.
     */
    public HackathonList(List<Hackathon> hackathons, Controller c, JFrame f) {
        controller = c;
        frame = f;

        this.setLayout(new BorderLayout());

        rowsViewport = new JPanel();
        rowsViewport.setLayout(new BoxLayout(rowsViewport, BoxLayout.Y_AXIS));
        rowsViewport.setBackground(Color.WHITE);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshLocalUIHackathonList(hackathons);

        scrollPane = new JScrollPane(rowsViewport);
        scrollPane.getVerticalScrollBar().setUI(new FlatScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(5);
        scrollPane.getHorizontalScrollBar().setUI(new FlatScrollBarUI());
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Aggiorna l'interfaccia della lista degli hackathon, ricreando i componenti
     * in base al ruolo dell'utente e allo stato dell'hackathon (attivo/concluso).
     *
     * @param hackathons Lista aggiornata di hackathon da mostrare.
     */
    public void refreshLocalUIHackathonList(List<Hackathon> hackathons)
    {
        rowsViewport.removeAll();
        rowsViewport.revalidate();
        rowsViewport.repaint();

        int i = 0;
        for(Hackathon h : hackathons)
        {
            List<JButton> btns = new ArrayList<>();
            List<Team> t = controller.getLocalCurrentUserTeam();

            if (controller.getCurrentUser() instanceof Partecipante)
            {
                if(Date.valueOf(LocalDate.now()).after(h.getDataFine()) || Date.valueOf(LocalDate.now()).equals(h.getDataFine()))
                {
                    RoundedFlatButton viewClassifica = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_LEADERBOARD);
                    viewClassifica.addActionListener(e -> {
                        JFrame out = new JFrame("Classifica");
                        out.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        out.setSize(240, 180);
                        out.setLocationRelativeTo(null);
                        out.setType(Window.Type.UTILITY);
                        out.setContentPane(new ClassificaUI(controller, frame, controller.calculateClassifica(h.getTitolo())));
                        out.setVisible(true);
                    });

                    btns.add(viewClassifica);
                }else{
                    if(controller.isLocalPartecipanteIscrittoAdHackathon(controller.getCurrentUser().getEmail(), h.getTitolo()))
                    {
                        if(controller.isLocalTeamInHackathon(h.getTitolo(), t) != null)
                        {
                            JButton teamBtn = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_GROUP);
                            teamBtn.addActionListener(e -> {

                                FrameManager.Instance.switchFrame(new TeamUI(controller, frame, controller.isLocalTeamInHackathon(h.getTitolo(), t).getNome(), h.getTitolo()));
                            });

                            btns.add(teamBtn);
                        }else{
                            JButton creaTeam = new RoundedFlatButton(Theme.actionColor, Theme.actionColor2, Theme.ICON_ADD);
                            creaTeam.addActionListener(e -> {
                                    if (controller.checkRegistrazioniChiuse(h.getTitolo())) {
                                        JOptionPane.showMessageDialog(frame, "Registrazioni chiuse per questo hackathon! ", "Error ", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                    final String input = JOptionPane.showInputDialog("Nome team:");
                                    if (input != null) {
                                        if (controller.createTeam(input, h.getTitolo(), controller.getCurrentUser().getEmail())) {
                                            JOptionPane.showMessageDialog(null, "Team creato!");

                                            controller.refreshHackathonList();
                                            refreshLocalUIHackathonList(controller.getLocalAllHackathons());
                                        }
                                        else
                                            JOptionPane.showMessageDialog(frame, "Non e' stato possibile creare il team!", "Error ", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Operazione annullata. ");
                                    }
                            });

                            btns.add(creaTeam);
                        }
                    }else
                    {
                        JButton iscrivitiAdHackathon = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_SUB);
                        iscrivitiAdHackathon.addActionListener(e -> {
                            if (JOptionPane.showConfirmDialog(frame, "Sei sicuro di volerti iscrivere ad '" + h.getTitolo() + "\'?") == 0)
                            {
                                controller.iscriviPartecipanteAdHackathon(h, controller.getCurrentUser().getEmail());
                                controller.refreshHackathonList();
                                refreshLocalUIHackathonList(controller.getLocalAllHackathons());
                            }
                        });

                        btns.add(iscrivitiAdHackathon);
                    }
                }

                JPanel row = createRow(h.getTitolo(), (i % 2 == 0) ? Theme.gray : Theme.gray2, h.getDataInizio().toString() + " - " + h.getDataFine(), btns);
                rowsViewport.add(row);
            }
            else if(controller.getCurrentUser() instanceof Giudice)
            {
                if(Date.valueOf(LocalDate.now()).after(h.getDataFine()) || Date.valueOf(LocalDate.now()).equals(h.getDataFine()))
                {
                    RoundedFlatButton viewClassifica = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_LEADERBOARD);
                    viewClassifica.addActionListener(e -> {
                        JFrame out = new JFrame("Classifica");
                        out.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        out.setSize(240, 180);
                        out.setLocationRelativeTo(null);
                        out.setType(Window.Type.UTILITY);
                        out.setContentPane(new ClassificaUI(controller, frame, controller.calculateClassifica(h.getTitolo())));
                        out.setVisible(true);
                    });

                    btns.add(viewClassifica);
                }else {
                    JButton visualizzaTeam = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_GROUP);
                    JButton pubblicaProblema = new RoundedFlatButton(Theme.actionColor, Theme.actionColor, Theme.ICON_ADD);

                    visualizzaTeam.addActionListener(e ->
                    {
                        if (controller.checkRegistrazioniChiuse(h.getTitolo())) {
                            JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", " Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        FrameManager.Instance.switchFrame(new GiudiceTeamGui(frame, controller, h.getTitolo()));
                    });

                    pubblicaProblema.addActionListener(e -> {
                        if (controller.checkRegistrazioniChiuse(h.getTitolo())) {
                            JOptionPane.showMessageDialog(frame, "Registrazioni chiude per questo hackathon!", " Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        final String input = JOptionPane.showInputDialog("Problema:");
                        if (input != null) {
                            if (controller.setDescrizioneProblema(h.getTitolo(), input))
                                JOptionPane.showMessageDialog(null, "Descrizione problema assegnata!");
                            else
                                JOptionPane.showMessageDialog(frame, "Non e' stato possibile impostare la descrizione del problema!", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Operazione annullata.");
                        }
                    });

                    btns.add(visualizzaTeam);
                    btns.add(pubblicaProblema);
                }
                JPanel row = createRow(h.getTitolo(), (i % 2 == 0) ? Theme.gray : Theme.gray2, h.getDataInizio().toString() + " - " + h.getDataFine(), btns);
                rowsViewport.add(row);
            }
            else if(controller.getCurrentUser() instanceof Organizzatore)
            {
                if(Date.valueOf(LocalDate.now()).after(h.getDataFine()) || Date.valueOf(LocalDate.now()).equals(h.getDataFine()))
                {
                    RoundedFlatButton viewClassifica = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_LEADERBOARD);
                    viewClassifica.addActionListener(e -> {
                        JFrame out = new JFrame("Classifica");
                        out.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        out.setSize(240, 180);
                        out.setLocationRelativeTo(null);
                        out.setType(Window.Type.UTILITY);
                        out.setContentPane(new ClassificaUI(controller, frame, controller.calculateClassifica(h.getTitolo())));
                        out.setVisible(true);
                    });

                    btns.add(viewClassifica);
                }else {
                    JButton invitaGiudice = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_ADD);

                    JButton apriRegistrazioni = new RoundedFlatButton(Theme.actionColor, Theme.actionColor2, Theme.ICON_UNLOCK);
                    if (controller.getLocalRegistrazioniAperteOfHackathon(h.getTitolo())) {
                        apriRegistrazioni.setEnabled(false);
                    } else {
                        apriRegistrazioni.setEnabled(true);
                    }

                    invitaGiudice.addActionListener(e -> {
                        if (controller.checkRegistrazioniChiuse(h.getTitolo())) {
                            JOptionPane.showMessageDialog(frame, "Registrazioni chiuse per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        final String input = JOptionPane.showInputDialog("Email:");
                        if (input != null) {

                            if (!input.contains("@") || !input.contains(".")) {
                                JOptionPane.showMessageDialog(frame, "Mail non valida!");
                                return;
                            }

                            if (controller.inviteJudgeToHackathon(input, h.getTitolo()))
                                JOptionPane.showMessageDialog(frame, "Giudice invitato!");
                            else
                                JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!");

                        } else {
                            JOptionPane.showMessageDialog(null, "Operazione annullata.");
                        }
                    });

                    apriRegistrazioni.addActionListener(e -> {
                        if (JOptionPane.showConfirmDialog(frame, "Sei sicuro di voler aprire le registrazioni per '" + h.getTitolo() + "'?") == 0) {
                            controller.updateRegistrazioniHackathon(true, h.getTitolo());
                            controller.setLocalRegistrazioniAperteOfHackathon(h.getTitolo(), true);
                            apriRegistrazioni.setEnabled(false);
                            JOptionPane.showMessageDialog(frame, "Registrazioni aperte per '" + h.getTitolo() + "'");
                        }
                    });

                    btns.add(apriRegistrazioni);
                    btns.add(invitaGiudice);
                }

                JPanel row = createRow(h.getTitolo(), (i % 2 == 0) ? Theme.gray : Theme.gray2, h.getDataInizio().toString() + " - " + h.getDataFine(), btns);
                rowsViewport.add(row);
            }
            i++;
        }

        if(controller.getCurrentUser() instanceof Organizzatore)
        {
            // Pulsanti statici dell'organizzatore
            JButton creaHackathon = new RoundedFlatButton(Theme.actionColor, Theme.actionColor2, Theme.ICON_ADD);
            creaHackathon.addActionListener(e -> {
                FrameManager.Instance.switchFrame(new CreaHackathon(frame, controller));
                frame.setVisible(true);
            });

            btnPanel.add(creaHackathon);
            add(btnPanel, BorderLayout.NORTH);
        }
    }

    /**
     * Crea un pannello che rappresenta una riga della lista hackathon con titolo, data e bottoni di azione.
     *
     * @param title      Titolo dell'hackathon.
     * @param background Colore di sfondo della riga.
     * @param date       Intervallo di date (inizio-fine).
     * @param buttons    Lista di bottoni da associare alla riga.
     * @return JPanel costruito.
     */
    private JPanel createRow(String title, Color background, String date, List<JButton> buttons)
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
        titleLabel.setFont(Theme.hackathon_list_title);

        JLabel subtitleLabel = new JLabel(date);
        subtitleLabel.setFont(Theme.hackathon_list_p);
        subtitleLabel.setForeground(Color.GRAY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(subtitleLabel);

        row.add(textPanel, BorderLayout.WEST);

        // JPANEL bottoni
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        for(JButton button : buttons)
        {
            buttonPanel.add(button);
        }
        // ---
        row.add(buttonPanel, BorderLayout.EAST);
        return row;
    }

}

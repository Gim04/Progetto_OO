package gui;


import controller.Controller;

import javax.swing.*;
import java.awt.*;

import gui.custom.RoundedFlatButton;
import gui.util.FrameManager;
import model.Partecipante;
import model.Team;
import util.Theme;

/**
 * Interfaccia utente che mostra i dettagli di un team, i partecipanti associati e permette
 * operazioni come l'invito di nuovi membri e la gestione dei documenti.
 */
public class TeamUI extends JPanel {

    private JPanel topPanel;
    private JPanel btnPanel;
    private RoundedFlatButton invitePartecipante;
    private RoundedFlatButton addDocument;
    private RoundedFlatButton backButton;

    private JList<String> partecipanti;

    private Controller controller;
    private JFrame frame;

    private JLabel nomeTeam;

    private Team currentTeam;

    private JLabel descrizioneProblema;

    /**
     * Costruttore principale per l'interfaccia del team.
     *
     * @param c         Controller dell'applicazione.
     * @param f         Finestra principale JFrame.
     * @param nomeTeam  Nome del team selezionato.
     * @param hackathon Titolo dell'hackathon a cui è associato il team.
     */
    public TeamUI(Controller c, JFrame f, String nomeTeam, String hackathon)
    {
        this.controller = c;
        this.currentTeam = controller.isLocalTeamInHackathon(hackathon, controller.getLocalCurrentUserTeam());
        this.frame = f;
        this.nomeTeam = new JLabel(nomeTeam);
        this.nomeTeam.setFont(Theme.header);

        this.descrizioneProblema = new JLabel(hackathon + " - " + controller.getLocalDescrizioneProblema(hackathon));
        this.descrizioneProblema.setFont(Theme.paragraph);
        this.descrizioneProblema.setForeground(Color.DARK_GRAY);

        JPanel teamInfoPanel = new JPanel();
        teamInfoPanel.setLayout(new BoxLayout(teamInfoPanel, BoxLayout.Y_AXIS));
        teamInfoPanel.add(this.nomeTeam);
        teamInfoPanel.add(this.descrizioneProblema);

        partecipanti = new JList<>();
        partecipanti.setFont(Theme.hackathon_list_title);

        refreshUILocalTable();

        topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        setLayout(new BorderLayout());

        invitePartecipante = new RoundedFlatButton(Theme.actionColor, Theme.actionColor2, Theme.ICON_PERSON_ADD);
        addDocument = new RoundedFlatButton(Theme.secondaryColor, Theme.secondaryColor2, Theme.ICON_DOCS);
        backButton = new RoundedFlatButton(Theme.backColor, Theme.backColor2, Theme.ICON_UNDO);

        backButton.addActionListener(e -> {
            FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame));
        });

        invitePartecipante.addActionListener(e -> {
            if (controller.checkRegistrazioniChiuse(hackathon))
            {
                JOptionPane.showMessageDialog(frame, "Registrazioni chiuse per questo hackathon!", "Error", JOptionPane.ERROR_MESSAGE);
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

                if (controller.invitePartecipanteToTeam(input, currentTeam, hackathon)) {
                    JOptionPane.showMessageDialog(frame, "Partecipante aggiunto al team '" + nomeTeam + "'");
                    refreshUILocalTable();
                }else
                    JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!");
            }
        });

        addDocument.addActionListener((e-> {
            FrameManager.Instance.switchFrame(new DocumentUI(controller, f, nomeTeam, hackathon));
        }));


        // Colonna 0: Team info (nome + descrizione)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        topPanel.add(teamInfoPanel, gbc);

        // Colonna 1: Bottone addDocument (in alto a destra)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        addDocument.setPreferredSize(new Dimension(32, 32));
        addDocument.setMinimumSize(new Dimension(32, 32));
        addDocument.setMaximumSize(new Dimension(32, 32));
        topPanel.add(addDocument, gbc);


        // Colonna 2: Bottone backButton (in alto a destra)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        addDocument.setPreferredSize(new Dimension(32, 32));
        addDocument.setMinimumSize(new Dimension(32, 32));
        addDocument.setMaximumSize(new Dimension(32, 32));
        topPanel.add(backButton, gbc);

        btnPanel.add(invitePartecipante);

        add(topPanel, BorderLayout.NORTH);
        add(partecipanti, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        
    }

    /**
     * Metodo per aggiornare dinamicamente la lista dei partecipanti nel team.
     * Ricarica i dati dal team corrente.
     */
    public void refreshUILocalTable()
    {
        String[] s = new String[currentTeam.getPartecipanti().size()];
        int i = 0;
        for(Partecipante p : currentTeam.getPartecipanti())
        {
            s[i] = p.getEmail();
            i++;
        }

        partecipanti.setListData(s);
        partecipanti.revalidate();
        partecipanti.repaint();
    }

}

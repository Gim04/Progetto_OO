package gui;

import controller.Controller;
import gui.custom.FlatButton;
import gui.custom.FlatCheckBox;
import gui.custom.FlatTextField;
import gui.util.FrameManager;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CreaHackathon extends JPanel
{
    private FlatTextField nome;
    private FlatTextField dimensione;
    private FlatTextField maxIscritti;
    private FlatTextField dataInizio;
    private FlatTextField dataFine;
    private FlatCheckBox registrazioni;

    private JLabel nomeLabel;
    private JLabel dimensioneLabel;
    private JLabel maxIscrittiLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JLabel registrazioniLabel;

    private JLabel formLabel;

    private FlatButton aggiungiHackathonButton;

    private JPanel form;

    private JFrame frame;
    private Controller controller;

    public CreaHackathon(JFrame frame, Controller controller) {
        this.controller = controller;
        this.frame = frame;

        frame.setTitle("Login");

        setLayout(new GridBagLayout());
        setBackground(new Color(230, 230, 230));
        setBorder(new CompoundBorder(
                new LineBorder(new Color(200,200,200), 1, true),
                new EmptyBorder(20,20,20,20)
        ));

        form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBackground(Color.WHITE);

        nome        = new FlatTextField();
        dimensione  = new FlatTextField();
        maxIscritti = new FlatTextField();
        dataInizio  = new FlatTextField();
        dataFine    = new FlatTextField();
        registrazioni = new FlatCheckBox();

        nomeLabel = new JLabel("Nome hackathon:");
        nomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        dimensioneLabel = new JLabel("Dimensione team:");
        dimensioneLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        maxIscrittiLabel = new JLabel("Numero massimo di iscritti:");
        maxIscrittiLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        dataInizioLabel = new JLabel("Data inizio:");
        dataInizioLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        dataFineLabel = new JLabel("Data fine:");
        dataFineLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        registrazioniLabel = new JLabel("Registrazioni Aperte:");
        registrazioniLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        formLabel = new JLabel("Crea Hackathon");
        formLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        aggiungiHackathonButton = new FlatButton();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.3;

        // FORM TITLE
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formLabel.setFont(new Font("SandSerif", Font.BOLD, 24));
        formLabel.setForeground(Color.DARK_GRAY);
        form.add(formLabel, gbc);

        // Riga 0 - Nome
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(nomeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(nome, gbc);

        // Riga 1 - Dimensione
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        form.add(dimensioneLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(dimensione, gbc);

        // Riga 2 - Max iscritti
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        form.add(maxIscrittiLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(maxIscritti, gbc);

        // Riga 3 - Data inizio
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        form.add(dataInizioLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(dataInizio, gbc);

        // Riga 4 - Data fine
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        form.add(dataFineLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(dataFine, gbc);

        // Riga 5 - Registrazioni
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        form.add(registrazioniLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(registrazioni, gbc);

        // Riga 6 - Pulsante Crea Hackathon
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        form.add(aggiungiHackathonButton, gbc);

        aggiungiHackathonButton.addActionListener(e -> {

            LocalDate dateI = isValidData(dataInizio.getText());
            LocalDate dateF = isValidData(dataFine.getText());

            if (dateI == null || dateF == null) {
                JOptionPane.showMessageDialog(null, "Formato data non valido!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maxIscrittiValue = -1;
            try {
                maxIscrittiValue = Integer.parseInt(maxIscritti.getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Formato numero iscritti non valido!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int dimensioneValue = -1;
            try {
                dimensioneValue = Integer.parseInt(dimensione.getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Formato dimensione teams non valido!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!controller.aggiungiHackathon(nome.getText(), dimensioneValue, maxIscrittiValue, dateI, dateF, registrazioni.isSelected(), controller.getCurrentUser().getEmail()))
            {
                JOptionPane.showMessageDialog(frame, "Si e' verificato un errore!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            controller.refreshHackathonListForOrganizzatore();

            FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame));
        });

        aggiungiHackathonButton.setText("Crea Hackathon");

        add(form);
    }

    private LocalDate isValidData(String data) {
        if (data.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$")) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = LocalDate.parse(data, formatter);
                return date;
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}

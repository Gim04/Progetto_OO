package gui;

import controller.Controller;
import gui.custom.FlatButton;
import gui.custom.FlatCheckBox;
import gui.custom.FlatTextField;
import gui.util.FrameManager;
import model.Sede;
import util.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Pannello per la creazione di un nuovo Hackathon.
 * Contiene un form con vari campi di testo, checkbox, combo box e un pulsante di submit.
 */
public class CreaHackathon extends JPanel
{
    // Campi del form
    private FlatTextField nome;
    private FlatTextField dimensione;
    private FlatTextField maxIscritti;
    private FlatTextField dataInizio;
    private FlatTextField dataFine;
    private FlatCheckBox registrazioni;
    private JComboBox<String> sedeComboBox;

    // Label per i campi
    private JLabel nomeLabel;
    private JLabel dimensioneLabel;
    private JLabel maxIscrittiLabel;
    private JLabel dataInizioLabel;
    private JLabel dataFineLabel;
    private JLabel registrazioniLabel;
    private JLabel sedeLabel;

    private JLabel formLabel;

    private FlatButton aggiungiHackathonButton;

    private JPanel form;

    private Controller controller;

    /**
     * Costruttore che crea il pannello form per la creazione di un hackathon.
     *
     * @param frame      il JFrame contenitore principale (usato per titolo e cambio frame)
     * @param controller l'istanza del controller per gestire la logica
     */
    public CreaHackathon(JFrame frame, Controller controller) {
        this.controller = controller;

        // Imposta titolo frame
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

        // Inizializza i campi di input
        nome        = new FlatTextField();
        dimensione  = new FlatTextField();
        maxIscritti = new FlatTextField();
        dataInizio  = new FlatTextField();
        dataFine    = new FlatTextField();
        registrazioni = new FlatCheckBox();

        // Ottieni lista sedi da controller e prepara combo box
        List<String> sedi = controller.getSedi();
        String[] sediArr = new String[sedi.size()];
        for(int i = 0; i < sedi.size(); i++)
            sediArr[i] = sedi.get(i);
        sedeComboBox = createFlatJComboxBox(sediArr);

        // Label e font coerente
        nomeLabel = new JLabel("Nome hackathon:");
        nomeLabel.setFont(Theme.paragraph);

        dimensioneLabel = new JLabel("Dimensione team:");
        dimensioneLabel.setFont(Theme.paragraph);

        maxIscrittiLabel = new JLabel("Numero massimo di iscritti:");
        maxIscrittiLabel.setFont(Theme.paragraph);

        dataInizioLabel = new JLabel("Data inizio:");
        dataInizioLabel.setFont(Theme.paragraph);

        dataFineLabel = new JLabel("Data fine:");
        dataFineLabel.setFont(Theme.paragraph);

        registrazioniLabel = new JLabel("Registrazioni Aperte:");
        registrazioniLabel.setFont(Theme.paragraph);

        sedeLabel = new JLabel("Sede:");
        sedeLabel.setFont(Theme.paragraph);

        formLabel = new JLabel("Crea Hackathon");
        formLabel.setFont(Theme.header);

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
        formLabel.setFont(Theme.header);
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

        // Riga 6 - Combobox
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        form.add(sedeLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        form.add(sedeComboBox, gbc);

        // Riga 7 - Pulsante Crea Hackathon
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        form.add(aggiungiHackathonButton, gbc);

        aggiungiHackathonButton.addActionListener(e -> {

            LocalDate dateI = isValidData(dataInizio.getText());
            LocalDate dateF = isValidData(dataFine.getText());

            if (dateI == null || dateF == null) {
                JOptionPane.showMessageDialog(null, "Formato data non valido!", "Error ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int maxIscrittiValue = -1;
            try {
                maxIscrittiValue = Integer.parseInt(maxIscritti.getText());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Formato numero iscritti non valido!", "Error ", JOptionPane.ERROR_MESSAGE);
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

            String[] decomposed = ((String)sedeComboBox.getSelectedItem()).split(",");
            Sede decomposedSede = new Sede(decomposed[1], decomposed[0], Integer.parseInt(decomposed[2]));
            if(!controller.aggiungiHackathon(nome.getText(), dimensioneValue, maxIscrittiValue, dateI, dateF, registrazioni.isSelected(), controller.getCurrentUser().getEmail(), decomposedSede))
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

    /**
     * Controlla se la stringa fornita rappresenta una data valida nel formato "dd/MM/yyyy".
     *
     * @param data Stringa data da validare.
     * @return La data convertita come LocalDate o null se non valida.
     */
    private LocalDate isValidData(String data) {
        if (data.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$")) {
            try {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(data, formatter);
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Crea una JComboBox personalizzata con stile piatto e font coerente con il tema.
     *
     * @param items Array di stringhe da inserire nella combo box.
     * @return JComboBox stilizzata.
     */
    private JComboBox<String> createFlatJComboxBox(String[] items)
    {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(Theme.paragraph);
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.DARK_GRAY);
        comboBox.setFocusable(false);

        return comboBox;

    }

}
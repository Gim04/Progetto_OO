package gui;

import controller.Controller;
import gui.custom.FlatButton;
import gui.custom.FlatTextField;
import gui.util.FrameManager;
import util.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pannello di registrazione che permette all'utente di creare un nuovo account inserendo nome, cognome,
 * email, password e ruolo.
 * Include validazioni base per i campi e consente di tornare al pannello di login.
 */
public class Register extends JPanel
{
    private Controller controller;

    private JLabel formLabel;
    private JPanel form;

    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel surnameLabel;
    private JTextField surnameField;

    private JLabel emailLabel;
    private JTextField emailField;

    private JLabel passwordLabel;
    private JPasswordField passwordField;

    private JLabel ruoloLabel;
    private JComboBox<String> ruoloComboBox;

    private JButton registerButton;

    /**
     * Costruttore del pannello di registrazione.
     *
     * @param c     Controller dell'applicazione che gestisce la logica.
     * @param frame JFrame padre in cui il pannello viene inserito.
     */
    public Register(Controller c, JFrame frame) {
        controller = c;

        frame.setTitle("Register ");

        setLayout(new GridBagLayout());
        setBackground(new Color(230, 230, 230));
        setBorder(new CompoundBorder(
                new LineBorder(new Color(200,200,200), 1, true),
                new EmptyBorder(20,20,20,20)
        ));

        form = new JPanel();
        form.setLayout(new GridBagLayout());

        formLabel = new JLabel("Register");
        nameLabel = new JLabel("Nome:");
        surnameLabel = new JLabel("Cognome:");
        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");
        ruoloLabel = new JLabel("Ruolo:");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 25, 15, 25);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // FORM LABEL
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formLabel.setFont(Theme.header);
        formLabel.setForeground(Color.DARK_GRAY);
        form.add(formLabel, gbc);

        // NAME LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        nameLabel.setFont(Theme.paragraph);
        form.add(nameLabel, gbc);

        // NAME FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        nameField = new FlatTextField();
        form.add(nameField, gbc);

        // SURNAME LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        surnameLabel.setFont(Theme.paragraph);
        form.add(surnameLabel, gbc);

        // SURNAME FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        surnameField = new FlatTextField();
        surnameField.setFont(Theme.paragraph);
        form.add(surnameField, gbc);

        // EMAIL LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        emailLabel.setFont(Theme.paragraph);
        form.add(emailLabel, gbc);

        // EMAIL FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailField = new FlatTextField();
        emailField.setFont(Theme.paragraph);
        form.add(emailField, gbc);

        // PASSWORD LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        passwordLabel.setFont(Theme.paragraph);
        form.add(passwordLabel, gbc);

        // PASSWORD FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = createFlatPasswordField();
        passwordField.setFont(Theme.paragraph);
        form.add(passwordField, gbc);

        // COMBOBOX LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        ruoloLabel.setFont(Theme.paragraph);
        form.add(ruoloLabel, gbc);

        // COMBOBOX FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        ruoloComboBox = createFlatJComboxBox(new String[]{"Partecipante", "Giudice", "Organizzatore"});
        form.add(ruoloComboBox, gbc);

        // BUTTON
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerButton = new FlatButton("Register");
        form.add(registerButton, gbc);

        // REGISTER LINK
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel loginLabel = new JLabel("<html>Hai un account? <a href='#'>Accedi Ora!</a></html>");
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.setFont(Theme.hackathon_list_p);
        loginLabel.setForeground(Color.GRAY);

        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FrameManager.Instance.switchFrame(new Login(controller, frame));
            }
        });

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(Color.WHITE);
        linkPanel.add(loginLabel);

        registerButton.addActionListener(e-> {
            if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getPassword().length == 0 || ruoloComboBox.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(frame, "Tutti i campi sono obbligatori", "Error ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (passwordField.getPassword().length < 8) {
                JOptionPane.showMessageDialog(frame, "La password deve essere almeno di 8 caratteri", "Error ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                JOptionPane.showMessageDialog(frame, "La mail deve essere in un formato: example@domain.com", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String result = controller.registerUser(nameField.getText(), surnameField.getText(), emailField.getText(), passwordField.getText(), ruoloComboBox.getItemAt(ruoloComboBox.getSelectedIndex()).toString());
            if (result != null) {
                JOptionPane.showMessageDialog(frame, result, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Registrazione effettuata!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

        });

        form.add(linkPanel, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        add(form, gbc);
    }

    /**
     * Crea un campo password con stile flat coerente con il tema dell'applicazione.
     *
     * @return JPasswordField configurato.
     */
    private JPasswordField createFlatPasswordField()
    {
        JPasswordField field = new JPasswordField(25);
        field.setFont(Theme.paragraph);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200),1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.DARK_GRAY);
        field.setCaretColor(Color.DARK_GRAY);
        return field;
    }

    /**
     * Crea una JComboBox con stile flat coerente con il tema dell'applicazione.
     *
     * @param items Array di stringhe da inserire nella combo box.
     * @return JComboBox configurata.
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

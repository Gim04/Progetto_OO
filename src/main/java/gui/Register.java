package gui;

import controller.Controller;
import gui.util.FrameManager;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Register extends JPanel
{
    private Controller controller;
    private JFrame frame;

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

    public Register(Controller c, JFrame frame) {
        controller = c;
        this.frame = frame;

        frame.setTitle("Register");

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
        formLabel.setFont(new Font("SandSerif", Font.BOLD, 24));
        formLabel.setForeground(Color.DARK_GRAY);
        form.add(formLabel, gbc);

        // NAME LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(nameLabel, gbc);

        // NAME FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        nameField = createFlatTextField();
        form.add(nameField, gbc);

        // SURNAME LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        surnameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(surnameLabel, gbc);

        // SURNAME FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        surnameField = createFlatTextField();
        surnameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(surnameField, gbc);

        // EMAIL LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(emailLabel, gbc);

        // EMAIL FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailField = createFlatTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(emailField, gbc);

        // PASSWORD LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(passwordLabel, gbc);

        // PASSWORD FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        passwordField = createFlatPasswordField();
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(passwordField, gbc);

        // COMBOBOX LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        ruoloLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
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
        registerButton = createFlatButton("Register");
        form.add(registerButton, gbc);

        // REGISTER LINK
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel loginLabel = new JLabel("<html>Hai un account? <a href='#'>Accedi Ora!</a></html>");
        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
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

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText().isEmpty() || surnameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getPassword().length == 0 || ruoloComboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(frame, "Tutti i campi sono obbligatori", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (passwordField.getPassword().length < 8) {
                    JOptionPane.showMessageDialog(frame, "La password deve essere almeno di 8 caratteri", "Error", JOptionPane.ERROR_MESSAGE);
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
            }
        });

        form.add(linkPanel, gbc);

        gbc.insets = new Insets(0, 0, 0, 0);
        add(form, gbc);
    }

    private JTextField createFlatTextField()
    {
        JTextField field = new JTextField(25);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200),1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.DARK_GRAY);
        field.setCaretColor(Color.DARK_GRAY);
        return field;
    }

    private JPasswordField createFlatPasswordField()
    {
        JPasswordField field = new JPasswordField(25);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200),1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.DARK_GRAY);
        field.setCaretColor(Color.DARK_GRAY);
        return field;
    }

    private JButton createFlatButton(String text)
    {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new Color(70,130,180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private JComboBox<String> createFlatJComboxBox(String[] items)
    {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.DARK_GRAY);
        comboBox.setFocusable(false);

        return comboBox;

    }
}

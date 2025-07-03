package gui;

import controller.Controller;
import gui.util.FrameManager;
import model.Giudice;
import model.Organizzatore;
import model.Partecipante;
import model.Utente;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class Login extends JPanel
{
    private JFrame frame;

    private Controller controller;

    private JPanel form;

    private JLabel formLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;

    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login(Controller c, JFrame frame) {
        controller = c;
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

        emailTextField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        formLabel = new JLabel("Login");

        emailLabel = new JLabel("Email:");
        passwordLabel = new JLabel("Password:");

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

        // EMAIL LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        form.add(emailLabel, gbc);

        // EMAIL FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailTextField = createFlatTextField();
        form.add(emailTextField, gbc);

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
        form.add(passwordField, gbc);

        // BUTTON
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = createFlatButton("Login");
        form.add(loginButton, gbc);

        // REGISTER LINK
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel registerLabel = new JLabel("<html>Non hai un account? <a href='#'>Registrati Ora!</a></html>");
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        registerLabel.setForeground(Color.GRAY);

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                FrameManager.Instance.switchFrame(new Register(controller, frame));
            }
        });

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        linkPanel.setBackground(Color.WHITE);
        linkPanel.add(registerLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (emailTextField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                    JOptionPane.showMessageDialog(form, "Email o password non validi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Utente utente = controller.logUser(emailTextField.getText(), new String(passwordField.getPassword()));

                if (utente == null) {
                    JOptionPane.showMessageDialog(form, "Email o password errati!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                controller.setCurrentUser(utente);
                frame.setTitle("Hackathon List");

                if (utente instanceof Partecipante) {
                    controller.refreshHackathonList();
                    FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame).$$$getRootComponent$$$());
                    return;
                } else if (utente instanceof Giudice) {
                    controller.refreshHackathonListForGiudice();
                    FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame).$$$getRootComponent$$$());
                    return;
                } else if (utente instanceof Organizzatore) {
                    controller.refreshHackathonListForOrganizzatore();
                    FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame).$$$getRootComponent$$$());
                    return;
                }

                JOptionPane.showMessageDialog(form, "ERRORE INASPETTATO!", "ERR", JOptionPane.ERROR_MESSAGE);
            }
        });

        form.add(linkPanel, gbc);
        add(form);
    }

    private JTextField createFlatTextField()
    {
        JTextField field = new JTextField(15);
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
        JPasswordField field = new JPasswordField(15);
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
}

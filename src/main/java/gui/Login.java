package gui;

import controller.Controller;
import gui.custom.FlatButton;
import gui.custom.FlatTextField;
import gui.util.FrameManager;
import model.Giudice;
import model.Organizzatore;
import model.Partecipante;
import model.Utente;
import util.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Pannello di login che consente all'utente di inserire email e password per autenticarsi.
 * Gestisce la validazione dei campi e la navigazione verso il pannello di registrazione o della lista hackathon.
 */
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

    /**
     * Costruttore del pannello di login.
     *
     * @param c     Controller dell'applicazione che gestisce la logica.
     * @param frame JFrame padre in cui il pannello è inserito.
     */
    public Login(Controller c, JFrame frame) {
        controller = c;
        this.frame = frame;

        frame.setTitle("Login ");

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
        loginButton = new JButton("Login ");
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
        formLabel.setFont(Theme.header);
        formLabel.setForeground(Color.DARK_GRAY);
        form.add(formLabel, gbc);

        // EMAIL LABEL
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        emailLabel.setFont(Theme.paragraph);
        form.add(emailLabel, gbc);

        // EMAIL FIELD
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        emailTextField = new FlatTextField();
        form.add(emailTextField, gbc);

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
        form.add(passwordField, gbc);

        // BUTTON
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton = new FlatButton("Login");
        form.add(loginButton, gbc);

        // REGISTER LINK
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel registerLabel = new JLabel("<html>Non hai un account? <a href='#'>Registrati Ora!</a></html>");
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setFont(Theme.hackathon_list_p);
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

        loginButton.addActionListener(e-> {
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
                controller.updateLocalPartecipanti();
                controller.refreshHackathonList();
                FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame));
                return;
            } else if (utente instanceof Giudice) {
                controller.updateLocalPartecipanti();
                controller.refreshHackathonListForGiudice();
                FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame));
                return;
            } else if (utente instanceof Organizzatore) {
                controller.updateLocalPartecipanti();
                controller.refreshHackathonListForOrganizzatore();
                FrameManager.Instance.switchFrame(new HackathonList(controller.getLocalAllHackathons(), controller, frame));
                return;
            }

            JOptionPane.showMessageDialog(form, "ERRORE INASPETTATO!", "ERR", JOptionPane.ERROR_MESSAGE);

        });

        form.add(linkPanel, gbc);
        add(form);
    }

    /**
     * Crea un campo password personalizzato con stile flat.
     *
     * @return un JPasswordField configurato con font, colori e bordi personalizzati.
     */
    private JPasswordField createFlatPasswordField()
    {
        JPasswordField field = new JPasswordField(15);
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
}

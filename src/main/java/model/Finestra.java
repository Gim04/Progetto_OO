package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Finestra extends JFrame implements ActionListener
{
    JButton button;
    public Finestra()
    {
        super();
        BoxLayout layout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        setVisible(true);
        setSize(500,500);
        setTitle("Progettone OO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.blue);
        setLayout(new BorderLayout());

        Panel panel1 = new Panel();
        Panel panel2 = new Panel();
        Panel panel3 = new Panel();
        Panel panel4 = new Panel();
        Panel panel5 = new Panel();

        panel1.setBackground(Color.red);
        panel2.setBackground(Color.green);
        panel3.setBackground(Color.yellow);
        panel4.setBackground(Color.magenta);
        panel5.setBackground(Color.blue);

        panel1.setPreferredSize(new Dimension(100,100));
        panel2.setPreferredSize(new Dimension(100,100));
        panel3.setPreferredSize(new Dimension(100,100));
        panel4.setPreferredSize(new Dimension(100,100));
        panel5.setPreferredSize(new Dimension(100,100));

        button = new JButton("free v-bucks");
        button.setSize(200,100);
        button.addActionListener(this);

        panel5.add(button);
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.WEST);
        add(panel3, BorderLayout.SOUTH);
        add(panel4, BorderLayout.EAST);
        add(panel5, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == button)
        {
            JOptionPane.showMessageDialog(this, "free v-bucks!", "Offerta speciale!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

package app;

import gui.util.FrameManager;

import javax.swing.*;

import gui.Login;
import util.EDatabaseType;

/**
 * The type Main.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(980, 740);
        frame.setTitle("Login");

        FrameManager frameManager = new FrameManager(frame, EDatabaseType.POSTGRESQL);
        frameManager.switchFrame(new Login(frameManager.getController(), frame));
        frame.setVisible(true);
    }
}
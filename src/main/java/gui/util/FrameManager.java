package gui.util;

import Database.ConnessioneDatabase;
import controller.Controller;

import javax.swing.*;

public class FrameManager
{

    public static FrameManager Instance = null;

    JFrame frame = null;
    JComponent rootPanel = null;

    Controller controller = null;
    ConnessioneDatabase connessioneDatabase = null;

    public FrameManager(JFrame frame)
    {
        if(Instance != null)
        {
            throw new IllegalStateException("FrameManager has already been initialized");
        }

        Instance = this;
        this.frame = frame;

        connessioneDatabase = new ConnessioneDatabase();
        controller = new Controller();
    }

    public void switchFrame(JComponent next)
    {
        rootPanel = next;

        frame.setContentPane(rootPanel);
        frame.revalidate();
        frame.repaint();

        System.gc();
    }

    public JFrame getWindow()
    {
        return frame;
    }

    public JComponent getRootPanel()
    {
        return rootPanel;
    }

    public Controller getController()
    {
        return controller;
    }
}

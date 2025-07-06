package ui;

import javax.swing.*;

public class GameWindow extends JFrame {
    private static GameWindow instance;
    private JPanel currentPanel;

    public static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;
    }

    private GameWindow() {
        setTitle("Memorizing Card");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        showMenu();
        setVisible(true);
    }

    public void setPanel(JPanel panel) {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = panel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showMenu() {
        setPanel(new MenuPanel());
    }

    public void showGame(int mode, int difficulty) {
        setPanel(new GamePanel(mode, difficulty));
    }
}
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

    public GameWindow() {
        setTitle("Memorizing Card");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        showMenu();
    }

    public void setPanel(JPanel panel) {
        if (currentPanel != null) remove(currentPanel);
        currentPanel = panel;
        add(currentPanel);
        revalidate();
        repaint();
    }

    public void showMenu() {
        setPanel(new MenuPanel());
    }

    public void showGamePanel1Player(int difficulty) {
        setPanel(new GamePanel(1, difficulty));
    }

    public void showGamePanel2Player() {
        setPanel(new GamePanel(2, 0)); // difficulty nggak dipakai di 2P
    }
}

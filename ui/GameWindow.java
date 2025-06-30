package ui;

import javax.swing.*;

public class GameWindow extends JFrame {
    private static GameWindow instance;

    public GameWindow() {
        instance = this;

        setTitle("Memorizing Card - Kesya");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        showMenu(); // awal tampil menu
        setVisible(true);
    }

    public static GameWindow getInstance() {
        return instance;
    }

    public void showMenu() {
        setContentPane(new MenuPanel());
        revalidate();
        repaint();
    }

    public void showGamePanel1Player() {
        setContentPane(new GamePanel(1)); // mode 1 player
        revalidate();
        repaint();
    }

    public void showGamePanel2Player() {
        setContentPane(new GamePanel(2)); // mode 2 player
        revalidate();
        repaint();
    }
}

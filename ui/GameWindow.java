package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // --- Logika untuk membuat game menjadi Full Screen ---
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();

        setUndecorated(true); // Menghilangkan border dan title bar
        device.setFullScreenWindow(this); // Mengatur jendela menjadi full screen
        // --- Akhir dari Logika Full Screen ---

        // Listener untuk keluar dari full screen dengan tombol Escape
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    device.setFullScreenWindow(null); // Keluar dari mode full screen
                    System.exit(0); // Menutup aplikasi
                }
            }
        });

        getContentPane().setBackground(Color.decode("#ADD8E6"));
        showPlayPanel();
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

    public void showPlayPanel() {
        setPanel(new PlayPanel());
    }

    public void showModeSelection() {
        setPanel(new ModeSelectionPanel());
    }

    public void showDifficultySelection(int mode) {
        setPanel(new DifficultySelectionPanel(mode));
    }

    public void showPlayerNameInput(int mode, int difficulty) {
        setPanel(new PlayerNamePanel(mode, difficulty));
    }

    public void showMenu() {
        setPanel(new PlayPanel());
    }

    public void showGame(int mode, int difficulty, String player1Name, String player2Name) {
        // Fokuskan kembali ke window utama agar KeyListener tetap berfungsi
        this.requestFocusInWindow();
        setPanel(new GamePanel(mode, difficulty, player1Name, player2Name));
    }
}
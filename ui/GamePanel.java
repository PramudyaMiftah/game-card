package ui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private int mode; // 1 = 1 player, 2 = 2 player

    public GamePanel(int mode) {
        this.mode = mode;
        setLayout(new BorderLayout());

        // Panel atas (status bar)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, mode == 1 ? 3 : 2));

        if (mode == 1) {
            topPanel.add(new JLabel("❤️ Nyawa: 3"));
            topPanel.add(new JLabel("🕒 Timer: 60 detik"));
            topPanel.add(new JLabel("⭐ Level: 1"));
        } else {
            topPanel.add(new JLabel("👤 Giliran: Player 1"));
            topPanel.add(new JLabel("Skor P1: 0 | Skor P2: 0"));
        }

        add(topPanel, BorderLayout.NORTH);

        // Panel tengah (grid kartu)
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(4, 4, 10, 10)); // 4x4 = 16 kartu
        for (int i = 0; i < 16; i++) {
            JButton cardBtn = new JButton("❓");
            gridPanel.add(cardBtn);
        }
        add(gridPanel, BorderLayout.CENTER);

        // Panel bawah (opsional)
        JPanel bottomPanel = new JPanel();
        JButton backToMenu = new JButton("Kembali ke Menu");
        backToMenu.addActionListener(e -> GameWindow.getInstance().showMenu());
        bottomPanel.add(backToMenu);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}

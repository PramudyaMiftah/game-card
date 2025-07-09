package ui;

import javax.swing.*;

public class PlayPanel extends AbstractMenuPanel {

    public PlayPanel() {
        super("menu-utama-sakura.gif");
        this.title = "MEMORIZE CARD";
        this.subtitle = "MAIN MENU";
        this.menuOptions = new String[]{"Play", "High Scores", "Keluar"};
    }

<<<<<<< HEAD
    @Override
    protected void onEnterPressed() {
        switch (selectedIndex) {
            case 0: // Play
                GameWindow.getInstance().showModeSelection();
                break;
            case 1: // High Scores
                GameWindow.getInstance().showLeaderboard();
                break;
            case 2: // Keluar
                System.exit(0);
                break;
        }
=======
        // Judul Game
        gbc.gridy = 0; // Baris 0
        gbc.insets = new Insets(0, 0, 50, 0); // Jarak bawah 50px
        JLabel titleLabel = new JLabel("Memorizing Card", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(titleLabel, gbc);

        // Ukuran standar untuk semua tombol
        Dimension buttonSize = new Dimension(250, 40);
        Font buttonFont = new Font("Arial", Font.BOLD, 16);

        // Tombol Play
        gbc.gridy = 1; // Baris 1
        gbc.insets = new Insets(0, 0, 15, 0); // Jarak bawah 15px
        JButton playButton = new JButton("Play");
        playButton.setPreferredSize(buttonSize);
        playButton.setFont(buttonFont);
        playButton.setBackground(Color.decode("#4682B4"));
        playButton.setForeground(Color.WHITE);
        playButton.addActionListener(e -> GameWindow.getInstance().showModeSelection());
        add(playButton, gbc);

        // Tombol Leaderboard
        gbc.gridy = 2; // Baris 2
        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setPreferredSize(buttonSize);
        leaderboardButton.setFont(buttonFont);
        leaderboardButton.setBackground(Color.decode("#32CD32"));
        leaderboardButton.setForeground(Color.WHITE);
        leaderboardButton.addActionListener(e -> GameWindow.getInstance().showLeaderboard());
        add(leaderboardButton, gbc);

        // Tombol Keluar
        gbc.gridy = 3; // Baris 3
        gbc.insets = new Insets(0, 0, 15, 0); // Jarak bawah 15px
        JButton exitButton = new JButton("Keluar");
        exitButton.setPreferredSize(buttonSize);
        exitButton.setFont(buttonFont);
        exitButton.setBackground(Color.decode("#DC143C"));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton, gbc);
>>>>>>> 1deecc7 (Save current progress before updating)
    }
}
package ui;

import javax.swing.*;
import java.awt.*;
import assetsmanager.SoundManager;
import assetsmanager.VideoManager;

public class PlayPanel extends JPanel {

    private final ImageIcon backgroundGif;

    public PlayPanel() {
        // Menggunakan GridBagLayout untuk kontrol posisi yang lebih baik
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#ADD8E6"));
        GridBagConstraints gbc = new GridBagConstraints();

        // Munculin gif
        backgroundGif = VideoManager.loadImageIcon("menu-utama.gif");

        // Judul Game
        gbc.gridy = 0; // Baris 0
        gbc.insets = new Insets(0, 0, 50, 0); // Jarak bawah 50px
        JLabel titleLabel = new JLabel("Memorizing Card", SwingConstants.CENTER);
        titleLabel.setFont(Menu.DISPLAY_FONT_XLARGE);
        titleLabel.setForeground(Menu.WARNA_JUDUL);
        add(titleLabel, gbc);

        // Pengaturan standar untuk semua tombol
        Dimension buttonSize = new Dimension(250, 40);
        Font buttonFont = Menu.DISPLAY_FONT_BUTTON;

        // --- Tombol Play ---
        gbc.gridy = 1; // Baris 1
        gbc.insets = new Insets(0, 0, 15, 0); // Jarak bawah 15px
        JButton playButton = new JButton("Play");
        playButton.setPreferredSize(buttonSize);
        playButton.setFont(buttonFont);
        playButton.setBackground(Color.decode("#4682B4"));
        playButton.setForeground(Color.WHITE);
        playButton.addActionListener(_ -> {
            SoundManager.playSound("click.wav"); // Suara klik
            GameWindow.getInstance().showModeSelection(); // Aksi navigasi
        });
        playButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SoundManager.playSound("hover.wav"); // Suara hover
            }
        });
        add(playButton, gbc);

        // --- Tombol High Scores ---
        gbc.gridy = 2; // Baris 2
        JButton leaderboardButton = new JButton("HIGH SCORES");
        leaderboardButton.setPreferredSize(buttonSize);
        leaderboardButton.setFont(buttonFont);
        leaderboardButton.setBackground(Color.decode("#4682B4"));
        leaderboardButton.setForeground(Color.WHITE);
        leaderboardButton.addActionListener(_ -> {
            SoundManager.playSound("click.wav"); // Suara klik
            GameWindow.getInstance().showLeaderboard(); // Aksi navigasi
        });
        leaderboardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SoundManager.playSound("hover.wav"); // Suara hover
            }
        });
        add(leaderboardButton, gbc);

        // --- Tombol Keluar ---
        gbc.gridy = 3; // Baris 3
        JButton exitButton = new JButton("Keluar");
        exitButton.setPreferredSize(buttonSize);
        exitButton.setFont(buttonFont);
        exitButton.setBackground(Color.decode("#DC143C"));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(_ -> {
            SoundManager.playSound("click.wav"); // Suara klik
            System.exit(0); // Aksi keluar
        });
        exitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SoundManager.playSound("hover.wav"); // Suara hover
            }
        });
        add(exitButton, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundGif != null) {
            // Gambar GIF di seluruh area panel
            g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}


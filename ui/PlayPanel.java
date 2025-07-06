package ui;

import javax.swing.*;
import java.awt.*;

public class PlayPanel extends JPanel {
    public PlayPanel() {
        // Menggunakan GridBagLayout untuk kontrol posisi yang lebih baik
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#ADD8E6"));
        GridBagConstraints gbc = new GridBagConstraints();

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
        playButton.addActionListener(_ -> GameWindow.getInstance().showModeSelection());
        add(playButton, gbc);

        // Tombol Keluar
        gbc.gridy = 2; // Baris 2
        JButton exitButton = new JButton("Keluar");
        exitButton.setPreferredSize(buttonSize);
        exitButton.setFont(buttonFont);
        exitButton.setBackground(Color.decode("#DC143C"));
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(_ -> System.exit(0));
        add(exitButton, gbc);
    }
}
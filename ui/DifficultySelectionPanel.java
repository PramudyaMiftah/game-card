package ui;

import javax.swing.*;
import java.awt.*;

public class DifficultySelectionPanel extends JPanel {
    public DifficultySelectionPanel(int mode) {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#ADD8E6"));
        GridBagConstraints gbc = new GridBagConstraints();

        // Judul
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        JLabel title = new JLabel("Pilih Tingkat Kesulitan", SwingConstants.CENTER);
        title.setFont(Menu.DISPLAY_FONT_LARGE);
        add(title, gbc);

        // Pengaturan standar
        Dimension buttonSize = new Dimension(250, 40);
        Font buttonFont = Menu.DISPLAY_FONT_BUTTON;
        gbc.insets = new Insets(0, 0, 15, 0);

        // Tombol Easy
        gbc.gridy = 1;
        JButton easyBtn = new JButton("Easy");
        easyBtn.setPreferredSize(buttonSize);
        easyBtn.setFont(buttonFont);
        easyBtn.setBackground(Color.decode("#4682B4"));
        easyBtn.setForeground(Color.WHITE);
        easyBtn.addActionListener(_ -> GameWindow.getInstance().showPlayerNameInput(mode, 0));
        add(easyBtn, gbc);

        // Tombol Medium
        gbc.gridy = 2;
        JButton mediumBtn = new JButton("Medium");
        mediumBtn.setPreferredSize(buttonSize);
        mediumBtn.setFont(buttonFont);
        mediumBtn.setBackground(Color.decode("#4682B4"));
        mediumBtn.setForeground(Color.WHITE);
        mediumBtn.addActionListener(_ -> GameWindow.getInstance().showPlayerNameInput(mode, 1));
        add(mediumBtn, gbc);

        // Tombol Hard
        gbc.gridy = 3;
        JButton hardBtn = new JButton("Hard");
        hardBtn.setPreferredSize(buttonSize);
        hardBtn.setFont(buttonFont);
        hardBtn.setBackground(Color.decode("#4682B4"));
        hardBtn.setForeground(Color.WHITE);
        hardBtn.addActionListener(_ -> GameWindow.getInstance().showPlayerNameInput(mode, 2));
        add(hardBtn, gbc);

        // Tombol Kembali
        gbc.gridy = 4;
        JButton backButton = new JButton("Kembali");
        backButton.setPreferredSize(buttonSize);
        backButton.setFont(buttonFont);
        backButton.setBackground(Color.decode("#696969"));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(_ -> GameWindow.getInstance().showModeSelection());
        add(backButton, gbc);
    }
}
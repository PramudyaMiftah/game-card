package ui;

import javax.swing.*;
import java.awt.*;

public class ModeSelectionPanel extends JPanel {
    public ModeSelectionPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.decode("#ADD8E6"));
        GridBagConstraints gbc = new GridBagConstraints();

        // Judul
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 50, 0);
        JLabel title = new JLabel("Pilih Mode", SwingConstants.CENTER);
        title.setFont(Menu.DISPLAY_FONT_LARGE);
        add(title, gbc);

        // Pengaturan standar
        Dimension buttonSize = new Dimension(250, 40);
        Font buttonFont = Menu.DISPLAY_FONT_BUTTON;
        gbc.insets = new Insets(0, 0, 15, 0);

        // Tombol 1 Player
        gbc.gridy = 1;
        JButton onePlayerBtn = new JButton("1 Player");
        onePlayerBtn.setPreferredSize(buttonSize);
        onePlayerBtn.setFont(buttonFont);
        onePlayerBtn.setBackground(Color.decode("#4682B4"));
        onePlayerBtn.setForeground(Color.WHITE);
        onePlayerBtn.addActionListener(_ -> GameWindow.getInstance().showDifficultySelection(1));
        add(onePlayerBtn, gbc);

        // Tombol 2 Player
        gbc.gridy = 2;
        JButton twoPlayerBtn = new JButton("2 Player");
        twoPlayerBtn.setPreferredSize(buttonSize);
        twoPlayerBtn.setFont(buttonFont);
        twoPlayerBtn.setBackground(Color.decode("#4682B4"));
        twoPlayerBtn.setForeground(Color.WHITE);
        twoPlayerBtn.addActionListener(_ -> GameWindow.getInstance().showDifficultySelection(2));
        add(twoPlayerBtn, gbc);

        // Tombol Kembali
        gbc.gridy = 3;
        JButton backButton = new JButton("Kembali");
        backButton.setPreferredSize(buttonSize);
        backButton.setFont(buttonFont);
        backButton.setBackground(Color.decode("#696969"));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(_ -> GameWindow.getInstance().showPlayPanel());
        add(backButton, gbc);
    }
}
package ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#ADD8E6"));

        JLabel title = new JLabel("Memorizing Card", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBackground(Color.decode("#ADD8E6"));
        JButton onePlayerBtn = new JButton("1 Player");
        onePlayerBtn.setBackground(Color.decode("#4682B4"));
        onePlayerBtn.setForeground(Color.WHITE);
        onePlayerBtn.setFont(new Font("Arial", Font.BOLD, 32));

        JButton twoPlayerBtn = new JButton("2 Player");
        twoPlayerBtn.setBackground(Color.decode("#4682B4"));
        twoPlayerBtn.setForeground(Color.WHITE);
        twoPlayerBtn.setFont(new Font("Arial", Font.BOLD, 32));

        JButton exitBtn = new JButton("Keluar");
        exitBtn.setBackground(Color.decode("#4682B4"));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFont(new Font("Arial", Font.BOLD, 32));

        buttonPanel.add(onePlayerBtn);
        buttonPanel.add(twoPlayerBtn);
        buttonPanel.add(exitBtn);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.decode("#ADD8E6"));
        wrapperPanel.add(buttonPanel);
        add(wrapperPanel, BorderLayout.CENTER);
    }
}
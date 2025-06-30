package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Memorizing Card", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton onePlayerBtn = new JButton("1 Player");
        JButton twoPlayerBtn = new JButton("2 Player");

        // Aksi tombol 1 Player
        onePlayerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindow.getInstance().showGamePanel1Player();
            }
        });

        // Aksi tombol 2 Player (opsional)
        twoPlayerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameWindow.getInstance().showGamePanel2Player();
            }
        });

        centerPanel.add(onePlayerBtn);
        centerPanel.add(twoPlayerBtn);
        add(centerPanel, BorderLayout.CENTER);
    }
}

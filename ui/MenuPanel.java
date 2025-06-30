package ui;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Memorizing Card", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton onePlayerBtn = new JButton("1 Player");
        JButton twoPlayerBtn = new JButton("2 Player");
        JButton exitBtn = new JButton("Keluar");

        // 1 PLAYER BUTTON - PILIH MODE
        onePlayerBtn.addActionListener(e -> {
            String[] options = {"Easy", "Medium", "Hard"};
            int selected = JOptionPane.showOptionDialog(
                    this,
                    "Pilih Mode Permainan",
                    "1 Player Mode",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (selected != -1) {
                GameWindow.getInstance().showGamePanel1Player(selected);
            }
        });

        // 2 PLAYER BUTTON
        twoPlayerBtn.addActionListener(e -> GameWindow.getInstance().showGamePanel2Player());

        exitBtn.addActionListener(e -> System.exit(0));

        buttonPanel.add(onePlayerBtn);
        buttonPanel.add(twoPlayerBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.CENTER);
    }
}

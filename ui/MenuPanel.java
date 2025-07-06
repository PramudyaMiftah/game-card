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

        onePlayerBtn.addActionListener(_ -> {
            String[] options = {"Easy (4x4)", "Medium (5x4)", "Hard (6x5)"};
            int difficulty = JOptionPane.showOptionDialog(
                    this,
                    "Pilih Tingkat Kesulitan",
                    "1 Player Mode",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (difficulty != -1) {
                GameWindow.getInstance().showGame(1, difficulty);
            }
        });

        twoPlayerBtn.addActionListener(_ -> {
            String[] options = {"Easy (4x4)", "Medium (5x4)", "Hard (6x5)"};
            int difficulty = JOptionPane.showOptionDialog(
                    this,
                    "Pilih Tingkat Kesulitan",
                    "2 Player Mode",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (difficulty != -1) {
                GameWindow.getInstance().showGame(2, difficulty);
            }
        });

        exitBtn.addActionListener(_ -> System.exit(0));

        buttonPanel.add(onePlayerBtn);
        buttonPanel.add(twoPlayerBtn);
        buttonPanel.add(exitBtn);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.add(buttonPanel);
        add(wrapperPanel, BorderLayout.CENTER);
    }
}
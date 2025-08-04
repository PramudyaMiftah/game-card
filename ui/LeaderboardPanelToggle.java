package ui;

import leaderboard.MultiLeaderboardManager;
import leaderboard.ScoreEntry;
import assetsmanager.VideoManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Leaderboard panel that shows either 1-player or 2-player scores.
 * A pair of buttons lets the user switch between the two modes.
 * The visual layout matches the earlier single-board style (narrow, centred table
 * over a translucent black background).
 */
public class LeaderboardPanelToggle extends JPanel {
    private static final int MODE_1P = MultiLeaderboardManager.MODE_1P;
    private static final int MODE_2P = MultiLeaderboardManager.MODE_2P;

    private final ImageIcon backgroundGif;

    private int currentMode = MODE_1P;
    private JPanel centerWrapper; // container holding the scrollable table
    private JButton btn1P;
    private JButton btn2P;

    public LeaderboardPanelToggle() {
        setLayout(new BorderLayout());
        backgroundGif = VideoManager.loadImageIcon("menu-utama-sakura.gif");

        // ---------- Title (Custom drawn with outline) ----------
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                String titleText = "HIGH SCORES";
                g2d.setFont(Menu.DISPLAY_FONT_LARGE);
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(titleText);
                int x = (getWidth() - titleWidth) / 2;
                int y = getHeight() - 20;
                
                drawTextWithOutline(g2d, titleText, x, y, Menu.WARNA_JUDUL, 2);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(200, 0, 10, 0));
        add(titlePanel, BorderLayout.NORTH);

        // ---------- Mode Switch Buttons ----------
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        switchPanel.setOpaque(false);
        btn1P = new JButton("1 PLAYER");
        btn2P = new JButton("2 PLAYER");
        
        // Style buttons with state-based styling and custom painting for outline
        makeButtonWithOutline(btn1P);
        makeButtonWithOutline(btn2P);
        updateButtonStates();
        btn1P.addActionListener(e -> switchMode(MODE_1P));
        btn2P.addActionListener(e -> switchMode(MODE_2P));
        switchPanel.add(btn1P);
        switchPanel.add(btn2P);

        JPanel topWrapper = new JPanel();
        topWrapper.setLayout(new BoxLayout(topWrapper, BoxLayout.Y_AXIS));
        topWrapper.setOpaque(false);
        topWrapper.add(titlePanel);
        topWrapper.add(Box.createVerticalStrut(10)); // Small space after title
        topWrapper.add(switchPanel);
        topWrapper.add(Box.createVerticalStrut(20)); // Space between buttons and leaderboard
        add(topWrapper, BorderLayout.NORTH);

        // ---------- Center table ----------
        centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrapper.setOpaque(false);
        add(centerWrapper, BorderLayout.CENTER);
        rebuildCenter();

        // ---------- Back button ----------
        JButton back = new JButton("Kembali");
        makeButtonWithOutline(back);
        styleMainMenuButton(back);
        back.addActionListener(e -> {
            // Keep music playing when returning to menu
            GameWindow.getInstance().showMenu();
        });
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        // ESC shortcut (same as back)
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke("BACK_SPACE"), "back");
        am.put("back", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                back.doClick();
            }
        });
    }

    private void switchMode(int mode) {
        if (currentMode != mode) {
            currentMode = mode;
            updateButtonStates();
            rebuildCenter();
        }
    }

    private void rebuildCenter() {
        centerWrapper.removeAll();
        centerWrapper.add(buildScrollableTable(currentMode));
        revalidate();
        repaint();
    }

    private JScrollPane buildScrollableTable(int mode) {
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);

        Font headerFont = Menu.DISPLAY_FONT_MEDIUM;
        Font rowFont = Menu.DISPLAY_FONT_BUTTON;

        String scoreHeader = (mode == MODE_1P ? "SISA WAKTU" : "SCORE");
        centerContent.add(buildRow("RANK", "NAME", scoreHeader, headerFont, Color.WHITE));
        ((JComponent) centerContent.getComponent(0)).setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));

        int fixedWidth = 550;
        centerContent.setMaximumSize(new Dimension(fixedWidth, Integer.MAX_VALUE));

        List<ScoreEntry> scores = MultiLeaderboardManager.getScores(mode);
        int rank = 1;
        for (ScoreEntry entry : scores) {
            Color color = rank == 1 ? Color.YELLOW : Color.WHITE;
            String scoreText = (mode == MODE_1P ? entry.getScore() + " detik" : String.valueOf(entry.getScore()));
            JPanel row = buildRow(ordinal(rank), entry.getName(), scoreText, rowFont, color);
            row.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            centerContent.add(row);
            rank++;
        }

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setOpaque(true);
        tablePanel.setBackground(new Color(0, 0, 0, 100));
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // More balanced padding
        tablePanel.add(centerContent);

        JScrollPane scroll = new JScrollPane(tablePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(fixedWidth, 300));
        return scroll;
    }

    // Helper to build each row
    private JPanel buildRow(String r, String n, String s, Font f, Color c) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        JLabel rank = createLabel(r, Menu.FONT_ANGKA, c, 80);
        rank.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel name = createLabel(n, f, c, 250);
        JLabel score = createLabel(s, Menu.FONT_ANGKA, c, 150);
        score.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(rank);
        row.add(Box.createHorizontalStrut(20));
        row.add(name);
        row.add(Box.createHorizontalGlue());
        row.add(score);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return row;
    }

    private JLabel createLabel(String text, Font font, Color color, int minWidth) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        if (minWidth > 0) {
            Dimension size = new Dimension(minWidth, 24);
            label.setMinimumSize(size);
            label.setPreferredSize(size);
            label.setMaximumSize(size);
        }
        return label;
    }

    private String ordinal(int n) {
        int mod100 = n % 100;
        int mod10 = n % 10;
        if (mod100 - mod10 == 10) return n + "TH";
        return switch (mod10) {
            case 1 -> n + "ST";
            case 2 -> n + "ND";
            case 3 -> n + "RD";
            default -> n + "TH";
        };
    }

    private void styleMainMenuButton(JButton button) {
        button.setFont(Menu.DISPLAY_FONT_BUTTON); // This font works fine for "Kembali" (no numbers)
        button.setForeground(Color.WHITE); // White text like unselected mode buttons
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(null);
        button.setMargin(new Insets(5, 10, 5, 10));
    }

    private void updateButtonStates() {
        styleModeButton(btn1P, currentMode == MODE_1P);
        styleModeButton(btn2P, currentMode == MODE_2P);
    }

    private void styleModeButton(JButton button, boolean isSelected) {
        button.setFont(Menu.FONT_ANGKA); // Use number font for proper number rendering
        button.setFocusPainted(false);
        button.setBorderPainted(false); // Remove borders - text only
        button.setContentAreaFilled(false); // Remove background fill
        button.setOpaque(false); // Make transparent
        button.setBorder(null); // No border at all
        
        if (isSelected) {
            // Selected state: blue text (like main menu selected)
            button.setForeground(Menu.WARNA_JUDUL);
        } else {
            // Unselected state: white text
            button.setForeground(Color.WHITE);
        }
        
        button.setMargin(new Insets(5, 10, 5, 10)); // Smaller margins for text-only
    }

    private void makeButtonWithOutline(JButton button) {
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton btn = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                String text = btn.getText();
                FontMetrics fm = g2d.getFontMetrics(btn.getFont());
                int x = (btn.getWidth() - fm.stringWidth(text)) / 2;
                int y = (btn.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                
                drawTextWithOutline(g2d, text, x, y, btn.getForeground(), 2);
                g2d.dispose();
            }
        });
    }

    private void drawTextWithOutline(Graphics2D g2d, String text, int x, int y, Color textColor, int outlineThickness) {
        // Draw black outline
        g2d.setColor(Color.BLACK);
        for (int dx = -outlineThickness; dx <= outlineThickness; dx++) {
            for (int dy = -outlineThickness; dy <= outlineThickness; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, x + dx, y + dy);
                }
            }
        }
        // Draw main text
        g2d.setColor(textColor);
        g2d.drawString(text, x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundGif != null) {
            g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
}

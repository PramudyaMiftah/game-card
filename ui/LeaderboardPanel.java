package ui;
import leaderboard.LeaderboardManager;
import leaderboard.ScoreEntry;
<<<<<<< HEAD
import assetsmanager.VideoManager;
=======
>>>>>>> 1deecc7 (Save current progress before updating)

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardPanel extends JPanel {
<<<<<<< HEAD

    private final ImageIcon backgroundGif;

    public LeaderboardPanel() {
        setLayout(new BorderLayout());

        backgroundGif = VideoManager.loadImageIcon("menu-utama-sakura.gif");

        // ----- Title ------------------------------------------------------
        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setFont(Menu.DISPLAY_FONT_LARGE);
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(200, 0, 20, 0)); // top: 60
        add(title, BorderLayout.NORTH);

        // ----- Center list -------------------------------------------------
        JPanel centerContent = new JPanel();
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));
        centerContent.setOpaque(false);


        Font headerFont = Menu.DISPLAY_FONT_MEDIUM;
        Font rowFont    = Menu.DISPLAY_FONT_BUTTON;

        // Header row
        JPanel header = buildRow("RANK", "NAME", "SCORE", headerFont, Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE)); // garis bawah putih
        centerContent.add(header);

        // Limit width
        int fixedWidth = 400;
        centerContent.setMaximumSize(new Dimension(fixedWidth, Integer.MAX_VALUE));
=======
    public LeaderboardPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#ADD8E6")); // keep game background color

        // ----- Title ------------------------------------------------------
        JLabel title = new JLabel("HIGH SCORES", SwingConstants.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        // ----- Center list -------------------------------------------------
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        Font headerFont = new Font("Monospaced", Font.BOLD, 24);
        Font rowFont    = new Font("Monospaced", Font.BOLD, 22);

        // Header row
        JPanel header = buildRow("RANK", "NAME", "SCORE", headerFont, Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        center.add(header);

        // Limit width
        int fixedWidth = 400;
        center.setMaximumSize(new Dimension(fixedWidth, Integer.MAX_VALUE));
>>>>>>> 1deecc7 (Save current progress before updating)
        List<ScoreEntry> scores = LeaderboardManager.getScores();
        int rank = 1;
        for (ScoreEntry entry : scores) {
            Color color = (rank == 1) ? Color.YELLOW : Color.WHITE;
            JPanel row = buildRow(ordinal(rank), entry.getName(), String.valueOf(entry.getScore()), rowFont, color);
            row.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
<<<<<<< HEAD
            centerContent.add(row);
            rank++;
        }

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.setOpaque(true);
        tablePanel.setBackground(new Color(0, 0, 0, 100)); // hitam transparan
        tablePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 80));
        tablePanel.add(centerContent);


        JScrollPane scroll = new JScrollPane(tablePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
=======
            center.add(row);
            rank++;
        }

        JScrollPane scroll = new JScrollPane(center, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
>>>>>>> 1deecc7 (Save current progress before updating)
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(fixedWidth, 300));

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapper.setOpaque(false);
        wrapper.add(scroll);
        add(wrapper, BorderLayout.CENTER);

        // ----- Back button -------------------------------------------------
        JButton back = new JButton("Kembali");
<<<<<<< HEAD
        back.setFont(Menu.DISPLAY_FONT_BUTTON); //
=======
        back.setFont(new Font("Monospaced", Font.BOLD, 16));
>>>>>>> 1deecc7 (Save current progress before updating)
        back.setBackground(Color.decode("#4682B4"));
        back.setForeground(Color.WHITE);
        back.addActionListener(e -> GameWindow.getInstance().showPlayPanel());

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);
    }

    // Helper to build each row
    private JPanel buildRow(String r, String n, String s, Font f, Color c) {
        JPanel row = new JPanel();
        row.setOpaque(false);
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        JLabel rank = createLabel(r, f, c, 70);
        rank.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel name = createLabel(n, f, c, 230);
        JLabel score = createLabel(s, f, c, 0);
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

    // Convert 1 -> 1ST, 2 -> 2ND, etc.
    private String ordinal(int n) {
        int mod100 = n % 100;
        int mod10  = n % 10;
        if (mod100 - mod10 == 10) return n + "TH";
        return switch (mod10) {
            case 1 -> n + "ST";
            case 2 -> n + "ND";
            case 3 -> n + "RD";
            default -> n + "TH";
        };
    }
<<<<<<< HEAD

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundGif != null) {
            // Gambar GIF di seluruh area panel
            g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
=======
>>>>>>> 1deecc7 (Save current progress before updating)
}
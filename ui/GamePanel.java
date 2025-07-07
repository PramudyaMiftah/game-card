package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Timer;
import java.util.Map;
import java.util.HashMap;

public class GamePanel extends JPanel {
    private final int mode;
    private final int difficulty;
    private final int rows;
    private final int cols;
    private final String player1Name;
    private final String player2Name;

    private final Stack<CardUI> openedCards = new Stack<>();
    private final Map<String, Boolean> matchedPairs = new HashMap<>();

    private int lives = 3;
    private int timeLeft = 60;
    private Timer countdownTimer;
    private JLabel lifeLabel, timerLabel;

    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private final Queue<Integer> playerTurnQueue = new LinkedList<>();
    private JLabel turnLabel, scoreLabel;
    private boolean isChecking = false;

    // --- Constructor telah diubah untuk menerima nama pemain ---
    public GamePanel(int mode, int difficulty, String player1Name, String player2Name) {
        this.mode = mode;
        this.difficulty = difficulty;
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        setLayout(new BorderLayout());
        setBackground(Color.decode("#ADD8E6"));

        switch (difficulty) {
            case 1 -> { rows = 5; cols = 4; } // Medium
            case 2 -> { rows = 6; cols = 4; } // Hard
            default -> { rows = 4; cols = 4; } // Easy
        }

        // --- Panel Atas (Info Pemain, Skor, Waktu) ---
        JPanel topPanel = new JPanel(new GridLayout(1, mode == 1 ? 3 : 2, 10, 20));
        topPanel.setBackground(Color.decode("#ADD8E6"));
        if (mode == 1) {
            lifeLabel = new JLabel("❤️ Nyawa: " + lives);
            timerLabel = new JLabel("🕒 Timer: " + timeLeft + " detik");
            topPanel.add(lifeLabel);
            topPanel.add(timerLabel);
            topPanel.add(new JLabel("⭐ Level: " + getDifficultyLabel()));
        } else {
            // PENTING: Mengosongkan dan mengatur ulang antrian untuk game 2P baru
            playerTurnQueue.clear();
            playerTurnQueue.add(1);
            playerTurnQueue.add(2);

            turnLabel = new JLabel("👤 Giliran: " + this.player1Name);
            scoreLabel = new JLabel("Skor " + this.player1Name + ": 0 | Skor " + this.player2Name + ": 0");
            topPanel.add(turnLabel);
            topPanel.add(scoreLabel);
        }
        add(topPanel, BorderLayout.NORTH);

        // --- Panel Kartu (Grid Permainan) ---
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        gridPanel.setBackground(Color.decode("#ADD8E6"));
        List<String> cardNames = generateCardPairs(rows * cols);
        ImageIcon backIcon = loadCardImage("/assets/cards/card_back.png", true);

        for (String name : cardNames) {
            ImageIcon frontIcon = loadCardImage("/assets/cards/" + name, false);
            CardUI card = new CardUI(name, frontIcon, backIcon);
            card.getButton().addActionListener(_ -> handleCardClick(card));
            gridPanel.add(card.getButton());
        }
        JPanel wrapper = new JPanel(new BorderLayout()); // Bungkus grid
        wrapper.setBorder(BorderFactory.createEmptyBorder(100, 300, 20, 300));
        wrapper.setOpaque(false); // Biar transparan kalau ada background

        wrapper.add(gridPanel, BorderLayout.CENTER); // Masukkan grid ke tengah wrapper
        add(wrapper, BorderLayout.CENTER); // Masukkan wrapper ke layout utama


        // --- Panel Bawah (Tombol Kembali) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.decode("#ADD8E6"));
        JButton backBtn = new JButton("Kembali ke Menu");
        backBtn.setBackground(Color.decode("#4682B4"));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(_ -> {
            stopTimer();
            GameWindow.getInstance().showMenu();
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        if (mode == 1) {
            startCountdownTimer();
        }
    }

    private void handleCardClick(CardUI clicked) {
        if (clicked.isMatched() || openedCards.contains(clicked) || isChecking) {
            return;
        }

        clicked.flipUp();
        openedCards.push(clicked);

        if (openedCards.size() == 2) {
            isChecking = true;
            CardUI first = openedCards.pop();
            CardUI second = openedCards.pop();

            if (first.getName().equals(second.getName())) { // Kartu cocok
                first.setMatched(true);
                second.setMatched(true);
                matchedPairs.put(first.getName(), true);

                if (mode == 2 && !playerTurnQueue.isEmpty()) {
                    if (playerTurnQueue.peek() == 1) scoreP1++;
                    else scoreP2++;
                    updateScoreAndTurn();
                }

                if (isGameWon()) {
                    if (mode == 1) stopTimer();
                    showWinDialog();
                }
                isChecking = false;
            } else { // Kartu tidak cocok
                javax.swing.Timer flipBackTimer = new javax.swing.Timer(1000, _ -> {
                    first.flipDown();
                    second.flipDown();
                    if (mode == 2 && !playerTurnQueue.isEmpty()) {
                        playerTurnQueue.add(playerTurnQueue.poll()); // Ganti giliran pemain
                        updateScoreAndTurn();
                    }
                    isChecking = false;
                });
                flipBackTimer.setRepeats(false);
                flipBackTimer.start();
            }
        }
    }

    private void startCountdownTimer() {
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new java.util.TimerTask() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateTampilanWaktu();
                } else {
                    lives--;
                    updateTampilanNyawa();
                    if (lives <= 0) {
                        stopTimer();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(GamePanel.this, "Game Over! Kamu kehabisan nyawa.");
                            GameWindow.getInstance().showMenu();
                        });
                    } else {
                        timeLeft = 60;
                        updateTampilanWaktu();
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(GamePanel.this, "Waktu Habis! Kamu kehilangan 1 nyawa."));
                    }
                }
            }
        }, 1000, 1000);
    }

    public void stopTimer() {
        if (countdownTimer != null) {
            countdownTimer.cancel();
            countdownTimer = null; // Set ke null untuk mencegah error
        }
    }

    private void updateTampilanWaktu() {
        if (timerLabel != null) {
            timerLabel.setText("🕒 Timer: " + timeLeft + " detik");
        }
    }

    private void updateTampilanNyawa() {
        if (lifeLabel != null) {
            lifeLabel.setText("❤️ Nyawa: " + lives);
        }
    }

    private void updateScoreAndTurn() {
        if (mode == 2) {
            String currentPlayerName = playerTurnQueue.peek() == 1 ? player1Name : player2Name;
            turnLabel.setText("👤 Giliran: " + currentPlayerName);
            scoreLabel.setText("Skor " + player1Name + ": " + scoreP1 + " | Skor " + player2Name + ": " + scoreP2);
        }
    }

    private ImageIcon loadCardImage(String path, boolean isBack) {
        String[] extensions = {".png", ".jpg", ".jpeg"};
        java.net.URL imgURL = null;

        if (isBack) {
            imgURL = getClass().getResource(path);
        } else {
            for (String ext : extensions) {
                imgURL = getClass().getResource(path + ext);
                if (imgURL != null) break;
            }
        }

        if (imgURL != null) {
            ImageIcon originalIcon = new ImageIcon(imgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("Nggak nemu file gambar untuk: " + path);
            BufferedImage placeholder = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = placeholder.createGraphics();
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, 150, 150);
            g.dispose();
            return new ImageIcon(placeholder);
        }
    }

    private List<String> generateCardPairs(int totalCards) {
        String[] possible = {
                "anjing", "avocado", "carrot", "coffe", "cupcake", "pig",
                "eskrim", "hammy", "jerapah", "mushroom", "penguin",
                "butterfly", "tomat", "watermelon", "bee", "shark",
                "jellyfish", "kelinci", "meng", "tikus"
        };
        List<String> names = new ArrayList<>();
        for (int i = 0; i < totalCards / 2; i++) {
            names.add(possible[i]);
            names.add(possible[i]);
        }
        Collections.shuffle(names);
        return names;
    }

    private boolean isGameWon() {
        return matchedPairs.size() == (rows * cols) / 2;
    }

    private void showWinDialog() {
        String message;
        if (mode == 1) {
            message = "Selamat " + player1Name + "! Kamu berhasil mencocokkan semua kartu!";
        } else {
            if (scoreP1 > scoreP2) {
                message = "Selamat " + player1Name + "! Kamu memenangkan permainan!";
            } else if (scoreP2 > scoreP1) {
                message = "Selamat " + player2Name + "! Kamu memenangkan permainan!";
            } else {
                message = "Permainan berakhir seri!";
            }
        }

        int option = JOptionPane.showOptionDialog(
                this,
                message,
                "Permainan Selesai!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Main Lagi", "Kembali ke Menu"},
                "Main Lagi"
        );

        if (option == 0) { // Main Lagi
            GameWindow.getInstance().showDifficultySelection(mode);
        } else { // Kembali ke Menu
            GameWindow.getInstance().showMenu();
        }
    }

    private String getDifficultyLabel() {
        return switch (difficulty) {
            case 0 -> "Easy";
            case 1 -> "Medium";
            case 2 -> "Hard";
            default -> "-";
        };
    }
}
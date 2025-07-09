package ui;

import leaderboard.LeaderboardManager;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

import assetsmanager.ImageManager;
//import assetsmanager.VideoManager;
import leaderboard.LeaderboardManager;
import assetsmanager.SoundManager;

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

    // --- Arcade Scoring helpers ---
    private int streakP1 = 0;
    private int streakP2 = 0;
    private int missesP1 = 0;
    private int missesP2 = 0;
    private final Queue<Integer> playerTurnQueue = new LinkedList<>();
    private JLabel turnLabel, scoreLabel;
    private boolean isChecking = false;

    private final ImageIcon backgroundGif;

    public GamePanel(int mode, int difficulty, String player1Name, String player2Name) {
        this.mode = mode;
        this.difficulty = difficulty;
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        setLayout(new BorderLayout());
        setBackground(Color.decode("#ADD8E6"));
        backgroundGif = ImageManager.loadImageIcon("game-panel.jpeg.jpg");

        switch (difficulty) {
            case 1 -> {
                rows = 4;
                cols = 5;
            } // Medium
            case 2 -> {
                rows = 4;
                cols = 6;
            } // Hard
            default -> {
                rows = 4;
                cols = 4;
            } // Easy
        }

        // --- Panel Atas (Info Pemain, Skor, Waktu) ---
        RoundedPanel topPanel = new RoundedPanel(30); // 30px radius sudut
        topPanel.setLayout(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        topPanel.setBackground(Color.decode("#4682B4"));
        Font statusFont = Menu.DISPLAY_FONT_BUTTON;
        Color fontColor = Color.WHITE;

        if (mode == 1) {
            lifeLabel = new JLabel("❤️ Nyawa: " + lives);
            lifeLabel.setFont(statusFont);
            lifeLabel.setForeground(fontColor);

            timerLabel = new JLabel("🕒 Timer: " + timeLeft + " detik");
            timerLabel.setFont(statusFont);
            timerLabel.setForeground(fontColor);
            timerLabel.setHorizontalAlignment(JLabel.CENTER);

            JLabel levelLabel = new JLabel("⭐ Level: " + getDifficultyLabel());
            levelLabel.setFont(statusFont);
            levelLabel.setForeground(fontColor);
            levelLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            topPanel.add(lifeLabel, BorderLayout.WEST);
            topPanel.add(timerLabel, BorderLayout.CENTER);
            topPanel.add(levelLabel, BorderLayout.EAST);

        } else {
            playerTurnQueue.clear();
            playerTurnQueue.add(1);
            playerTurnQueue.add(2);

            turnLabel = new JLabel("👤 Giliran: " + this.player1Name);
            turnLabel.setFont(statusFont);
            turnLabel.setForeground(fontColor);

            scoreLabel = new JLabel("Skor " + this.player1Name + ": " + scoreP1 + " | Skor " + this.player2Name + ": " + scoreP2);
            scoreLabel.setFont(statusFont);
            scoreLabel.setForeground(fontColor);
            scoreLabel.setHorizontalAlignment(JLabel.RIGHT);

            topPanel.add(turnLabel, BorderLayout.WEST);
            topPanel.add(scoreLabel, BorderLayout.EAST);
        }
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setOpaque(false); // Biar transparan
        topWrapper.setBorder(BorderFactory.createEmptyBorder(20, 50, 0, 50)); // Atas, kiri, bawah, kanan (margin luar)
        topWrapper.add(topPanel, BorderLayout.CENTER);

        add(topWrapper, BorderLayout.NORTH);


        // --- Panel Kartu (Grid Permainan) ---
        RoundedPanel gridPanel = new RoundedPanel(40); // 40 = radius
        gridPanel.setLayout(new GridLayout(rows, cols, 5, 5));
        gridPanel.setBackground(Color.decode("#EFEFEF"));
        List<String> cardNames = generateCardPairs(rows * cols);
        ImageIcon backIcon = loadCardImage("/assets/cards/card_back.png", true);

        for (String name : cardNames) {
            ImageIcon frontIcon = loadCardImage("/assets/cards/" + name, false);
            CardUI card = new CardUI(name, frontIcon, backIcon);
            card.getButton().addActionListener(e -> handleCardClick(card));
            gridPanel.add(card.getButton());
        }
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 300, 20, 300));
        wrapper.setOpaque(false); // Biar transparan kalau ada background

        wrapper.add(gridPanel, BorderLayout.CENTER); // Masukkan grid ke tengah wrapper
        add(wrapper, BorderLayout.CENTER); // Masukkan wrapper ke layout utama


        // --- Panel Bawah (Tombol Kembali) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
//        bottomPanel.setBackground(Color.decode("#ADD8E6"));
        JButton backBtn = new JButton("Kembali ke Menu");
        backBtn.setBackground(Color.decode("#4682B4"));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(e -> {
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
            // Hitung skor berbasis arcade (base * multiplier)
            int base = switch (difficulty) {
                case 1 -> 200; // Medium
                case 2 -> 300; // Hard
                default -> 100; // Easy
            };
            int currentPlayer = (mode == 2) ? playerTurnQueue.peek() : 1;

            // Update streak dan multiplier
            double multiplier;
            if (currentPlayer == 1) {
                streakP1++;
                multiplier = Math.min(1.0 + 0.2 * (streakP1 - 1), 2.0);
            } else {
                streakP2++;
                multiplier = Math.min(1.0 + 0.2 * (streakP2 - 1), 2.0);
            }
            int pairScore = (int) Math.round(base * multiplier);
            if (currentPlayer == 1) {
                scoreP1 += pairScore;
            } else {
                scoreP2 += pairScore;
            }
            updateScoreAndTurn();
                first.setMatched(true);
                second.setMatched(true);
                matchedPairs.put(first.getName(), true);

                if (mode == 2 && !playerTurnQueue.isEmpty()) {
                    
                    updateScoreAndTurn();
                }

                if (isGameWon()) {
                    // Pindahkan ke invokeLater agar event klik selesai dulu baru dialog muncul
                    SwingUtilities.invokeLater(() -> {
                        if (mode == 1) stopTimer();
                        showWinDialog();
                    });
                }
                isChecking = false;
            } else { // Kartu tidak cocok
                javax.swing.Timer flipBackTimer = new javax.swing.Timer(1000, ev -> {
                    first.flipDown();
                    second.flipDown();

                    // Akurasi: penalti salah tebak
                    int currentPlayer = (mode == 2) ? playerTurnQueue.peek() : 1;
                    if (currentPlayer == 1) {
                        scoreP1 = Math.max(0, scoreP1 - 50);
                        streakP1 = 0;
                        missesP1++;
                    } else {
                        scoreP2 = Math.max(0, scoreP2 - 50);
                        streakP2 = 0;
                        missesP2++;
                    }
                    updateScoreAndTurn();
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
        // Parameter: delay 1000ms (1 detik), dan aksi yang dijalankan setiap detiknya
        countdownTimer = new Timer(1000, _ -> {
            if (timeLeft > 0) {
                timeLeft--;
                updateTampilanWaktu();
            } else {
                lives--;
                updateTampilanNyawa();
                timeLeft = 60; // Reset waktu untuk kesempatan berikutnya

                if (lives <= 0) {
                    countdownTimer.stop(); // Hentikan timer sebelum pindah window
                    SoundManager.playSound("game-over.wav");
                    JOptionPane.showMessageDialog(this, "Game Over! Kamu kehabisan nyawa.", "Game Over", JOptionPane.ERROR_MESSAGE);
                    GameWindow.getInstance().showMenu();
                } else {
                    // Timer akan terus berjalan untuk nyawa berikutnya
                    JOptionPane.showMessageDialog(this, "Waktu Habis! Kamu kehilangan 1 nyawa.", "Waktu Habis", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        countdownTimer.start(); // Mulai timer
    }

    public void stopTimer() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop(); // Method untuk menghentikannya adalah .stop()
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

    private String getDifficultyLabel() {
        return switch (difficulty) {
            case 1 -> "Medium";
            case 2 -> "Hard";
            default -> "Easy";
        };
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
<<<<<<< HEAD
        SoundManager.playSound("win.wav");
        String message;
        if (mode == 1) {
            message = "Selamat " + player1Name + "! Kamu berhasil mencocokkan semua kartu!";
            int finalScore = lives * timeLeft; // Contoh perhitungan skor
            LeaderboardManager.addScore(player1Name, finalScore);
            // -------------------------

        } else {
            if (scoreP1 > scoreP2) {
                message = "Selamat " + player1Name + "! Kamu memenangkan permainan!";
                // Bisa juga ditambahkan penyimpanan skor untuk pemenang mode 2P jika mau
                // LeaderboardManager.addScore(player1Name, scoreP1);

            } else if (scoreP2 > scoreP1) {
                message = "Selamat " + player2Name + "! Kamu memenangkan permainan!";
                // LeaderboardManager.addScore(player2Name, scoreP2);

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
=======
    // ---- Final score bonuses -------------------------------------------------
    if (mode == 1) {
        int diffFactor = switch (difficulty) { case 1 -> 2; case 2 -> 3; default -> 1; };
        scoreP1 += timeLeft * 10 * diffFactor;          // time bonus
        if (missesP1 == 0) scoreP1 += 500;              // perfect-round bonus
    } else {
        if (missesP1 == 0) scoreP1 += 500;
        if (missesP2 == 0) scoreP2 += 500;
>>>>>>> 1deecc7 (Save current progress before updating)
    }

    // ---- Save scores to leaderboard -----------------------------------------
    if (mode == 1) {
        LeaderboardManager.addScore(player1Name, scoreP1);
    } else {
        LeaderboardManager.addScore(player1Name, scoreP1);
        LeaderboardManager.addScore(player2Name, scoreP2);
    }

<<<<<<< HEAD
    class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false); // Supaya latar belakang transparan dulu
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // penting untuk gambar component anak
        if (backgroundGif != null) {
            g.drawImage(backgroundGif.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }
=======
    // ---- Build win message ---------------------------------------------------
    String message;
    if (mode == 1) {
        message = String.format(
            "Selamat %s! Kamu berhasil mencocokkan semua kartu!\nSkor kamu: %d",
            player1Name, scoreP1);
    } else if (scoreP1 > scoreP2) {
        message = String.format("Selamat %s! Kamu memenangkan permainan!\n%d : %d",
                                player1Name, scoreP1, scoreP2);
    } else if (scoreP2 > scoreP1) {
        message = String.format("Selamat %s! Kamu memenangkan permainan!\n%d : %d",
                                player2Name, scoreP2, scoreP1);
    } else {
        message = String.format("Permainan berakhir seri!\n%d : %d", scoreP1, scoreP2);
    }

    // ---- Simple JOptionPane wrapped in JDialog ------------------------------
    JOptionPane pane = new JOptionPane(
        message,
        JOptionPane.INFORMATION_MESSAGE,
        JOptionPane.DEFAULT_OPTION,
        null,
        new Object[] { "Main Lagi", "Kembali ke Menu" },
        "Main Lagi"
    );

    JDialog dialog = pane.createDialog(GameWindow.getInstance(), "Permainan Selesai!");
    dialog.setModal(true);
    dialog.setAlwaysOnTop(true);
    dialog.setLocationRelativeTo(GameWindow.getInstance());
    dialog.toFront();
    dialog.setVisible(true);

    // ---- Next action ---------------------------------------------------------
    if ("Main Lagi".equals(pane.getValue())) {
        GameWindow.getInstance().showDifficultySelection(mode);
    } else {
        GameWindow.getInstance().showMenu();
    }
}
>>>>>>> 1deecc7 (Save current progress before updating)
}
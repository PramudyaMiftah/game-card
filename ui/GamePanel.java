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
    private int mode;
    private int difficulty;
    private int rows, cols;

    private List<CardUI> cards = new ArrayList<>();
    private Stack<CardUI> openedCards = new Stack<>();
    private Map<String, Boolean> matchedPairs = new HashMap<>();

    private int lives = 3;
    private int timeLeft = 60;
    private Timer countdownTimer;
    private JLabel lifeLabel, timerLabel;

    private int scoreP1 = 0, scoreP2 = 0;
    private Queue<Integer> playerTurnQueue = new LinkedList<>();
    private JLabel turnLabel, scoreLabel;

    public GamePanel(int mode, int difficulty) {
        this.mode = mode;
        this.difficulty = difficulty;
        setLayout(new BorderLayout());

        switch (difficulty) {
            case 0 -> { rows = 4; cols = 4; }
            case 1 -> { rows = 5; cols = 4; }
            case 2 -> { rows = 6; cols = 5; }
            default -> { rows = 4; cols = 4; }
        }

        JPanel topPanel = new JPanel(new GridLayout(1, mode == 1 ? 3 : 2));
        if (mode == 1) {
            lifeLabel = new JLabel("❤️ Nyawa: " + lives);
            timerLabel = new JLabel("🕒 Timer: " + timeLeft + " detik");
            topPanel.add(lifeLabel);
            topPanel.add(timerLabel);
            topPanel.add(new JLabel("⭐ Level: " + getDifficultyLabel()));
        } else {
            playerTurnQueue.add(1);
            playerTurnQueue.add(2);
            turnLabel = new JLabel("👤 Giliran: Player " + playerTurnQueue.peek());
            scoreLabel = new JLabel("Skor P1: 0 | Skor P2: 0");
            topPanel.add(turnLabel);
            topPanel.add(scoreLabel);
        }
        add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        List<String> cardNames = generateCardPairs(rows * cols);
        ImageIcon backIcon;
        java.net.URL backImgURL = getClass().getResource("/assets/cards/card_back.png");
        if (backImgURL != null) {
            ImageIcon originalIcon = new ImageIcon(backImgURL);
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            backIcon = new ImageIcon(scaledImage);
        } else {
            System.err.println("Nggak nemu file gambar: /assets/cards/card_back.png");
            backIcon = new ImageIcon(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));
        }


        for (String name : cardNames) {
            ImageIcon frontIcon = loadCardImage(name);
            CardUI card = new CardUI(name, frontIcon, backIcon);
            card.getButton().addActionListener(e -> handleCardClick(card));
            cards.add(card);
            gridPanel.add(card.getButton());
        }
        add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        JButton backBtn = new JButton("Kembali ke Menu");
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
        if (clicked.isMatched() || openedCards.contains(clicked) || openedCards.size() >= 2) {
            return;
        }

        clicked.flipUp();
        openedCards.push(clicked);

        if (openedCards.size() == 2) {
            CardUI second = openedCards.pop();
            CardUI first = openedCards.pop();

            if (first.getName().equals(second.getName())) {
                first.setMatched(true);
                second.setMatched(true);
                matchedPairs.put(first.getName(), true);

                if (mode == 2) {
                    if (playerTurnQueue.peek() == 1) scoreP1++;
                    else scoreP2++;
                    updateScoreAndTurn();
                }

                if (isGameWon()) {
                    if (mode == 1) stopTimer();
                    showWinDialog();
                }
            } else {
                javax.swing.Timer flipBackTimer = new javax.swing.Timer(1000, e -> {
                    first.flipDown();
                    second.flipDown();

                    if (mode == 2) {
                        int lastPlayer = playerTurnQueue.poll();
                        playerTurnQueue.add(lastPlayer);
                        updateScoreAndTurn();
                    }
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
            turnLabel.setText("👤 Giliran: Player " + playerTurnQueue.peek());
            scoreLabel.setText("Skor P1: " + scoreP1 + " | Skor P2: " + scoreP2);
        }
    }

    private ImageIcon loadCardImage(String name) {
        String[] extensions = {".png", ".jpg", ".jpeg"};
        int desiredWidth = 100;
        int desiredHeight = 100;

        for (String ext : extensions) {
            String path = "/assets/cards/" + name + ext;
            java.net.URL imgURL = getClass().getResource(path);

            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        }

        System.err.println("Nggak nemu file gambar untuk: " + name);
        BufferedImage placeholder = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, desiredWidth, desiredHeight);
        g.dispose();
        return new ImageIcon(placeholder);
    }

    private List<String> generateCardPairs(int totalCards) {
        String[] possible = {
                "avocado", "carrot", "coffe", "cupcake", "pig",
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
        int option = JOptionPane.showOptionDialog(
                this,
                "Selamat! Kamu berhasil mencocokkan semua kartu!",
                "Kamu Menang!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Main Lagi", "Kembali ke Menu"},
                "Main Lagi"
        );

        if (option == 0) {
            GameWindow.getInstance().showGame(1, difficulty);
        } else {
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
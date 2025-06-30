package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class GamePanel extends JPanel {
    private int mode;
    private int difficulty;
    private int rows, cols;

    private CardUI firstCard = null;
    private CardUI secondCard = null;
    private List<CardUI> cards = new ArrayList<>();

    private int lives = 3;
    private int timeLeft = 60;
    private Timer countdownTimer;
    private JLabel lifeLabel, timerLabel;

    private int currentPlayer = 1;
    private int scoreP1 = 0, scoreP2 = 0;
    private JLabel turnLabel, scoreLabel;

    public GamePanel(int mode, int difficulty) {
        this.mode = mode;
        this.difficulty = difficulty;
        setLayout(new BorderLayout());

        // Ukuran grid sesuai mode
        if (mode == 1) {
            switch (difficulty) {
                case 0 -> { rows = 4; cols = 4; }
                case 1 -> { rows = 5; cols = 4; }
                case 2 -> { rows = 6; cols = 5; }
            }
        } else {
            rows = 4; cols = 4;
        }

        // Panel atas
        JPanel topPanel = new JPanel(new GridLayout(1, mode == 1 ? 3 : 2));
        if (mode == 1) {
            lifeLabel = new JLabel("❤️ Nyawa: " + lives);
            timerLabel = new JLabel("🕒 Timer: " + timeLeft + " detik");
            topPanel.add(lifeLabel);
            topPanel.add(timerLabel);
            topPanel.add(new JLabel("⭐ Level: " + getDifficultyLabel()));
        } else {
            turnLabel = new JLabel("👤 Giliran: Player 1");
            scoreLabel = new JLabel("Skor P1: 0 | Skor P2: 0");
            topPanel.add(turnLabel);
            topPanel.add(scoreLabel);
        }
        add(topPanel, BorderLayout.NORTH);

        // Panel kartu
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        List<String> cardNames = generateCardPairs(rows * cols);
        for (String name : cardNames) {
            ImageIcon icon = loadCardImage(name);
            CardUI card = new CardUI(name, icon);
            card.getButton().addActionListener(e -> handleCardClick(card));
            cards.add(card);
            gridPanel.add(card.getButton());
        }
        add(gridPanel, BorderLayout.CENTER);

        // Panel bawah
        JPanel bottomPanel = new JPanel();
        JButton backBtn = new JButton("Kembali ke Menu");
        backBtn.addActionListener(e -> {
            if (countdownTimer != null) countdownTimer.stop();
            GameWindow.getInstance().showMenu();
        });
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        if (mode == 1) {
            startCountdownTimer();
        }
    }

    private void handleCardClick(CardUI clicked) {
        if (clicked.isMatched() || clicked.isFaceUp()) return;
        if (secondCard != null) return;

        clicked.flipUp();

        if (firstCard == null) {
            firstCard = clicked;
        } else {
            secondCard = clicked;

            if (firstCard.getName().equals(secondCard.getName())) {
                firstCard.setMatched(true);
                secondCard.setMatched(true);

                if (mode == 2) {
                    if (currentPlayer == 1) scoreP1++;
                    else scoreP2++;
                    updateScoreAndTurn(false);
                }

                firstCard = null;
                secondCard = null;

                if (mode == 1 && isGameWon()) {
                    if (countdownTimer != null) countdownTimer.stop();
                    showWinDialog();
                } else if (mode == 1) {
                    timeLeft = 60;
                    timerLabel.setText("🕒 Timer: " + timeLeft + " detik");
                }

            } else {
                Timer flipBackTimer = new Timer(1000, e -> {
                    firstCard.flipDown();
                    secondCard.flipDown();

                    if (mode == 1) {
                        lives--;
                        lifeLabel.setText("❤️ Nyawa: " + lives);
                        if (lives <= 0) {
                            countdownTimer.stop();
                            JOptionPane.showMessageDialog(this, "Game Over! Kamu kehabisan nyawa.");
                            GameWindow.getInstance().showMenu();
                            return;
                        }
                        timeLeft = 60;
                        timerLabel.setText("🕒 Timer: " + timeLeft + " detik");
                        countdownTimer.restart();
                    } else {
                        currentPlayer = (currentPlayer == 1) ? 2 : 1;
                        updateScoreAndTurn(true);
                    }

                    firstCard = null;
                    secondCard = null;
                });
                flipBackTimer.setRepeats(false);
                flipBackTimer.start();
            }
        }
    }

    private void startCountdownTimer() {
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("🕒 Timer: " + timeLeft + " detik");

            if (timeLeft <= 0) {
                lives--;
                lifeLabel.setText("❤️ Nyawa: " + lives);
                timeLeft = 60;
                timerLabel.setText("🕒 Timer: " + timeLeft + " detik");

                if (lives <= 0) {
                    countdownTimer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Kamu kehabisan nyawa.");
                    GameWindow.getInstance().showMenu();
                }
            }
        });
        countdownTimer.start();
    }

    private void updateScoreAndTurn(boolean switched) {
        if (mode == 2) {
            turnLabel.setText("👤 Giliran: Player " + currentPlayer);
            scoreLabel.setText("Skor P1: " + scoreP1 + " | Skor P2: " + scoreP2);
        }
    }

    private List<String> generateCardPairs(int totalCards) {
        String[] possible = {
                "apple", "banana", "cherry", "duck", "fish",
                "frog", "leaf", "star", "sun", "moon",
                "cat", "dog", "heart", "ice", "key"
        };
        List<String> names = new ArrayList<>();
        for (int i = 0; i < totalCards / 2; i++) {
            names.add(possible[i]);
            names.add(possible[i]);
        }
        Collections.shuffle(names);
        return names;
    }

    private ImageIcon loadCardImage(String name) {
        return new ImageIcon("assets/cards/" + name + ".png");
    }

    private boolean isGameWon() {
        for (CardUI card : cards) {
            if (!card.isMatched()) return false;
        }
        return true;
    }

    private void showWinDialog() {
        int option = JOptionPane.showOptionDialog(
                this,
                "🎉 Selamat! Kamu berhasil mencocokkan semua kartu!",
                "Kamu Menang!",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Main Lagi", "Kembali ke Menu"},
                "Main Lagi"
        );

        if (option == 0) {
            GameWindow.getInstance().showGamePanel1Player(difficulty);
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
package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.BufferedImage;

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
            turnLabel = new JLabel("👤 Giliran: Player 1");
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
            backIcon = new ImageIcon(backImgURL);
        } else {
            System.err.println("Nggak nemu file gambar: /assets/cards/card_back.png");
            backIcon = new ImageIcon(new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB));
        }
        for (String name : cardNames) {
            ImageIcon icon = loadCardImage(name);
            CardUI card = new CardUI(name, icon, backIcon);
            card.getButton().addActionListener(e -> handleCardClick(card));
            cards.add(card);
            gridPanel.add(card.getButton());
        }
        add(gridPanel, BorderLayout.CENTER);

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
        if (clicked.isMatched() || clicked.isFaceUp() || secondCard != null) return;

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
                    updateScoreAndTurn();
                }

                firstCard = null;
                secondCard = null;

                if (mode == 1 && isGameWon()) {
                    if (countdownTimer != null) countdownTimer.stop();
                    showWinDialog();
                }

            } else {
                Timer flipBackTimer = new Timer(1000, e -> {
                    firstCard.flipDown();
                    secondCard.flipDown();

                    if (mode == 2) {
                        currentPlayer = (currentPlayer == 1) ? 2 : 1;
                        updateScoreAndTurn();
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

                if (lives <= 0) {
                    countdownTimer.stop();
                    JOptionPane.showMessageDialog(this, "Game Over! Kamu kehabisan nyawa.");
                    GameWindow.getInstance().showMenu();
                } else {
                    timeLeft = 60;
                    timerLabel.setText("🕒 Timer: " + timeLeft + " detik");
                    JOptionPane.showMessageDialog(this, "Waktu Habis! Kamu kehilangan 1 nyawa.");
                }
            }
        });
        countdownTimer.start();
    }

    private void updateScoreAndTurn() {
        if (mode == 2) {
            turnLabel.setText("👤 Giliran: Player " + currentPlayer);
            scoreLabel.setText("Skor P1: " + scoreP1 + " | Skor P2: " + scoreP2);
        }
    }

    private List<String> generateCardPairs(int totalCards) {
        String[] possible = {
                "apple", "avocado", "carrot", "coffe",
                "hammy", "frog", "eskrim", "depe", "CUPCAKE",
                "jerapah", "mushroom", "heart", "penguin", "tomat", "watermelon",
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
        String[] extensions = {".png", ".jpg", ".jpeg"};
        int desiredWidth = 100;  // Lebar gambar yang kamu inginkan (sesuaikan)
        int desiredHeight = 100; // Tinggi gambar yang kamu inginkan (sesuaikan)

        for (String ext : extensions) {
            String path = "/assets/cards/" + name + ext;
            java.net.URL imgURL = getClass().getResource(path);

            if (imgURL != null) {
                ImageIcon originalIcon = new ImageIcon(imgURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        }

        System.err.println("Nggak nemu file gambar untuk: " + name + " (sudah coba .png, .jpg, .jpeg)");
        BufferedImage placeholder = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, desiredWidth, desiredHeight);
        g.dispose();
        return new ImageIcon(placeholder);
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
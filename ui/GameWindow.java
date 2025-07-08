package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameWindow extends JFrame {
    private static GameWindow instance;

    // Variabel untuk CardLayout
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public static GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;
    }

    private GameWindow() {
        setTitle("Memorizing Card");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);

        // Inisialisasi CardLayout dan panel utama yang akan menampung semua "layar"
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tambahkan panel-panel yang statis (selalu ada)
        mainPanel.add(new PlayPanel(), "play");
        mainPanel.add(new ModeSelectionPanel(), "mode");

        // Set panel utama sebagai content pane dari JFrame
        setContentPane(mainPanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    int choice = JOptionPane.showConfirmDialog(
                            GameWindow.this,
                            "Apakah kamu yakin ingin keluar dari permainan?",
                            "Konfirmasi Keluar",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (choice == JOptionPane.YES_OPTION) {
                        System.exit(0); // Tutup aplikasi jika user memilih "Yes"
                    }
                }
            }
        });

        // Atur agar bisa menerima input keyboard
        setFocusable(true);

        showPlayPanel(); // Tampilkan layar awal
        setVisible(true);
    }

    // Satu method utama untuk berpindah panel
    private void showCard(String cardName) {
        cardLayout.show(mainPanel, cardName);
        // Pastikan window utama tetap fokus untuk menangkap event 'Escape'
        requestFocusInWindow();
    }

    // Method-method lama sekarang memanggil showCard atau membuat panel baru
    public void showPlayPanel() {
        showCard("play");
    }

    public void showModeSelection() {
        showCard("mode");
    }

    public void showDifficultySelection(int mode) {
        // Panel ini dinamis, jadi kita buat baru setiap kali dibutuhkan
        DifficultySelectionPanel difficultyPanel = new DifficultySelectionPanel(mode);
        mainPanel.add(difficultyPanel, "difficulty");
        showCard("difficulty");
    }

    public void showPlayerNameInput(int mode, int difficulty) {
        // Panel ini juga dinamis
        PlayerNamePanel playerNamePanel = new PlayerNamePanel(mode, difficulty);
        mainPanel.add(playerNamePanel, "player_name");
        showCard("player_name");
    }

    public void showMenu() {
        // Kembali ke menu utama (PlayPanel)
        showCard("play");
    }

    public void showGame(int mode, int difficulty, String player1Name, String player2Name) {
        // GamePanel selalu dibuat baru setiap game dimulai
        GamePanel gamePanel = new GamePanel(mode, difficulty, player1Name, player2Name);
        mainPanel.add(gamePanel, "game");
        showCard("game");
    }

    public void showLeaderboard() {
        // Panel ini bisa dibuat baru setiap kali atau dibuat sekali di konstruktor
        LeaderboardPanel leaderboardPanel = new LeaderboardPanel();
        mainPanel.add(leaderboardPanel, "leaderboard");
        showCard("leaderboard");
    }
}
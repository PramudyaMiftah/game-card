package ui;

import javax.swing.*;

public class DifficultySelectionPanel extends AbstractMenuPanel {

    private final int mode; // Simpan mode yang dipilih dari layar sebelumnya

    public DifficultySelectionPanel(int mode) {
        // 1. Panggil konstruktor parent
        super("menu-utama-sakura.gif"); // Ganti nama GIF jika perlu
        this.mode = mode;

        // 2. Tentukan pilihan menu untuk panel ini
        this.menuOptions = new String[]{"Easy", "Medium", "Hard", "Kembali"};
    }

    // 3. Implementasikan apa yang terjadi saat Enter ditekan
    @Override
    protected void onEnterPressed() {
        switch (selectedIndex) {
            case 0: // Easy
                GameWindow.getInstance().showPlayerNameInput(this.mode, 0);
                break;
            case 1: // Medium
                GameWindow.getInstance().showPlayerNameInput(this.mode, 1);
                break;
            case 2: // Hard
                GameWindow.getInstance().showPlayerNameInput(this.mode, 2);
                break;
            case 3: // Kembali
                GameWindow.getInstance().showModeSelection();
                break;
        }
    }
}
package ui;

import javax.swing.*;

public class PlayPanel extends AbstractMenuPanel {

    public PlayPanel() {
        super("menu-utama-sakura.gif");
        this.title = "MEMORIZE CARD";
        this.subtitle = "MAIN MENU";
        this.menuOptions = new String[]{"Play", "High Scores", "Keluar"};
    }

    @Override
    protected void onEnterPressed() {
        switch (selectedIndex) {
            case 0: // Play
                GameWindow.getInstance().showModeSelection();
                break;
            case 1: // High Scores
                GameWindow.getInstance().showLeaderboard();
                break;
            case 2: // Keluar
                System.exit(0);
                break;
        }
    }
}
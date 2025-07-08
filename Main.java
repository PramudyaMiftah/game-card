import ui.GameWindow;
import ui.Menu;
import assetsmanager.SoundManager;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SoundManager.loadSounds();
        Menu.loadFonts();
        SwingUtilities.invokeLater(() -> GameWindow.getInstance().setVisible(true));
    }
}
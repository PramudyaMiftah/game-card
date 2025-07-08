import ui.GameWindow;
import ui.Menu;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Menu.loadFonts();
        SwingUtilities.invokeLater(() -> GameWindow.getInstance().setVisible(true));
    }
}
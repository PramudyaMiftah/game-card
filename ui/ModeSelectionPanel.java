package ui;

public class ModeSelectionPanel extends AbstractMenuPanel {

    public ModeSelectionPanel() {
        super("menu-utama-sakura.gif");
        this.title = "MEMORIZE CARD";
        this.subtitle = "PILIH MODE";

        // 2. Tentukan pilihan menu untuk panel ini
        this.menuOptions = new String[]{"1 Player", "2 Players", "Kembali"};
    }

    // 3. Implementasikan apa yang terjadi saat Enter ditekan
    @Override
    protected void onEnterPressed() {
        switch (selectedIndex) {
            case 0: // 1 Player
                GameWindow.getInstance().showDifficultySelection(1);
                break;
            case 1: // 2 Players
                GameWindow.getInstance().showDifficultySelection(2);
                break;
            case 2: // Kembali
                GameWindow.getInstance().showPlayPanel();
                break;
        }
    }
}
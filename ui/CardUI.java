package ui;

import javax.swing.*;

public class CardUI {
    private String name;
    private ImageIcon frontIcon;
    private static final ImageIcon backIcon = new ImageIcon("assets/cards/apple.png");

    private JButton button;
    private boolean matched = false;
    private boolean faceUp = false;

    public CardUI(String name, ImageIcon frontIcon) {
        this.name = name;
        this.frontIcon = frontIcon;
        this.button = new JButton();
        flipDown(); // tampilan awal = tertutup
    }

    public String getName() {
        return name;
    }

    public JButton getButton() {
        return button;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void flipUp() {
        faceUp = true;
        button.setIcon(frontIcon);
    }

    public void flipDown() {
        faceUp = false;
        button.setIcon(backIcon);
    }
}

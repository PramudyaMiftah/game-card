// Buat file baru bernama CardUI.java
package ui;

import javax.swing.*;

public class CardUI {
    private String name;
    private JButton button;
    private ImageIcon iconFront; // Gambar depan (isi kartu)
    private ImageIcon iconBack;  // Gambar belakang (kartu tertutup)
    private boolean isFaceUp = false;
    private boolean isMatched = false;

    public CardUI(String name, ImageIcon front, ImageIcon back) {
        this.name = name;
        this.iconFront = front;
        this.iconBack = back;
        this.button = new JButton(iconBack); // Awalnya tampilkan gambar belakang
    }

    public void flipUp() {
        button.setIcon(iconFront);
        isFaceUp = true;
    }

    public void flipDown() {
        button.setIcon(iconBack);
        isFaceUp = false;
    }

    // --- Getter dan Setter lainnya ---
    public JButton getButton() { return button; }
    public String getName() { return name; }
    public boolean isFaceUp() { return isFaceUp; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) {
        isMatched = matched;
        if (matched) {
            button.setEnabled(false); // Matikan tombol jika sudah cocok
        }
    }
}
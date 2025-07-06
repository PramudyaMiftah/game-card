// Buat file baru bernama CardUI.java
package ui;

import javax.swing.*;

public class CardUI {
    private final String name;
    private final JButton button;
    private final ImageIcon iconFront; // Gambar depan (isi kartu)
    private final ImageIcon iconBack;  // Gambar belakang (kartu tertutup)
    private boolean isMatched = false;

    public CardUI(String name, ImageIcon front, ImageIcon back) {
        this.name = name;
        this.iconFront = front;
        this.iconBack = back;
        this.button = new JButton(iconBack); // Awalnya tampilkan gambar belakang
    }

    public void flipUp() {
        button.setIcon(iconFront);
    }

    public void flipDown() {
        button.setIcon(iconBack);
    }

    // --- Getter dan Setter lainnya ---
    public JButton getButton() { return button; }
    public String getName() { return name; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) {
        isMatched = matched;
        if (matched) {
            button.setEnabled(false); // Matikan tombol jika sudah cocok
        }
    }
}
// File baru: ui/AbstractMenuPanel.java
package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import assetsmanager.SoundManager;
import assetsmanager.VideoManager;

public abstract class AbstractMenuPanel extends JPanel {

    protected ImageIcon backgroundImage;
    protected ImageIcon cursorImage;
    protected int selectedIndex = 0;
    protected String[] menuOptions; // Akan diisi oleh kelas turunan
    protected String title;
    protected String subtitle;

    public AbstractMenuPanel(String backgroundGifName) {
        // Load background dan kursor
        this.backgroundImage = VideoManager.loadImageIcon(backgroundGifName);
        this.cursorImage = VideoManager.loadImageIcon("cursor.png"); // Pastikan ada file cursor.png di assets/gifs

        // Wajib agar Key Bindings berfungsi
        this.setFocusable(true);
        this.requestFocusInWindow();

        setupKeyBindings();
    }

    // Method abstract yang WAJIB diimplementasikan oleh kelas turunan
    protected abstract void onEnterPressed();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (title != null) {
            g2d.setFont(Menu.DISPLAY_FONT_XLARGE);
            FontMetrics fm = g2d.getFontMetrics();
            int titleWidth = fm.stringWidth(title);
            int titleX = (getWidth() - titleWidth) / 2;
            int titleY = 270;
            drawTextWithOutline(g2d, title, titleX, titleY, Menu.WARNA_JUDUL, Color.BLACK, 4);
        }

        if (subtitle != null) {
            g2d.setFont(Menu.DISPLAY_FONT_LARGE);
            FontMetrics fm = g2d.getFontMetrics();
            int subtitleWidth = fm.stringWidth(subtitle);
            int subtitleX = (getWidth() - subtitleWidth) / 2;
            int subtitleY = 600;
            drawTextWithOutline(g2d, subtitle, subtitleX, subtitleY, Menu.WARNA_SUBJUDUL, Color.BLACK, 2);
        }

        int startY = 650;
        int lineHeight = 50;

        for (int i = 0; i < menuOptions.length; i++) {
            String text = menuOptions[i];

            Font font = (i == selectedIndex) ? Menu.DISPLAY_FONT_LARGE : Menu.DISPLAY_FONT_MEDIUM;
            g2d.setFont(font);

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int x = (getWidth() - textWidth) / 2;
            int y = startY + (i * lineHeight);

            // Jika ini adalah pilihan yang aktif, gambar kursor dan gunakan warna judul
            if (i == selectedIndex) {
                if (cursorImage != null) {
                    g2d.drawImage(cursorImage.getImage(), x - 50, y - fm.getAscent() / 2 - 25, 50, 50, null);
                }
                drawTextWithOutline(g2d, text, x, y, Menu.WARNA_OPTIONS, Color.BLACK, 2);
            } else {
                drawTextWithOutline(g2d, text, x, y, Menu.WARNA_TEKS_UTAMA, Color.BLACK, 2);
            }
        }
    }

    private void drawTextWithOutline(Graphics g, String text, int x, int y, Color textColor, Color outlineColor, int outlineThickness) {
        g.setColor(outlineColor);
        g.drawString(text, x - outlineThickness, y - outlineThickness);
        g.drawString(text, x + outlineThickness, y - outlineThickness);
        g.drawString(text, x - 1, y + outlineThickness);
        g.drawString(text, x + outlineThickness, y + outlineThickness);

        g.setColor(textColor);
        g.drawString(text, x, y);
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "downAction");
        actionMap.put("downAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                SoundManager.playSound("hovere.wav");
                selectedIndex = (selectedIndex + 1) % menuOptions.length;
                repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("UP"), "upAction");
        actionMap.put("upAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                SoundManager.playSound("hovere.wav");
                selectedIndex = (selectedIndex - 1 + menuOptions.length) % menuOptions.length;
                repaint();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "enterAction");
        actionMap.put("enterAction", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                SoundManager.playSound("click.wav");
                onEnterPressed(); // Panggil method abstract
            }
        });
    }
}
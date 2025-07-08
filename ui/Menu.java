package ui; // Sesuaikan dengan package kamu

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;

public class Menu {

    public static Font PIXEL_FONT_BASE; // Font dasar yang dimuat
    public static Font DISPLAY_FONT_LARGE;
    public static Font DISPLAY_FONT_MEDIUM;
    public static Font DISPLAY_FONT_BUTTON;

    // Static initializer block: Kode ini akan dijalankan satu kali saat kelas dimuat
    static {
        try {
            PIXEL_FONT_BASE = Font.createFont(Font.TRUETYPE_FONT, Menu.class.getResourceAsStream("/assets/fonts/mario-kart.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(PIXEL_FONT_BASE);

            DISPLAY_FONT_LARGE = PIXEL_FONT_BASE.deriveFont(Font.BOLD, 36f);
            DISPLAY_FONT_MEDIUM = PIXEL_FONT_BASE.deriveFont(Font.BOLD, 24f);
            DISPLAY_FONT_BUTTON = PIXEL_FONT_BASE.deriveFont(Font.BOLD, 20f);

        } catch (FontFormatException | IOException e) {
            System.err.println("Gagal memuat font pixel art. Menggunakan font default Monospaced.");
            // Font fallback jika gagal, ukuran disesuaikan
            PIXEL_FONT_BASE = new Font("Monospaced", Font.BOLD, 24);
            DISPLAY_FONT_LARGE = new Font("Monospaced", Font.BOLD, 36);
            DISPLAY_FONT_MEDIUM = new Font("Monospaced", Font.BOLD, 24);
            DISPLAY_FONT_BUTTON = new Font("Monospaced", Font.BOLD, 20);
            e.printStackTrace();
        }
    }

    // Metode ini tidak wajib, tapi bisa dipakai untuk memastikan font sudah dimuat
    public static void loadFonts() {
        // Cukup panggil saja metode ini sekali di awal aplikasi (misal di main method GameWindow)
        // karena static block akan otomatis dieksekusi saat kelas pertama kali diakses.
        System.out.println("Font retro dimuat.");
    }
}
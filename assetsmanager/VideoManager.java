package assetsmanager;

import javax.swing.ImageIcon;
import java.net.URL;

public class VideoManager {

    public static ImageIcon loadImageIcon(String fileName) {
        try {
            URL imgURL = VideoManager.class.getResource("/assets/gif/menu-utama.gif");
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("File gambar tidak ditemukan: " + fileName);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar: " + fileName);
            e.printStackTrace();
            return null;
        }
    }
}
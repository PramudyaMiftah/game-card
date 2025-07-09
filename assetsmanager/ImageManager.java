package assetsmanager;

import javax.swing.ImageIcon;
import java.net.URL;

public class ImageManager {

    public static ImageIcon loadImageIcon(String fileName) {
        try {
            URL imgURL = ImageManager.class.getResource("/assets/image/" + fileName);
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
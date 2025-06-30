package logic;

public class Card {
    public String isi;
    public boolean terbuka = false;
    public boolean ditemukan = false;

    public Card(String isi) {
        this.isi = isi;
    }

    public boolean cocokDengan(Card lain) {
        return this.isi.equals(lain.isi);
    }
}
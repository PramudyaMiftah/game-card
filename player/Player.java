package player;

public class Player {
    public String nama;
    public int skor;
    public int nyawa;

    public Player(String nama) {
        this.nama = nama;
        this.skor = 0;
        this.nyawa = 3;
    }

    public void tambahSkor() {
        this.skor++;
    }

    public void kurangiNyawa() {
        if (nyawa > 0) nyawa--;
    }
}
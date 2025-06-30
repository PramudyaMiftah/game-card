package data;

public class ScoreNode {
    public String nama;
    public int skor;
    public ScoreNode kiri, kanan;

    public ScoreNode(String nama, int skor) {
        this.nama = nama;
        this.skor = skor;
    }
}
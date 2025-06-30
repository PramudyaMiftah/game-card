package logic;

public class CardMatcher {
    public static boolean cocok(Card a, Card b) {
        return a.isi.equals(b.isi);
    }
}
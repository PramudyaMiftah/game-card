package logic;

import java.util.LinkedList;

public class FoundList {
    private LinkedList<Card> ditemukan = new LinkedList<>();

    public void tambah(Card card) {
        ditemukan.add(card);
    }

    public boolean sudahDitemukan(Card card) {
        return ditemukan.contains(card);
    }
}
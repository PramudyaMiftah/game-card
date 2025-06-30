package logic;

public class CardGrid {
    public Card[][] grid;

    public CardGrid(int baris, int kolom, String[] isiKartu) {
        grid = new Card[baris][kolom];
        int index = 0;
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                grid[i][j] = new Card(isiKartu[index++]);
            }
        }
    }

    public Card getCard(int i, int j) {
        return grid[i][j];
    }
}
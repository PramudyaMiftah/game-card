package leaderboard;

public class ScoreEntry implements Comparable<ScoreEntry> {
    private final String name;
    private final int score;

    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        // Sort descending by score
        return Integer.compare(other.score, this.score);
    }
}

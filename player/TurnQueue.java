package player;

import java.util.LinkedList;
import java.util.Queue;

public class TurnQueue {
    private Queue<Player> queue = new LinkedList<>();

    public void tambahPemain(Player p) {
        queue.add(p);
    }

    public Player giliranBerikutnya() {
        Player p = queue.poll();
        queue.add(p);
        return p;
    }

    public Player pemainSekarang() {
        return queue.peek();
    }
}
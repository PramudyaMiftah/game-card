package logic;

import java.util.*;

public class HintGraph {
    private Map<Card, List<Card>> graph = new HashMap<>();

    public void tambahNode(Card card) {
        graph.putIfAbsent(card, new ArrayList<>());
    }

    public void tambahEdge(Card a, Card b) {
        graph.get(a).add(b);
        graph.get(b).add(a);
    }

    public Card cariHint() {
        for (Card a : graph.keySet()) {
            for (Card b : graph.get(a)) {
                if (!a.terbuka && !b.terbuka && a.cocokDengan(b)) {
                    return a;
                }
            }
        }
        return null;
    }
}
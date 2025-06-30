package logic;

import java.util.Stack;

public class CardStack {
    private Stack<Card> stack = new Stack<>();

    public void push(Card card) {
        if (stack.size() >= 2) stack.clear();
        stack.push(card);
    }

    public Card[] getDuaKartu() {
        if (stack.size() < 2) return null;
        return new Card[]{stack.get(0), stack.get(1)};
    }

    public void clear() {
        stack.clear();
    }
}
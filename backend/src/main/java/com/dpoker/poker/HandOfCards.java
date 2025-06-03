package com.dpoker.poker;

public class HandOfCards implements Comparable<HandOfCards> {

    private Card[] handCards;
    private Card[] communityCards;


    private int getHandScore() {
        return 0; // Placeholder for hand score calculation logic
    }

    private HandType getHandType() {
        // Placeholder for hand type determination logic
        return HandType.HIGH_CARD;
    }

    @Override
    public int compareTo(HandOfCards other) {
        return Integer.compare(this.getHandScore(), other.getHandScore());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HandOfCards{");
        sb.append("handCards=");
        for (Card card : handCards) {
            sb.append(card.toString()).append(", ");
        }
        sb.append("communityCards=");
        for (Card card : communityCards) {
            sb.append(card.toString()).append(", ");
        }
        sb.append('}');
        return sb.toString();
    }

}

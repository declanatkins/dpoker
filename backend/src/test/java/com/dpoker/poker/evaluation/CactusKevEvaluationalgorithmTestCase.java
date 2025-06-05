package com.dpoker.poker.evaluation;

import com.dpoker.poker.Card;
import com.dpoker.poker.Card.Rank;
import com.dpoker.poker.Card.Suit;
import com.dpoker.poker.HandType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CactusKevEvaluationalgorithmTestCase {

    private static CactusKevEvaluationAlgorithm evaluator;

    @BeforeAll
    public static void setup() {
        evaluator = new CactusKevEvaluationAlgorithm();
    }

    private Card card(Rank rank, Suit suit) {
        return new Card(suit, rank);
    }

    private void assertHandType(HandType expected, Card... cards) {
        assertEquals(expected, evaluator.getHandType(cards),
            "Expected " + expected + " but got something else for hand: " + List.of(cards));
    }

    @Test
    public void testHighCard() {
        assertHandType(HandType.HIGH_CARD,
            card(Rank.TWO, Suit.HEARTS),
            card(Rank.FIVE, Suit.DIAMONDS),
            card(Rank.SEVEN, Suit.SPADES),
            card(Rank.JACK, Suit.CLUBS),
            card(Rank.KING, Suit.HEARTS)
        );
    }

    @Test
    public void testPairOfTwos() {
        assertHandType(HandType.ONE_PAIR,
            card(Rank.TWO, Suit.HEARTS),
            card(Rank.TWO, Suit.SPADES),
            card(Rank.FIVE, Suit.CLUBS),
            card(Rank.SEVEN, Suit.DIAMONDS),
            card(Rank.NINE, Suit.HEARTS)
        );
    }

    @Test
    public void testTwoPair() {
        assertHandType(HandType.TWO_PAIR,
            card(Rank.THREE, Suit.HEARTS),
            card(Rank.THREE, Suit.DIAMONDS),
            card(Rank.SIX, Suit.CLUBS),
            card(Rank.SIX, Suit.SPADES),
            card(Rank.KING, Suit.HEARTS)
        );
    }

    @Test
    public void testThreeOfAKind() {
        assertHandType(HandType.THREE_OF_A_KIND,
            card(Rank.THREE, Suit.HEARTS),
            card(Rank.THREE, Suit.DIAMONDS),
            card(Rank.THREE, Suit.SPADES),
            card(Rank.FIVE, Suit.HEARTS),
            card(Rank.SEVEN, Suit.CLUBS)
        );
    }

    @Test
    public void testStraightWheel() {
        assertHandType(HandType.STRAIGHT,
            card(Rank.ACE, Suit.HEARTS),
            card(Rank.TWO, Suit.SPADES),
            card(Rank.THREE, Suit.CLUBS),
            card(Rank.FOUR, Suit.DIAMONDS),
            card(Rank.FIVE, Suit.HEARTS)
        );
    }

    @Test
    public void testStraightNormal() {
        assertHandType(HandType.STRAIGHT,
            card(Rank.FIVE, Suit.HEARTS),
            card(Rank.SIX, Suit.SPADES),
            card(Rank.SEVEN, Suit.CLUBS),
            card(Rank.EIGHT, Suit.DIAMONDS),
            card(Rank.NINE, Suit.HEARTS)
        );
    }

    @Test
    public void testFlush() {
        assertHandType(HandType.FLUSH,
            card(Rank.TWO, Suit.HEARTS),
            card(Rank.FIVE, Suit.HEARTS),
            card(Rank.SEVEN, Suit.HEARTS),
            card(Rank.NINE, Suit.HEARTS),
            card(Rank.JACK, Suit.HEARTS)
        );
    }

    @Test
    public void testFullHouse() {
        assertHandType(HandType.FULL_HOUSE,
            card(Rank.TWO, Suit.HEARTS),
            card(Rank.TWO, Suit.SPADES),
            card(Rank.TWO, Suit.DIAMONDS),
            card(Rank.THREE, Suit.CLUBS),
            card(Rank.THREE, Suit.HEARTS)
        );
    }

    @Test
    public void testFourOfAKind() {
        assertHandType(HandType.FOUR_OF_A_KIND,
            card(Rank.TWO, Suit.HEARTS),
            card(Rank.TWO, Suit.SPADES),
            card(Rank.TWO, Suit.CLUBS),
            card(Rank.TWO, Suit.DIAMONDS),
            card(Rank.KING, Suit.HEARTS)
        );
    }

    @Test
    public void testStraightFlush() {
        assertHandType(HandType.STRAIGHT_FLUSH,
            card(Rank.FIVE, Suit.HEARTS),
            card(Rank.SIX, Suit.HEARTS),
            card(Rank.SEVEN, Suit.HEARTS),
            card(Rank.EIGHT, Suit.HEARTS),
            card(Rank.NINE, Suit.HEARTS)
        );
    }

    @Test
    public void testRoyalFlush() {
        assertHandType(HandType.ROYAL_FLUSH,
            card(Rank.TEN, Suit.SPADES),
            card(Rank.JACK, Suit.SPADES),
            card(Rank.QUEEN, Suit.SPADES),
            card(Rank.KING, Suit.SPADES),
            card(Rank.ACE, Suit.SPADES)
        );
    }
}

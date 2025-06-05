package com.dpoker.poker.evaluation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.dpoker.poker.Card;
import com.dpoker.poker.HandType;

public class CactusKevEvaluationAlgorithm implements EvaluationAlgorithm {

    /*
     * This implementation is based on the code in the blogpost:
     * https://joshgoestoflatiron.medium.com/july-17-evaluating-poker-hands-with-lookup-tables-and-perfect-hashing-c21e056da130
     *
     * It uses precomputed lookup tables and a perfect hashing algorithm to evaluate poker hands.
     */

    private static final int[] HASH_ADJUST = loadDataFile("data/hash_adjust.dat");
    private static final int[] HASH_VALUES = loadDataFile("data/hash_values.dat");
    private static final int[] FLUSH_LOOKUP_TABLE = loadDataFile("data/flush_lookup.dat");
    private static final int[] FIVE_UNIQUE_CARDS_LOOKUP_TABLE = loadDataFile("data/five_unique_cards.dat");

    private static final int HIGH_CARD_LOWER_BOUND = 6816;
    private static final int ONE_PAIR_LOWER_BOUND = 3326;
    private static final int TWO_PAIR_LOWER_BOUND = 2468;
    private static final int THREE_OF_A_KIND_LOWER_BOUND = 1610;
    private static final int STRAIGHT_LOWER_BOUND = 1600;
    private static final int FLUSH_LOWER_BOUND = 323;
    private static final int FULL_HOUSE_LOWER_BOUND = 167;
    private static final int FOUR_OF_A_KIND_LOWER_BOUND = 11;
    private static final int STRAIGHT_FLUSH_LOWER_BOUND = 2;


    private static int[] loadDataFile(String filename) {
        List<Integer> dataList = new ArrayList<>();

        try (
            InputStream is = CactusKevEvaluationAlgorithm.class.getClassLoader().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is))
        ) {

            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + filename);
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                for (String part : parts) {
                    dataList.add(Integer.parseInt(part.trim()));
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error reading data file: " + filename, e);
        }

        return dataList.stream().mapToInt(Integer::intValue).toArray();
    }

    private static final Map<Card.Rank, Integer> RANK_PRIME_VALUE = new EnumMap<>(
        Map.ofEntries(
            Map.entry(Card.Rank.TWO, 2),
            Map.entry(Card.Rank.THREE, 3),
            Map.entry(Card.Rank.FOUR, 5),
            Map.entry(Card.Rank.FIVE, 7),
            Map.entry(Card.Rank.SIX, 11),
            Map.entry(Card.Rank.SEVEN, 13),
            Map.entry(Card.Rank.EIGHT, 17),
            Map.entry(Card.Rank.NINE, 19),
            Map.entry(Card.Rank.TEN, 23),
            Map.entry(Card.Rank.JACK, 29),
            Map.entry(Card.Rank.QUEEN, 31),
            Map.entry(Card.Rank.KING, 37),
            Map.entry(Card.Rank.ACE, 41)
        )
    );

    private static final Map<Card.Suit, Integer> SUIT_VALUE = new EnumMap<>(
        Map.ofEntries(
            Map.entry(Card.Suit.HEARTS, 1),
            Map.entry(Card.Suit.DIAMONDS, 2),
            Map.entry(Card.Suit.CLUBS, 4),
            Map.entry(Card.Suit.SPADES, 8)
        )
    );

    private int getCardValue(Card card) {
        int rankOrdinal = card.getRank().ordinal();
        int rankValue = RANK_PRIME_VALUE.get(card.getRank());
        int suitValue = SUIT_VALUE.get(card.getSuit());

        return rankValue | rankOrdinal << 8 | suitValue << 12 | ((1 << rankOrdinal) << 16);
    }

    private boolean isFlush(Card[] cards) {
        int total = 0xF000;
        for (Card card : cards) {
            total &= this.getCardValue(card);
        }
        return total != 0;
    }

    private int getFlushBitPattern(Card[] cards) {
        int total = 0;
        for (Card card : cards) {
            total |= this.getCardValue(card);
        }
        return total >> 16;
    }

    private int getFlushRank(Card[] cards) {
        return FLUSH_LOOKUP_TABLE[getFlushBitPattern(cards)];
    }

    private int getFiveUniqueCardsRank(Card[] cards) {
        return FIVE_UNIQUE_CARDS_LOOKUP_TABLE[getFlushBitPattern(cards)];
    }

    private int getPrimeMultiplicand(Card[] hand) {
        int total = 1;
        for (Card card : hand) {
            total *= (this.getCardValue(card) & 0xFF);
        }
        return total;
    }

    private int findFast(int primeMultiplicand) {
        primeMultiplicand += 0xe91aaa35;
        primeMultiplicand ^= primeMultiplicand >>> 16;
        primeMultiplicand += primeMultiplicand << 8;
        primeMultiplicand ^= primeMultiplicand >>> 4;
        int adjust = (primeMultiplicand + (primeMultiplicand << 2)) >>> 19;
        return adjust ^ HASH_ADJUST[(primeMultiplicand >>> 8) & 0x1ff];
    }

    private int getHandNumericalValue(Card[] hand) {
        if (this.isFlush(hand)) {
            return this.getFlushRank(hand);
        }
        int fiveUniqueCardsRank = this.getFiveUniqueCardsRank(hand);
        if (fiveUniqueCardsRank != 0) {
            return fiveUniqueCardsRank;
        }
        return HASH_VALUES[this.findFast(this.getPrimeMultiplicand(hand))];
    }

    @Override
    public HandType getHandType(Card[] hand) {
        int handValue = this.getHandNumericalValue(hand);
        if (handValue >= HIGH_CARD_LOWER_BOUND) {
            return HandType.HIGH_CARD;
        }
        if (handValue >= ONE_PAIR_LOWER_BOUND) {
            return HandType.ONE_PAIR;
        }
        if (handValue >= TWO_PAIR_LOWER_BOUND) {
            return HandType.TWO_PAIR;
        }
        if (handValue >= THREE_OF_A_KIND_LOWER_BOUND) {
            return HandType.THREE_OF_A_KIND;
        }
        if (handValue >= STRAIGHT_LOWER_BOUND) {
            return HandType.STRAIGHT;
        }
        if (handValue >= FLUSH_LOWER_BOUND) {
            return HandType.FLUSH;
        }
        if (handValue >= FULL_HOUSE_LOWER_BOUND) {
            return HandType.FULL_HOUSE;
        }
        if (handValue >= FOUR_OF_A_KIND_LOWER_BOUND) {
            return HandType.FOUR_OF_A_KIND;
        }
        if (handValue >= STRAIGHT_FLUSH_LOWER_BOUND) {
            return HandType.STRAIGHT_FLUSH;
        }
        return HandType.ROYAL_FLUSH;
    }

    @Override
    public int compareHands(Card[] hand1, Card[] hand2) {
        return Integer.compare(
            this.getHandNumericalValue(hand1),
            this.getHandNumericalValue(hand2)
        );
    }

    @Override
    public List<Card[]> sortHands(List<Card[]> hands) {
        return hands.stream()
            .sorted(this::compareHands)
            .toList();
    }

}

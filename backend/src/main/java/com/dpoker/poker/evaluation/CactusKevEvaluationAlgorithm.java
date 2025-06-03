package com.dpoker.poker.evaluation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.dpoker.poker.Card;
import com.dpoker.poker.HandType;

public class CactusKevEvaluationAlgorithm implements EvaluationAlgorithm {

    private static final int[] HASH_ADJUST = loadDataFile("data/hash_adjust.dat");
    private static final int[] HASH_VALUES = loadDataFile("data/hash_values.dat");
    private static final int[] FLUSH_LOOKUP_TABLE = loadDataFile("data/flush_lookup.dat");
    private static final int[] FIVE_UNIQUE_CARDS_LOOKUP_TABLE = loadDataFile("data/five_unique_cards.dat");

    private static int[] loadDataFile(String filename) {
        int[] data = null;

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
        int rankValue = RANK_PRIME_VALUE.get(card.getRank());
        int suitValue = SUIT_VALUE.get(card.getSuit());

        return rankValue | rankValue << 8 | suitValue << 12 | ((1 << rankValue) << 16);
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
        return total << 16;
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

    @Override
    public HandType getHandType(Card[] hand) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHandType'");
    }

    @Override
    public String getHandDescription(Card[] hand) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHandDescription'");
    }

    @Override
    public int compareHands(Card[] hand1, Card[] hand2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareHands'");
    }

    @Override
    public List<Card[]> sortHands(List<Card[]> hands) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sortHands'");
    }

}


/*
 * exports.primeMultiplicand = hand => hand.reduce( ( total, card ) => total * ( card & 0xFF ), 1 );
// same as exports.primeMultiplicand = hand => ( hand[ 0 ] & 0xFF ) * ( hand[ 1] & 0xFF ) * ( hand[ 2 ] & 0xFF ) * ( hand[ 3 ] & 0xFF ) * ( hand[ 4 ] & 0xFF );
exports.findFast = u => {
  u += 0xe91aaa35;
  u ^= u >>> 16;
  u += u << 8;
  u ^= u >>> 4;
  let a  = ( u + ( u << 2 ) ) >>> 19;
  return a ^ hashAdjust[ ( u >>> 8 ) & 0x1ff ];
};
exports.handRank = hand => {
  if ( this.flush( hand ) ) return this.flushRank( hand );
  let fiveUniqueCardsRank = this.fiveUniqueCardsRank( hand );
  if ( fiveUniqueCardsRank ) return fiveUniqueCardsRank;
  return hashValues[ this.findFast( this.primeMultiplicand( hand ) ) ];
};
 */

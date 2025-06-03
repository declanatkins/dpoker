package com.dpoker.poker.evaluation;

import java.util.List;

import com.dpoker.poker.Card;
import com.dpoker.poker.HandType;

public interface EvaluationAlgorithm {

    public List<Card[]> sortHands(List<Card[]> hands);

    public HandType getHandType(Card[] hand);

    public String getHandDescription(Card[] hand);

    public int compareHands(Card[] hand1, Card[] hand2);

}

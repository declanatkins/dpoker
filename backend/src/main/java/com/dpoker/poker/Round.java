package com.dpoker.poker;

import java.util.List;

import com.dpoker.poker.player.Player;

public class Round {

    public enum RoundState {
        DEALING,
        PRE_FLOP,
        FLOP,
        TURN,
        RIVER,
        SHOWDOWN
    };

    private RoundState state;
    private PotOfChips pot;
    private DeckOfCards deck;
    private List<Player> players;
    private int smallBlind;
    private int bigBlind;

    public Round(List<Player> players, int smallBlind, int bigBlind) {
        this.players = players;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.state = RoundState.DEALING;
        this.pot = new PotOfChips();
        this.deck = new DeckOfCards();
    }

}

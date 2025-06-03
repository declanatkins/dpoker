package com.dpoker.poker.player;

import com.dpoker.poker.player.strategy.PlayerStrategy;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Player {

    private String name;
    private ChipStack chipStack;

    protected PlayerStrategy strategy;
    protected HandOfCards handOfCards;

    public void postBlind(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot post a negative blind amount");
        }
        if (!chipStack.canAfford(amount)) {
            throw new IllegalArgumentException("Insufficient chips to post blind");
        }
        chipStack.deduct(amount);
    }


}

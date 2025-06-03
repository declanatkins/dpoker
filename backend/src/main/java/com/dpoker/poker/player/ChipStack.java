package com.dpoker.poker.player;


public class ChipStack {

    private int amount;

    public ChipStack(int initialAmount) {
        this.amount = initialAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void add(int chips) {
        if (chips < 0) {
            throw new IllegalArgumentException("Cannot add negative chips");
        }
        this.amount += chips;
    }

    public void deduct(int chips) {
        if (chips < 0) {
            throw new IllegalArgumentException("Cannot deduct negative chips");
        }
        if (chips > amount) {
            throw new IllegalArgumentException("Cannot deduct more chips than available");
        }
        this.amount -= chips;
    }

    public boolean canAfford(int chips) {
        if (chips < 0) {
            throw new IllegalArgumentException("Cannot check affordability for negative chips");
        }
        return this.amount >= chips;
    }
}

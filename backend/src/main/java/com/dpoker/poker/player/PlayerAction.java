package com.dpoker.poker.player;

public enum PlayerAction {
    FOLD("Fold"),
    CHECK("Check"),
    CALL("Call"),
    RAISE("Raise"),
    ALL_IN("All In");

    private final String action;

    PlayerAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}

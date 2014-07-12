package com.example.simcards;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lauren on 7/11/2014.
 */
public class Player {

    //Represents the user's pile of cards.
    private ArrayList<Card> pile;

    //Is the player still in the game or not?
    private boolean status;

    //The game that the user is interacting with.
    private Game game;

    public Player(Game game) {
        this.game = game;
        pile = new ArrayList<Card>();
    }

    public void addCard(Card card) {
        pile.add(card);
    }

    public boolean getStatus() {
        return status;
    }

    //Loser gets kicked out of game.
    public void kickOut() {
        status = false;
    }

    //YOU WIN!!!
    public void celebrate() {}
}

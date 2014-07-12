package com.example.simcards;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lauren on 7/11/2014.
 */
public class Player {

    //Represents the user's pile of cards.
    private List<Card> pile;

    //Is the player still in the game or not?
    private boolean status;

    //Optional attribute to represent the number of points
    private int points;

    public Player() {
        pile = new ArrayList<Card>();
        status = true;
    }

    public void addCard(Card card) {
        pile.add(card);
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean b) {
        status = b;
    }

    public int getPoints()
    {
        return points;
    }

    //Add points to the player's total and return the new number of points
    public int addPoints(int addPoints) {
        points += addPoints;
        return points;
    }

    //Loser gets kicked out of game.
    public void kickOut() {
        status = false;
    }

    //YOU WIN!!!
    public void celebrate() {}

    public Card play() {
       Card c = pile.get(0);
        return play(c);
    }

    public Card play(Card card) {
        pile.remove(card);
        return card;
    }

    public List<Card> getCards() {
        return pile;
    }
}

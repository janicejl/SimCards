package com.example.simcards;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract class that handles game logic.
 * Created by Lauren on 7/11/2014.
 */
public abstract class Game {
    private List<Player> players;
    private Deck deck;
    private Player activePlayer;
    private Iterator<Player> iterator;
    private boolean isActiveGame;

    public Game(List<Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;
        iterator = players.iterator();
    }

    //Assigns cards to users.
    abstract void deal();

    //Executes one turn in the game;
    public void turn() {
        executeTurn();
        if (hasWon()) {
            activePlayer.celebrate();
            isActiveGame = false;
        }
       else {
           if (!shouldPlayerContinue()) {
               activePlayer.kickOut();
           }

            activePlayer = nextPlayer();
        }
    }

    abstract void executeTurn();

    //Determines if active player has won based off of game rules
    abstract boolean hasWon();

    //Called at the end of a turn. Uses game rules to determine if player's status should change
    //NOTE: this should be called after hasWon(). This should assume that the player hasn't won the
    // game.
    abstract boolean shouldPlayerContinue();

    public Player getActivePlayer() {
        return activePlayer;
    }

    //Returns if the active player is still in the game.
    public boolean getActivePlayerStatus() {
        return activePlayer.getStatus();
    }

    /*
     * Changes active player. Calls to this are handled within the child's nextTurn method.
     * This method assumes that the number of players >  1. i.e. that this method should have been
     * called.
     */
    public Player nextPlayer() {
        if (!iterator.hasNext()) {
            iterator = players.iterator();
        }

        Player nextActivePlayer = iterator.next();
        if (nextActivePlayer.getStatus()) {
            return nextActivePlayer;
        }

        return nextPlayer();
    }
}

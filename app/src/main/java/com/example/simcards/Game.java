package com.example.simcards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract class that handles game logic.
 * Created by Lauren on 7/11/2014.
 */
public abstract class Game {
    public final static int DEAL_ALL_CARDS = -1;
    protected List<Player> players;
    protected Deck deck;
    protected Player activePlayer;
    protected Iterator<Player> iterator;
    protected boolean isActiveGame;
    private int dealNumber;

    public Game(List<Player> players, Deck deck, int dealNumber) {
        this.players = players;
        this.deck = deck;
        iterator = players.iterator();
        activePlayer = iterator.next();
        this.dealNumber = dealNumber;
    }

    public void addPlayerToGame(Player p){
        if (players == null) {
            players = new ArrayList<Player>();
        }
        players.add(p);
    }
    //Assigns cards to users.
    public void deal() {
        deck.shuffle();
        if(dealNumber == DEAL_ALL_CARDS) {
            dealAll();
        }
        else {
            realDeal();
        }
    }

    private void dealAll() {
        while (!deck.dealComplete()) {
            Card c = deck.dealCard();
            activePlayer.addCard(c);
            activePlayer = nextPlayer();
        }
    }

    public Player getCurrentPlayer() {
        return activePlayer;
    }

    public abstract boolean makeMove(Card card);

    public abstract boolean shouldWeEndTheGame();

    public abstract int compareCards(Card one, Card two);

    public abstract void setNextPlayer();

    private void realDeal() {
        int total = players.size() * dealNumber;
        if (dealNumber >= 52) {
            dealAll();
        }
        else {
            for (int i = 0; i < total; i++) {
                Card c = deck.dealCard();
                activePlayer.addCard(c);
                activePlayer = nextPlayer();
            }
        }
    }

    public abstract void executeTurn();

    public Player getActivePlayer() {
        return activePlayer;
    }

    public abstract Player getWinner();

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
        return nextActivePlayer;
    }

    public void reset() {
        deck.shuffle();
        for (Player p : players) {
            p.reset();
        }
    };
}

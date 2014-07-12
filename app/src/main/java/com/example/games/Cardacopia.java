package com.example.games;

import com.example.simcards.Card;
import com.example.simcards.Deck;
import com.example.simcards.Game;
import com.example.simcards.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lauren on 7/12/2014.
 */
public class Cardacopia extends Game {
    /*
    Spades > Hearts > Clubs > Diamonds > Spades
     */
    ArrayList<Card> middlePile = new ArrayList<Card>();

    private Card mTopCard;

    public Cardacopia(List<Player> players, Deck deck, int dealNumber) {
        super(players, deck, dealNumber);
    }

    @Override
    public void executeTurn() {
        if (!hasValidMove()) {
            //TODO: send notiication to UI to display no more moves available
            activePlayer.kickOut();
        }

    }

    @Override
    public boolean hasWon() {
        return false;
    }

    public boolean hasValidMove() {
        for (Card c : activePlayer.getCards()) {
            if (isValidMove(c)) {
                return true;
            }
        }

        return false;
    }

    public boolean isValidMove(Card c) {
        return compareCards(c, mTopCard) > 0;
    }

    public boolean makeMove(Card c) {
        int comparison = compareCards(c, mTopCard);
        if (comparison == 0) {
            return false;
        }
        else {
            mTopCard = c;
            activePlayer.play(c);
            activePlayer.addPoints(comparison);
            return true;
        }
    }

    public void setNextPlayer() {
        activePlayer = nextPlayer();
    }

    @Override
    //return if player has a valid move
    public boolean shouldPlayerContinue() {
        return false;
    }

    public int compareCards(Card a, Card b) {

        if(a.getValue() > b.getValue()) {
            return a.getValue() - b.getValue();
        }
        else {
            String suitA = a.getSuit();
            String suitB = b.getSuit();
            if ((suitA.equals("Spades") && suitB.equals("Diamonds")) ||
                    (suitA.equals("Diamonds") && suitB.equals("Clubs")) ||
                    (suitA.equals("Clubs") && suitB.equals("Hearts")) ||
                    (suitA.equals("Hearts") && suitB.equals("Spades"))){
                return 1;
            }
        }

        return 0;
    }
}

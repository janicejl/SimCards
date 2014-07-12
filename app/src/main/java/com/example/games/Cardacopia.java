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
    //only called after game is over
    public Player getWinner(){
        Player mostAwesomePlayer = players.get(0);
        int topScore = mostAwesomePlayer.getPoints();

        for (Player p : players) {
           if (p.getPoints() > topScore){
               mostAwesomePlayer = p;
               topScore = p.getPoints();
           }
        }

        return mostAwesomePlayer;
    }

    public Card getTopCard() {
        return mTopCard;
    }

    public int[] getScoreArray() {
        int[] scoreArr = new int[3];
        Player nextPlayer = nextPlayer();
        int count = 0;
        while (nextPlayer != activePlayer){
            scoreArr[count] = nextPlayer.getPoints();
            nextPlayer = nextPlayer();
            count++;
        }

        return scoreArr;
    }

    public int[] getCardNumberArray() {
        int[] countArr = new int[3];
        Player nextPlayer = nextPlayer();
        int count = 0;
        while (nextPlayer != activePlayer){
            countArr[count] = nextPlayer.getCards().size();
            nextPlayer = nextPlayer();
            count++;
        }

        return countArr;
    }

    public boolean hasValidMove() {
        for (Card c : activePlayer.getCards()) {
            if (!activePlayer.getStatus()) {
                return false;
            }
            if (isValidMove(c)) {
                return true;
            }
        }
        activePlayer.setStatus(false);
        return false;
    }

    public boolean isValidMove(Card c) {
        return compareCards(c, mTopCard) > 0;
    }

    @Override
    public boolean makeMove(Card c) {
        if (mTopCard == null) {
            mTopCard = c;
            activePlayer.play(c);
            return true;
        }

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

    @Override
    public boolean shouldWeEndTheGame(){
        int playersPlaying = 0;
        for (Player p : players) {
            if (p.getStatus()) {
                playersPlaying++;
            }
        }
        return playersPlaying > 1;
    }

    @Override
    public void setNextPlayer() {
        activePlayer = nextPlayer();
    }

    @Override
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

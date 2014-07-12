package com.example.games;

import com.example.simcards.Card;
import com.example.simcards.Deck;
import com.example.simcards.Game;
import com.example.simcards.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Antonio on 7/12/2014.
 */
public class HungerUno extends Game {

    private int mCurrentPlayerIndex;

    public HungerUno(List<Player> players, Deck deck, int dealNumber) {
        super(players, deck, dealNumber);
        mCurrentPlayerIndex = 0;
    }

    @Override
    public boolean makeMove(Card card) {
        activePlayer.play(card);
        int move = 0;
        if (card.getSuitVal() == 3) { // pick random number from 0 to 2
            Random random = new Random();
            move = random.nextInt(3);
        } else {
            move = card.getSuitVal() + 1;
        }
        mCurrentPlayerIndex += (card.getValue() % 2 == 0 ? move : -1 * move);
        mCurrentPlayerIndex += 4;
        mCurrentPlayerIndex = mCurrentPlayerIndex % 4;
        activePlayer = players.get(mCurrentPlayerIndex);
        return true;
    }

    @Override
    public boolean shouldWeEndTheGame() {
        for (Player p : players) {
            if (p.getCards().size() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareCards(Card one, Card two) {
        return 0;
    }

    @Override
    public void setNextPlayer() {

    }

    @Override
    public void winRound(Player winner, ArrayList<Card> cards) {

    }

    @Override
    public void executeTurn() {

    }

    @Override
    public Player getWinner() {
        for (Player p : players) {
            if (p.getCards().size() == 0) {
                return p;
            }
        }
        return null;
    }

    public int[] getCardNumberArray() {
        int[] c = new int[3];
        for (int i = 0 ; i < 3 ; i++) {
            c[i] = players.get(mCurrentPlayerIndex + i % 4).getCards().size();
        }
        return  c;
    }
}

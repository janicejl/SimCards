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
public class SimpleWarGame extends Game{

    public SimpleWarGame(List<Player> players, Deck deck) {
        super(players, deck, Game.DEAL_ALL_CARDS);
    }

    @Override
    public boolean makeMove(Card card) {
        if (activePlayer.getStatus()) {
            activePlayer.play(card);
            if (activePlayer.getCards().size() == 0) {
                activePlayer.kickOut();
            }
            return true;
        }

        return false;
    }

    @Override
    public void winRound(Player roundWinner, ArrayList<Card> cardList) {
        for (Card c : cardList) {
            roundWinner.addCard(c);
        }
    }

    @Override
    public boolean shouldWeEndTheGame() {
        int count = 0;
        for (Player p : players) {
           if (p.getCards().size() > 0) {
               count++;
           }
        }

        return count <= 1;
    }

    @Override
    public int compareCards(Card one, Card two) {
        String firstSuit = one.getSuit();
        String secondSuit = two.getSuit();
        if (firstSuit.equals(secondSuit))
        {
            return one.getValue() - two.getValue();
        }
        else {
            if (firstSuit.equals("Hearts") ||
                    (firstSuit.equals("Spades") && !secondSuit.equals("Hearts")) ||
                    secondSuit.equals("Clubs")) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }

    public Card compareCards(Card one, Card two, Card three, Card four) {
        Card victorCard;
        if (compareCards(one, two) > 0) {
            victorCard = one;
        }
        else {
            victorCard = two;
        }

        Card masterCard;
        if (compareCards(three, four) > 0) {
            masterCard = three;
        }
        else {
            masterCard = four;
        }

       Card bestCardEver;
        if (compareCards(victorCard, masterCard) > 0)
        {
            bestCardEver = victorCard;
        }
        else {
            bestCardEver = masterCard;
        }

        return bestCardEver;
    }

    @Override
    public void setNextPlayer() {
        activePlayer = nextPlayer();
    }

    @Override
    public void executeTurn() {

    }

    @Override
    public Player getWinner() {
        Player bossPlayer = players.get(0);
        int bossPlayersCardCount = bossPlayer.getCards().size();

        for (Player p : players) {
            if (p.getCards().size() > bossPlayersCardCount) {
                bossPlayer = p;
                bossPlayersCardCount = p.getCards().size();
            }
        }

        return bossPlayer;
    }
}

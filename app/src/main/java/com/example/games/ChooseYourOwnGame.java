package com.example.games;

import com.example.simcards.Card;
import com.example.simcards.Deck;
import com.example.simcards.Game;
import com.example.simcards.Player;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Evy on 12/07/2014.
 */
public class ChooseYourOwnGame extends Game {
    public ChooseYourOwnGame(List<Player> players, Deck deck) {
        super(players, deck, 0);
    }

    public void PickUpOne() {
        Card c = deck.dealCard();
        activePlayer.addCard(c);
        if(deck.size() == 0)
            deck.recycleDiscardToDeck();
    }

    public void PlayCard (Card c) {
        deck.addToDiscard(activePlayer.play(c));
    }

    public Card getFaceup() {
        return deck.getFaceup();
    }

    @Override
    public void setNextPlayer(Player player) {
        activePlayer = player;
    }

    public void giveCard(Player player, Card c) {
        player.addCard(activePlayer.play(c));
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

    @Override
    public boolean shouldWeEndTheGame() { return false; }
    @Override
    public int compareCards(Card one, Card two) { return 0; }
    @Override
    public boolean makeMove(Card card) { return false; }
    @Override
    public void executeTurn() {}
    @Override
    public void winRound(Player winner, ArrayList<Card> cards){}
    @Override
    public Player getWinner(){return activePlayer;}
}

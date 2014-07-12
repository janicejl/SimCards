package com.example.simcards;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	ArrayList<Card> deck;
	int top;
    ArrayList<Card> discard;
    private Card mFaceup;

	public void shuffle() {
		for (int i = 0; i < deck.size(); i++) {
			Card temp = deck.get(i);
			Random random = new Random();
			int ran = random.nextInt(52);
			deck.set(i, deck.get(ran));
			deck.set(ran, temp);
		}
        top = 0;
	}

    public int size() { return deck.size(); }

    public void recycleDiscardToDeck() {
        if (deck.size() > 0) {
            discard.addAll(deck);
        }
        discard.clear();
        shuffle();
        mFaceup = null;
    }
	
	Deck() {
        deck = new ArrayList<Card>();
        discard = new ArrayList<Card>();
		String ranks[] = {
				"King", "Queen", "Jack", "Ten", "Nine", "Eight",
				"Seven", "Six", "Five", "Four", "Three", "Two", "Ace"
		};
		String suits[] = {
				"Clubs","Spades" ,"Hearts", "Diamonds"
		};
		int values[] = {
				13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
		};
		top = 0;
		for (int i = 0; i < 52; i++) {
			deck.add(new Card(ranks[i%13], suits[i%4], values[i%13], i%4));
		}
	}
	
	public Card dealCard() {
		Card c = deck.get(top);
		top++;
		return c;
	}

    public void addToDiscard(Card c) {
        discard.add(c);
        mFaceup = c;
    }

    public Card getFaceup() { return mFaceup; }

    public boolean dealComplete() {
        return top >= 52;
    }
}

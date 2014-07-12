package com.example.simcards;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
	ArrayList<Card> deck;
	int top;

	public void shuffle() {
		for (int i = 0; i < 52; i++) {
			Card temp = deck.get(i);
			Random random = new Random();
			int ran = random.nextInt(52);
			deck.set(i, deck.get(ran));
			deck.set(ran, temp);
		}
	}
	
	Deck() {
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
			deck.set(i, new Card(ranks[i%13], suits[i%4], values[i%13], i%4));
		}
	}
	
	public Card dealCard() {
		Card c = deck.get(top);
		top++;
		return c;
	}

    public boolean dealComplete() {
        return top >= 52;
    }
}

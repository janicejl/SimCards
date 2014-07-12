package com.example.simcards;

public class Card {
	String rank;
	String suit;
	int value;
	
	String getRank() {
		return rank;
	}
	
	String getSuit() {
		return rank;
	}
	
	int getValue() {
		return value;
	}
	
	void setValue(int aValue) {
		value = aValue;
	}
	
	Card() {}
	Card(String rk, String st, int val) {
		rank = rk;
		suit = st;
		value = val;
	}
	
	void print() {
		System.out.println(rank + " of " + suit);
	}
}

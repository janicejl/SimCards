package com.example.simcards;

import android.graphics.Rect;

public class Card {
	final int card_width = 73;
	final int card_height = 98;
	
	String rank;
	String suit;
	int value;
	int suits_picture_position;
	
	int x_pos;
	int y_pos;
	/*
	String getRank() {
		return rank;
	}
	*/
	
	String getSuit() {
		return rank;
	}
	int getX() {
		return x_pos;
	}
	
	int getY() {
		return y_pos;
	}
	
	Rect getBox() {
		return new Rect(x_pos, y_pos, x_pos + card_width, y_pos + card_height);
	}
	
	void setValue(int aValue) {
		value = aValue;
	}
	
	Card() {}
	Card(String rk, String st, int value, int suitVal) {
		rank = rk;
		suit = st;
		suits_picture_position = suitVal;
		
		x_pos = card_width * (value - 1);
		y_pos = card_height * suits_picture_position;
	}
	
	void print() {
		System.out.println(rank + " of " + suit);
	}
}

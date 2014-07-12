package com.example.simcards;

import android.graphics.Point;
import android.graphics.Rect;

public class Card {
	public static int card_width;
	public static int card_height;
    public static final Rect CARD_RECT = new Rect(0, 0, card_width, card_height);

	String rank;
	String suit;
	int value;
	int suits_picture_position;

	int x_pos;
	int y_pos;

    private Rect mCurrentPositionRect;

	String getRank() {
		return rank;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public int getValue() {
		return value;
	}
	
	int getX() {
		return x_pos;
	}
	
	int getY() {
		return y_pos;
	}

    /**
     * This gets the rectangle for the actual image in the bitmap
     */
	Rect getBox() {
		return new Rect(x_pos, y_pos, x_pos + card_width, y_pos + card_height);
	}
	
	void setValue(int aValue) {
		value = aValue;
	}
	
	Card(String rk, String st, int val, int suitVal) {
		this(rk, st, val, suitVal, 0, 0);
	}

    /**
     * Constructor containing the global positions for the cards
     */
    public Card(String rk, String st, int val, int suitVal, int positionX, int positionY) {
        rank = rk;
        suit = st;
        value = val;
        suits_picture_position = suitVal;

        x_pos = card_width * (value - 1);
        y_pos = card_height * suits_picture_position;

        mCurrentPositionRect = new Rect(positionX, positionY,
                positionX + card_width, positionY + card_height);
    }

	void print() {
		System.out.println(rank + " of " + suit);
	}

    public Rect getPositionRect() {
        return  mCurrentPositionRect;
    }

    public void setPositionRect(Rect r) {
        mCurrentPositionRect = r;
    }

    /**
     * Used for touch detection, will return true if the touch occurred on this card
     */
    public boolean containsPoint(Point p) {
        return mCurrentPositionRect.contains(p.x, p.y);
    }

    public String toJSON() {
        return "{\"card\":\""
                + "\"SUITS\":\"" + suit + "\","
                + "\"RANK\":\"" + rank + "\"}";
    }
}

package com.example.simcards;

import android.graphics.Point;
import android.graphics.Rect;

public class Card {
	public static final int card_width = 74;
	public static final int card_height = 118;
    public static final Rect CARD_RECT = new Rect(0, 0, card_width, card_height);

	String rank;
	String suit;
	int value;
	int suits_picture_position;
	
	int x_pos;
	int y_pos;

    private int mGlobalPositionX;
    private int mGlobalPositionY;

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

        mGlobalPositionX = positionX;
        mGlobalPositionY = positionY;
    }
	
	void print() {
		System.out.println(rank + " of " + suit);
	}

    public int getGlobalPositionX() {
        return mGlobalPositionX;
    }

    public int getGlobalPositionY() {
        return mGlobalPositionY;
    }

    public void setGlobalPositionX(int x) {
        mGlobalPositionX = x;
    }

    public void setGlobalPositionY(int y) {
        mGlobalPositionY = y;
    }

    /**
     * Used for touch detection, will return true if the touch occurred on this card
     */
    public boolean containsPoint(Point p) {
        Rect temp = new Rect(mGlobalPositionX, mGlobalPositionY, mGlobalPositionX + card_width,
                mGlobalPositionY+ card_height);
        return temp.contains(p.x, p.y);
    }
}

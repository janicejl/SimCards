package com.example.simcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class GameView extends View {
	Bitmap card_front = BitmapFactory.decodeResource(getResources(), R.drawable.cards);;
	Paint paint = new Paint();
	
	Card c = new Card("Spade", "Ten", 10, 1);
	Rect background;

	public GameView(Context context) {
		super(context);
	}

	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
		drawCards(canvas);
	}

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.GREEN);
    }

    private void drawCards(Canvas canvas) {
        canvas.drawBitmap(card_front, c.getBox(), c.getBox(), paint);
    }
}

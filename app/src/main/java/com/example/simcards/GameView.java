package com.example.simcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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
		// TODO Auto-generated constructor stub
		background = new Rect(0,0,
				context.getResources().getDisplayMetrics().widthPixels,
				context.getResources().getDisplayMetrics().heightPixels);
	}

	public void onDraw(Canvas canvas) {
		canvas.drawBitmap(card_front, c.getBox(), background, paint);
	}
}

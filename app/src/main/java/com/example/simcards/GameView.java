package com.example.simcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {
    private final static int BACKGROUND_COLOR = Color.GREEN;
    private Bitmap mCardBitmap;
    private Card mTestCard;

	public GameView(Context context) {
		super(context);
        loadAssets();
        mTestCard = new Card("Spade", "Ten", 10, 1);
        this.setOnTouchListener(new MyTouchListener());
	}

    private void loadAssets() {
        mCardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cards);
    }

	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		drawBackground(canvas);
        drawCards(canvas);
	}

    private void drawCards(Canvas canvas) {
        Rect cardRect = mTestCard.getBox();
        canvas.drawBitmap(mCardBitmap, cardRect, Card.CARD_RECT, null);
    }

    private void drawDeck(Canvas canvas) {

    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);
    }

    private class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Point p;
            p = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
            //if (mTestCard.containsPoint(p)) {
                Toast toast = Toast.makeText(getContext(), "Touched Card + " + p.x + " " + p.y,
                        Toast.LENGTH_SHORT);
                toast.show();
            //}
            return true;
        }
    }
}

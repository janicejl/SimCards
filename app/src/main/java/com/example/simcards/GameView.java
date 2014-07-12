package com.example.simcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class GameView extends View {
    // States of a basic game. Player chooses a card and goes to the decision state. From there they
    // either choose another card, staying in the same state, or they throw to the pile moving them
    // to the wait state. From there we wait until the next player is ready to display stuff
    private final static int CHOOSE_PLAYER_CARD = 1;
    private final static int PLAYER_MAKE_DECISION = 2;
    private final static int WAIT_FOR_NEXT_PLAYER = 3;

    private final static int BACKGROUND_COLOR = Color.GREEN;
    private final static int SCREEN_BUFFER = 175;
    private final static int DECK_WIDTH = 120;
    private final static int DECK_HEIGHT = 150;
    private Rect mCenterRect;
    private Bitmap mCardBitmap;
    private Bitmap mCardBackBitmap;
    private Player mCurrentPlayer;
    private int mScreenWidth;
    private int mScreenHeight;

	public GameView(Context context) {
		super(context);
        loadAssets();
        this.setOnTouchListener(new MyTouchListener());
        mScreenWidth = MainActivity.SCREEN_WIDTH;
        mScreenHeight = MainActivity.SCREEN_HEIGHT;
        mCenterRect = new Rect((mScreenWidth / 2)- (DECK_WIDTH / 2),
                (mScreenHeight / 2) - (DECK_HEIGHT / 2),
                (mScreenWidth / 2) + (DECK_WIDTH / 2),
                (mScreenHeight / 2) + (DECK_HEIGHT / 2));
        mCurrentPlayer = new Player();
        for (int i = 0 ; i < 1 ; i++) {
            mCurrentPlayer.addCard(new Card("10", "spade", 10, 1));
        }
	}

    private void loadAssets() {
        mCardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cards);
        mCardBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card_back);
        Card.card_height = mCardBitmap.getHeight() / 4;
        Card.card_width = mCardBitmap.getWidth() / 13;
    }

	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		drawBackground(canvas);
        drawCards(canvas);
        drawDeck(canvas);
	}

    private void drawCards(Canvas canvas) {
        float scaling = getScalingFactor();
        int cardWidth = (int) (scaling * Card.card_width);
        int yPos = mScreenHeight - Card.card_height - SCREEN_BUFFER;
        List<Card> cardList = mCurrentPlayer.getCards();
        for (int i = 0 ; i < cardList.size() ; i++) {
            Rect cardRect = cardList.get(i).getBox();
            Rect dstRect = new Rect(cardWidth * i, yPos,
                    cardWidth * (i + 1), yPos + Card.card_height);
            cardList.get(i).setPositionRect(dstRect);
            canvas.drawBitmap(mCardBitmap, cardRect, dstRect, null);
        }
    }

    private float getScalingFactor() {
        List<Card> cardList = mCurrentPlayer.getCards();
        int numberOfCards = cardList.size();
        float scalingFactor = 1.0f;
        if (numberOfCards * Card.card_width > mScreenWidth) {
            scalingFactor = (mScreenWidth - SCREEN_BUFFER) * 1.0f /
                    (numberOfCards * Card.card_width);
        }
        return scalingFactor;
    }

    private void drawDeck(Canvas canvas) {
        canvas.drawBitmap(mCardBackBitmap, null, mCenterRect, null);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);
    }

    private class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Point p;
            p = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
            if (mCenterRect.contains(p.x, p.y)) {
                Toast toast = Toast.makeText(getContext(), "CENTER", Toast.LENGTH_SHORT);
                toast.show();
            }
            List<Card> playerCards = mCurrentPlayer.getCards();
            for (int i = 0 ; i < playerCards.size() ; i++) {
                if (playerCards.get(i).containsPoint(p)) {
                    Toast toast = Toast.makeText(getContext(), "Card : " + i, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            return true;
        }
    }
}

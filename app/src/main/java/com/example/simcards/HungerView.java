package com.example.simcards;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.games.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Antonio on 7/12/2014.
 */
public class HungerView extends View {
    private final static int CHOOSE_PLAYER_CARD = 1;
    private final static int WAIT_FOR_NEXT_PLAYER = 2;

    private final static int BACKGROUND_COLOR = Color.parseColor("#002400");
    private final static int TABLE_BORDER = Color.argb(255, 200, 200, 200);
    private final static float TABLE_STROKE_WIDTH = 20f;
    private final static int DECK_WIDTH = 180;
    private final static int DECK_HEIGHT = 240;
    private final static int POPUP_HEIGHT = 150;
    private final static int POPUP_WIDTH = 250;

    private int mCurrentState;
    private Bitmap mCardBitmap;
    private Bitmap mCardBackBitmap;
    private Bitmap mCardBackRotatedBitmap;
    private Bitmap mCardBackCounterRotatedBitmap;
    private Bitmap mCardBack180RotateBitmap;
    private HungerUno mHungerUno;
    private Card mTopCard;
    private int[] mCardCounts;
    private int mScreenWidth;
    private int mScreenHeight;
    private Rect mCenterRect;
    private Card mDragCard;
    private PopupWindow mPopupWindow;

    public HungerView(Context context) {
        super(context);
        loadAssets();
        setGameVariables();
        setViewVariables();
    }

    private void setGameVariables() {
        mCurrentState = CHOOSE_PLAYER_CARD;

        List<Player> players = new ArrayList<Player>();
        for (int i = 0 ; i < 4 ; i++) {
            players.add(new Player());
        }
        mHungerUno = new HungerUno(players, new Deck(), Game.DEAL_ALL_CARDS);
        mHungerUno.deal();

        mTopCard = null;

        mCardCounts = mHungerUno.getCardNumberArray();
    }

    private void setViewVariables() {
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        setSystemUiVisibility(uiOptions);

        MyTouchListener listener = new MyTouchListener();
        this.setOnTouchListener(listener);

        mScreenWidth = CardacopiaInterface.SCREEN_WIDTH;
        mScreenHeight = CardacopiaInterface.SCREEN_HEIGHT;
        mCenterRect = new Rect((mScreenWidth / 2)- (DECK_WIDTH / 2),
                (mScreenHeight / 2) - (DECK_HEIGHT / 2),
                (mScreenWidth / 2) + (DECK_WIDTH / 2),
                (mScreenHeight / 2) + (DECK_HEIGHT / 2));

        mDragCard = null;
    }

    private void loadAssets() {
        mCardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cards);
        mCardBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card_back);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        mCardBackRotatedBitmap = Bitmap.createBitmap(mCardBackBitmap, 0, 0,
                mCardBackBitmap.getWidth(), mCardBackBitmap.getHeight(), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(-90);
        mCardBackCounterRotatedBitmap = Bitmap.createBitmap(mCardBackBitmap, 0, 0,
                mCardBackBitmap.getWidth(), mCardBackBitmap.getHeight(), matrix, true);
        matrix = new Matrix();
        matrix.postRotate(180);
        mCardBack180RotateBitmap = Bitmap.createBitmap(mCardBackBitmap, 0, 0,
                mCardBackBitmap.getWidth(), mCardBackBitmap.getHeight(), matrix, true);
        Card.card_height = mCardBitmap.getHeight() / 4;
        Card.card_width = mCardBitmap.getWidth() / 13;
    }

    private PopupWindow createPopupWindow(String message, final boolean switchPlayer) {
        Button popupButton = new Button(getContext());
        popupButton.setText(message);
        popupButton.setHeight(POPUP_HEIGHT);
        popupButton.setWidth(POPUP_WIDTH);
        popupButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCurrentState = CHOOSE_PLAYER_CARD;
                mPopupWindow.dismiss();
                invalidate();
                return true;
            }
        });
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.addView(popupButton);
//        RoundRectShape roundRectShape = new RoundRectShape(POPUP_OUTER_RECT, null, null);
//        Drawable background = new ShapeDrawable(roundRectShape);
//        linearLayout.setBackground(background);
        PopupWindow window = new PopupWindow(linearLayout, POPUP_WIDTH, POPUP_HEIGHT);
        window.setContentView(linearLayout);
        return window;
    }

    public void onDraw(Canvas canvas) {
        drawBackground(canvas);
        /*drawDeck(canvas);
        drawOpponentCards(canvas);
        drawScores(canvas);
        drawNameAndIcons(canvas);
        drawCards(canvas);*/
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(TABLE_BORDER);
        paint.setStrokeWidth(TABLE_STROKE_WIDTH);
        canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);
    }

    private class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Point p;
            p = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
            int x;
            int y;
            switch (mCurrentState) {
                case CHOOSE_PLAYER_CARD:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            x = (int) motionEvent.getX();
                            y = (int) motionEvent.getY();
                            int index = findCardIndex(new Point(x, y));
                            if (index >= 0) {
                                mDragCard = mHungerUno.getActivePlayer().getCards().get(index);
                                mDragCard.setPositionRect(new Rect(x, y, x + Card.card_width,
                                        y + Card.card_height));
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:
                            x = (int) motionEvent.getX() - (Card.card_width / 2);
                            y = (int) motionEvent.getY() - (Card.card_height / 2);
                            if (mDragCard != null) {
                                mDragCard.setPositionRect(new Rect(x, y, x + Card.card_width,
                                        y + Card.card_height));
                            }
                            invalidate();
                            break;

                        case MotionEvent.ACTION_UP:
                            if (mDragCard != null &&
                                    mDragCard.getPositionRect().intersect(mCenterRect) &&
                                    mHungerUno.makeMove(mDragCard)) {
                                postInvalidate();
                                mCurrentState = WAIT_FOR_NEXT_PLAYER;
                                mTopCard = mDragCard;
                                mPopupWindow =
                                        createPopupWindow(mHungerUno.getActivePlayer().getName() + " is up", true);
                                mPopupWindow.showAsDropDown(view, mScreenWidth / 2 - (POPUP_WIDTH / 2),
                                        -1 * mScreenHeight / 2 - (POPUP_HEIGHT / 2));
                            }
                            mDragCard = null;
                            invalidate();
                            break;
                    }
                    break;

                case WAIT_FOR_NEXT_PLAYER:
                    invalidate();
                    break;
            }
            return true;
        }

        private int findCardIndex(Point p) {
            List<Card> cardList = mHungerUno.getActivePlayer().getCards();
            for (int i = cardList.size() - 1 ; i >= 0 ; i--) {
                if (cardList.get(i).containsPoint(p)) {
                    return i;
                }
            }
            return -1;
        }
    }
}

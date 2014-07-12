package com.example.simcards;

import android.app.Activity;
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
    private final static int END_STATE = -1;

    private final static int BACKGROUND_COLOR = Color.parseColor("#002400");
    private final static int TABLE_BORDER = Color.argb(255, 200, 200, 200);
    private final static float TABLE_STROKE_WIDTH = 20f;
    private final static int DECK_WIDTH = 180;
    private final static int DECK_HEIGHT = 240;
    private final static int POPUP_HEIGHT = 150;
    private final static int POPUP_WIDTH = 250;
    private final static int PLAYER_BUFFERS = 250;
    private final static int SCREEN_BUFFER = 0;
    private final static int DRAG_ALPHA = 128;
    private final static float NAME_SIZE = 40.0F;
    private final static int NAME_COLOR = Color.WHITE;
    private final static int TEXT_BUFFER = 15;
    private final static int POPUP_BUTTON_BUFFER = 10;
    private final static long WIN_DELAY = 2000;

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

        mScreenWidth = HungerActivity.SCREEN_WIDTH;
        mScreenHeight = HungerActivity.SCREEN_HEIGHT;
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
        drawDeck(canvas);
        drawOpponentCards(canvas);
        drawNameAndIcons(canvas);
        drawCards(canvas);
    }

    private void drawNameAndIcons(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(NAME_COLOR);
        p.setTextSize(NAME_SIZE);
        canvas.drawText("Current player : " + mHungerUno.getActivePlayer().getName(),
                mScreenWidth / 2, mScreenHeight - Card.card_height - TEXT_BUFFER, p);
    }

    private void drawCards(Canvas canvas) {
        switch (mCurrentState) {
            case CHOOSE_PLAYER_CARD:
                // Draw the dragging card first
                if (mDragCard != null) {
                    Paint paint = new Paint();
                    paint.setAlpha(DRAG_ALPHA);
                    canvas.drawBitmap(mCardBitmap, mDragCard.getBox(),
                            mDragCard.getPositionRect(), paint);
                }


                int yPos = mScreenHeight - Card.card_height - SCREEN_BUFFER;
                List<Card> cardList = mHungerUno.getActivePlayer().getCards();
                int cardSpacing = Math.min(Card.card_width,
                        (mScreenWidth - (3 * SCREEN_BUFFER)) / (cardList.size() + 1));
                for (int i = 0 ; i < cardList.size() ; i++) {
                    if (mDragCard != null && cardList.get(i) == mDragCard) {
                        continue;
                    }
                    Rect cardRect = cardList.get(i).getBox();
                    Rect dstRect = new Rect(cardSpacing * i + SCREEN_BUFFER, yPos,
                            cardSpacing * i + Card.card_width + SCREEN_BUFFER,
                            yPos + Card.card_height);
                    cardList.get(i).setPositionRect(dstRect);
                    canvas.drawBitmap(mCardBitmap, cardRect, dstRect, null);
                }
                break;

            case WAIT_FOR_NEXT_PLAYER:
                int pos = mScreenHeight - Card.card_height - SCREEN_BUFFER;
                List<Card> list = mHungerUno.getActivePlayer().getCards();
                int spacing = Math.min(Card.card_width,
                        (mScreenWidth - (3 * SCREEN_BUFFER)) / (list.size() + 1));
                for (int i = 0 ; i < list.size() ; i++) {
                    Rect dstRect = new Rect(spacing * i + SCREEN_BUFFER, pos,
                            spacing * i + Card.card_width + SCREEN_BUFFER, pos + Card.card_height);
                    list.get(i).setPositionRect(dstRect);
                    canvas.drawBitmap(mCardBackBitmap, null, dstRect, null);
                }
                break;
        }
    }

    private void drawOpponentCards(Canvas canvas) {
        int mLeftPlayerCardCount = mCardCounts[0];
        int mTopPlayerCardCount = mCardCounts[1];
        int mRightPlayerCardCount = mCardCounts[2];
        // Left Player's hand
        if (mLeftPlayerCardCount != 0) {
            int leftSpacing = Math.min(Card.card_width,
                    (mScreenHeight - Card.card_width - (2 * PLAYER_BUFFERS)) /
                            (mLeftPlayerCardCount));
            int xPos = SCREEN_BUFFER;
            for (int i = 0 ; i < mLeftPlayerCardCount ; i++) {
                Rect dstRect = new Rect(xPos, i * leftSpacing + PLAYER_BUFFERS, xPos + Card.card_height,
                        i * leftSpacing + PLAYER_BUFFERS + Card.card_width);
                canvas.drawBitmap(mCardBackRotatedBitmap, null, dstRect, null);
            }
        }

        // Top Player's hand
        if (mTopPlayerCardCount != 0) {
            int topSpacing = Math.min(Card.card_width,
                    (mScreenWidth - (2 * PLAYER_BUFFERS)) / (mTopPlayerCardCount + 1));
            for (int i = 0 ; i <mTopPlayerCardCount ; i++) {
                Rect dstRect = new Rect(i * topSpacing + PLAYER_BUFFERS, SCREEN_BUFFER,
                        i * topSpacing + PLAYER_BUFFERS + Card.card_width,
                        SCREEN_BUFFER + Card.card_height);
                canvas.drawBitmap(mCardBack180RotateBitmap, null, dstRect, null);
            }
        }

        // Right Player's hand
        if (mRightPlayerCardCount != 0) {
            int rightSpacing = Math.min(Card.card_width,
                    (mScreenHeight - Card.card_width - (2 * PLAYER_BUFFERS)) / mRightPlayerCardCount);
            int xPos = mScreenWidth - SCREEN_BUFFER - Card.card_height;
            for (int i = 0 ; i < mRightPlayerCardCount ; i++) {
                Rect dstRect = new Rect(xPos, i * rightSpacing + PLAYER_BUFFERS,
                        xPos + Card.card_height, i * rightSpacing + PLAYER_BUFFERS + Card.card_width);
                canvas.drawBitmap(mCardBackCounterRotatedBitmap, null, dstRect, null);
            }
        }
    }

    private void drawDeck(Canvas canvas) {
        if (mTopCard == null) {
            canvas.drawBitmap(mCardBackBitmap, null, mCenterRect, null);
        } else {
            canvas.drawBitmap(mCardBitmap, mTopCard.getBox(), mCenterRect, null);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(TABLE_BORDER);
        paint.setStrokeWidth(TABLE_STROKE_WIDTH);
        canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);
    }

    private void showWin() {
        mCurrentState = END_STATE;
        Button popupButton = new Button(getContext());
        popupButton.setText("Winner is " + mHungerUno.getWinner().getName() + "!!!");
        popupButton.setHeight(POPUP_HEIGHT - POPUP_BUTTON_BUFFER);
        popupButton.setWidth(POPUP_WIDTH - POPUP_BUTTON_BUFFER);
        popupButton.setOnTouchListener(new OnTouchListener() {
            long startTime = System.currentTimeMillis();

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (System.currentTimeMillis() - startTime >= WIN_DELAY) {
                    ((Activity) getContext()).finish();
                }
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
        window.showAsDropDown(this, mScreenWidth / 2 - (POPUP_WIDTH / 2),
                -1 * mScreenHeight / 2 - (POPUP_HEIGHT / 2));
        mCurrentState = END_STATE;
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
                                if (mHungerUno.shouldWeEndTheGame()) {
                                    showWin();
                                }
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

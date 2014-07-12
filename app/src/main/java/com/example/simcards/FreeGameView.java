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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.games.Cardacopia;
import com.example.games.ChooseYourOwnGame;

import java.util.ArrayList;
import java.util.List;

public class FreeGameView extends View {
    private final static int CHOOSE_PLAYER_CARD = 1;
    private final static int WAIT_FOR_NEXT_TURN = 2;
    private final static int END_STATE = -1;
    private final static boolean DISPLAY_OTHER_SCORES = true;

    private final static int BACKGROUND_COLOR = Color.parseColor("#002400");
    private final static int SCORE_COLOR = Color.WHITE;
    private final static int SCORE_COLOR_OUTLINE = Color.BLACK;
    private final static float SCORE_SIZE = 80.0f;
    private final static float SCORE_SIZE_OUTLINE = 30.5f;
    private final static int PLAYER_BUFFERS = 250;
    private final static int SCREEN_BUFFER = 0;
    private final static int DECK_WIDTH = 180;
    private final static int DECK_HEIGHT = 240;
    private final static int POPUP_HEIGHT = 150;
    private final static int POPUP_WIDTH = 250;
    private final static int POPUP_BUTTON_BUFFER = 10;
    private final static int POPUP_BACKGROUND_COLOR = Color.argb(255, 128, 128, 128);
    private final static int WIN_POPUP_WIDTH = 400;
    private final static int WIN_POPUP_HEIGHT = 200;
    private final static int TEXT_BUFFER = 15;
    private final static float[] POPUP_OUTER_RECT = new float[] {5, 5, 5, 5, 5, 5, 5, 5};
    private final static int[] PLAYER_COLORS = new int[] {Color.BLUE, Color.GREEN, Color.RED,
            Color.BLACK};
    private final static long WIN_DELAY = 2000;
    private final static float NAME_SIZE = 40.0F;
    private final static int NAME_COLOR = Color.WHITE;
    private final static int DRAG_ALPHA = 128;

    private ChooseYourOwnGame mCurrentGame;
    private Rect mCenterRect;
    private Bitmap mCardBitmap;
    private Bitmap mCardBackBitmap;
    private Bitmap mCardBackRotatedBitmap;
    private Bitmap mCardBackCounterRotatedBitmap;
    private Bitmap mCardBack180RotateBitmap;
    private Player mCurrentPlayer;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLeftPlayerCardCount;
    private int mTopPlayerCardCount;
    private int mRightPlayerCardCount;
    //private int mLeftPlayerScore;
    //private int mRightPlayerScore;
    //private int mTopPlayerScore;
    //private int mCurrentPlayerScore;
    private int mCurrentState;
    private PopupWindow mPopupWindow;
    private Card mTopCard;
    private Card mDragCard;

	public FreeGameView(Context context, String[] names) {
		super(context);
        // Must load assets first to set card length and height
        loadAssets();

        setViewVariables();

        setGameVariables(names);
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

    private void setGameVariables(String[] names) {
        mCurrentState = CHOOSE_PLAYER_CARD;

        List<Player> players = new ArrayList<Player>();
        for (int i = 0 ; i < 4 ; i++) {
            players.add(new Player(names[i]));
        }
        mCurrentGame = new ChooseYourOwnGame(players, new Deck(), 0);

        mTopCard = null;

        mCurrentPlayer = mCurrentGame.getActivePlayer();

        int[] cardCounts = mCurrentGame.getCardNumberArray();
        //int[] scores = mCurrentGame.getScoreArray();

        mLeftPlayerCardCount = cardCounts[0];
        mTopPlayerCardCount = cardCounts[1];
        mRightPlayerCardCount = cardCounts[2];

        //mLeftPlayerScore = scores[0];
        //mRightPlayerScore = scores[2];
        //mTopPlayerScore = scores[1];
        //mCurrentPlayerScore = mCurrentPlayer.getPoints();
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
                if (switchPlayer) {
                    switchPlayer();
                }
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

    private void switchPlayer(Player player) {
        mCurrentPlayer = player;
        mCurrentGame.setNextPlayer(player);
        //int[] scores = mCurrentGame.getScoreArray();
        int[] cardCounts = mCurrentGame.getCardNumberArray();
        mTopCard = mCurrentGame.getFaceup();

        //mCurrentPlayerScore = mCurrentPlayer.getPoints();
        //mLeftPlayerScore = scores[0];
        //mTopPlayerScore = scores[1];
        //mRightPlayerScore = scores[2];
        mLeftPlayerCardCount = cardCounts[0];
        mTopPlayerCardCount = cardCounts[1];
        mRightPlayerCardCount = cardCounts[2];
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

	public void onDraw(Canvas canvas) {
		drawBackground(canvas);
        drawDeck(canvas);
        drawOpponentCards(canvas);
        //drawScores(canvas);
        drawNameAndIcons(canvas);
        drawCards(canvas);
	}

    private void drawNameAndIcons(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(NAME_COLOR);
        p.setTextSize(NAME_SIZE);
        canvas.drawText("Current player : " + mCurrentPlayer.getName(), mScreenWidth / 2 + SCORE_SIZE,
                mScreenHeight - Card.card_height - TEXT_BUFFER, p);
    }

   /*private void drawScores(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(SCORE_COLOR);
        p.setTextSize(SCORE_SIZE);

        canvas.drawText("" + mCurrentPlayerScore, mScreenWidth / 2 - (SCORE_SIZE),
                mScreenHeight - SCREEN_BUFFER - Card.card_height - TEXT_BUFFER, p);

        if (DISPLAY_OTHER_SCORES) {
            // left
            canvas.drawText("" + mLeftPlayerScore, Card.card_height + TEXT_BUFFER,
                    mScreenHeight / 2 - (SCORE_SIZE), p);

            // top
            canvas.drawText("" + mTopPlayerScore, mScreenWidth / 2 - (SCORE_SIZE),
                    Card.card_height + TEXT_BUFFER + SCORE_SIZE, p);

            // right
            String s = Integer.toString(mRightPlayerScore);
            canvas.drawText(s,
                    mScreenWidth - Card.card_height - TEXT_BUFFER - (SCORE_SIZE * (s.length())),
                    mScreenHeight / 2 - (SCORE_SIZE), p);
        }
    }*/

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
                List<Card> cardList = mCurrentPlayer.getCards();
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

            case WAIT_FOR_NEXT_TURN:
                int pos = mScreenHeight - Card.card_height - SCREEN_BUFFER;
                List<Card> list = mCurrentPlayer.getCards();
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
    }

    private class MyTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Point p;
            p = new Point((int) motionEvent.getX(), (int) motionEvent.getY());
            int x;
            int y;
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();
                    int index = findCardIndex(new Point(x, y));
                    if (index >= 0) {
                        mDragCard = mCurrentPlayer.getCards().get(index);
                        mDragCard.setPositionRect(new Rect(x, y, x + Card.card_width,
                                y + Card.card_height));
                        break;
                    }
                    index = 
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
                                mCurrentGame.makeMove(mDragCard)) {
                            postInvalidate();
                            mCurrentPlayer.play(mDragCard);
                            mTopCard = mDragCard;
                        }
                        mDragCard = null;
                        invalidate();
                        break;
                }

            return true;
        }

        private int findCardIndex(Point p) {
            List<Card> cardList = mCurrentPlayer.getCards();
            for (int i = cardList.size() - 1 ; i >= 0 ; i--) {
                if (cardList.get(i).containsPoint(p)) {
                  return i;
                }
            }
            return -1;
        }
    }
}

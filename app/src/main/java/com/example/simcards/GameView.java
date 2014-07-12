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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.List;

public class GameView extends View {
    private final static int CHOOSE_PLAYER_CARD = 1;
    private final static int WAIT_FOR_NEXT_PLAYER = 2;
    private final static boolean DISPLAY_OTHER_SCORES = true;

    private final static int BACKGROUND_COLOR = Color.GREEN;
    private final static int SCORE_COLOR = Color.WHITE;
    private final static int SCORE_COLOR_OUTLINE = Color.BLACK;
    private final static float SCORE_SIZE = 80.0f;
    private final static float SCORE_SIZE_OUTLINE = 30.5f;
    private final static int PLAYER_BUFFERS = 200;
    private final static int SCREEN_BUFFER = 0;
    private final static int DECK_WIDTH = 180;
    private final static int DECK_HEIGHT = 240;
    private final static int POPUP_HEIGHT = 150;
    private final static int POPUP_WIDTH = 250;
    private final static int POPUP_BUTTON_BUFFER = 10;
    private final static int POPUP_BACKGROUND_COLOR = Color.argb(255, 128, 128, 128);
    private final static int TEXT_BUFFER = 15;

    private Rect mCenterRect;
    private Bitmap mCardBitmap;
    private Bitmap mCardBackBitmap;
    private Bitmap mCardBackRotatedBitmap;
    private Player mCurrentPlayer;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mLeftPlayerCardCount;
    private int mTopPlayerCardCount;
    private int mRightPlayerCardCount;
    private int mLeftPlayerScore;
    private int mRightPlayerScore;
    private int mTopPlayerScore;
    private int mCurrentPlayerScore;
    private int mCurrentState;
    private PopupWindow mPopupWindow;

	public GameView(Context context) {
		super(context);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        setSystemUiVisibility(uiOptions);
        loadAssets();
        this.setOnTouchListener(new MyTouchListener());
        mScreenWidth = MainActivity.SCREEN_WIDTH;
        mScreenHeight = MainActivity.SCREEN_HEIGHT;
        mCenterRect = new Rect((mScreenWidth / 2)- (DECK_WIDTH / 2),
                (mScreenHeight / 2) - (DECK_HEIGHT / 2),
                (mScreenWidth / 2) + (DECK_WIDTH / 2),
                (mScreenHeight / 2) + (DECK_HEIGHT / 2));

        mCurrentState = CHOOSE_PLAYER_CARD;

        mPopupWindow = createPopupWindow();

        //TODO Take out the hardcoded stuff
        mCurrentPlayer = new Player();
        for (int i = 0 ; i < 20 ; i++) {
            mCurrentPlayer.addCard(new Card("10", "spade", (i + 1) % 13, i % 4));
        }

        mLeftPlayerCardCount = 10;
        mTopPlayerCardCount = 10;
        mRightPlayerCardCount = 10;

        mLeftPlayerScore = 112;
        mRightPlayerScore = 112;
        mTopPlayerScore = 124;
        mCurrentPlayerScore = 100;
	}

    private PopupWindow createPopupWindow() {
        Button popupButton = new Button(getContext());
        popupButton.setText("Next Player Please!!!");
        popupButton.setHeight(POPUP_HEIGHT - POPUP_BUTTON_BUFFER);
        popupButton.setWidth(POPUP_WIDTH - POPUP_BUTTON_BUFFER);
        popupButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCurrentState = CHOOSE_PLAYER_CARD;
                mPopupWindow.dismiss();
                switchPlayer();
                return true;
            }
        });
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.addView(popupButton);
//        Drawable background = new ShapeDrawable(new RectShape());
//        linearLayout.setBackground();
        PopupWindow window = new PopupWindow(linearLayout, POPUP_WIDTH, POPUP_HEIGHT);
        window.setContentView(linearLayout);
        return window;
    }

    private void switchPlayer() {
        invalidate();
    }

    private void loadAssets() {
        mCardBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cards);
        mCardBackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.card_back);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        mCardBackRotatedBitmap = Bitmap.createBitmap(mCardBackBitmap, 0, 0,
                mCardBackBitmap.getWidth(), mCardBackBitmap.getHeight(), matrix, true);
        Card.card_height = mCardBitmap.getHeight() / 4;
        Card.card_width = mCardBitmap.getWidth() / 13;
    }

	public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		drawBackground(canvas);
        drawCards(canvas);
        drawDeck(canvas);
        drawOpponentCards(canvas);
        drawScores(canvas);
	}

    private void drawScores(Canvas canvas) {
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
                    mScreenWidth - Card.card_height - TEXT_BUFFER - (SCORE_SIZE * (s.length() - 1)),
                    mScreenHeight / 2 - (SCORE_SIZE), p);
        }
    }

    private void drawCards(Canvas canvas) {
        switch (mCurrentState) {
            case CHOOSE_PLAYER_CARD:
                int yPos = mScreenHeight - Card.card_height - SCREEN_BUFFER;
                List<Card> cardList = mCurrentPlayer.getCards();
                int cardSpacing = Math.min(Card.card_width,
                        (mScreenWidth - (3 * SCREEN_BUFFER)) / (cardList.size() + 1));
                for (int i = 0 ; i < cardList.size() ; i++) {
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
        int leftSpacing = Math.min(Card.card_width,
                (mScreenHeight - Card.card_width - (2 * PLAYER_BUFFERS)) /
                        (mLeftPlayerCardCount));
        int xPos = SCREEN_BUFFER;
        for (int i = 0 ; i < mLeftPlayerCardCount ; i++) {
            Rect dstRect = new Rect(xPos, i * leftSpacing + PLAYER_BUFFERS, xPos + Card.card_height,
                    i * leftSpacing + PLAYER_BUFFERS + Card.card_width);
            canvas.drawBitmap(mCardBackRotatedBitmap, null, dstRect, null);
        }

        // Top Player's hand
        int topSpacing = Math.min(Card.card_width,
                (mScreenWidth - (2 * PLAYER_BUFFERS)) / (mTopPlayerCardCount + 1));
        for (int i = 0 ; i <mTopPlayerCardCount ; i++) {
            Rect dstRect = new Rect(i * topSpacing + PLAYER_BUFFERS, SCREEN_BUFFER,
                    i * topSpacing + PLAYER_BUFFERS + Card.card_width,
                    SCREEN_BUFFER + Card.card_height);
            canvas.drawBitmap(mCardBackBitmap, null, dstRect, null);
        }

        // Right Player's hand
        int rightSpacing = Math.min(Card.card_width,
                (mScreenHeight - Card.card_width - (2 * PLAYER_BUFFERS)) / mRightPlayerCardCount);
        xPos = mScreenWidth - SCREEN_BUFFER - Card.card_height;
        for (int i = 0 ; i < mRightPlayerCardCount ; i++) {
            Rect dstRect = new Rect(xPos, i * rightSpacing + PLAYER_BUFFERS,
                    xPos + Card.card_height, i * rightSpacing + PLAYER_BUFFERS + Card.card_width);
            canvas.drawBitmap(mCardBackBitmap, null, dstRect, null);
        }
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
            switch (mCurrentState) {
                case CHOOSE_PLAYER_CARD:
                    int index = findCardIndex(p);
                    if (index > -1) {
                        mCurrentPlayer.getCards().remove(index);
                        mCurrentState = WAIT_FOR_NEXT_PLAYER;
                        mPopupWindow = createPopupWindow();
                        mPopupWindow.showAsDropDown(view, mScreenWidth / 2 - (POPUP_WIDTH / 2),
                                -1 * mScreenHeight / 2 - (POPUP_HEIGHT / 2));
                        invalidate();
                    }
                    break;

                case WAIT_FOR_NEXT_PLAYER:
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

package com.example.simcards;

import android.graphics.Point;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;

import android.view.Display;
import android.view.Window;

public class CardacopiaInterface extends Activity {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
		setContentView(R.layout.activity_cardacopia);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_HEIGHT = size.y;
        SCREEN_WIDTH = size.x;
		
		GameView gameview = new GameView(this);
		gameview.setBackgroundColor(Color.parseColor("#002400"));
		setContentView(gameview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

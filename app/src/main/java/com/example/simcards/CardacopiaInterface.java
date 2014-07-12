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
import android.content.Intent;

public class CardacopiaInterface extends Activity {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String player1 = intent.getStringExtra("com.example.simcards.player1");
        String player2 = intent.getStringExtra("com.example.simcards.player2");
        String player3 = intent.getStringExtra("com.example.simcards.player3");
        String player4 = intent.getStringExtra("com.example.simcards.player4");

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
		setContentView(R.layout.activity_cardacopia);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_HEIGHT = size.y;
        SCREEN_WIDTH = size.x;
		
		GameView gameview = new GameView(this, player1, player2, player3, player4);
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

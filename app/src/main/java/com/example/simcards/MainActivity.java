package com.example.simcards;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
<<<<<<< HEAD
import android.view.Menu;

public class MainActivity extends Activity {
	
	
=======
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
>>>>>>> origin/master

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
<<<<<<< HEAD
		
		GameView gameview = new GameView(this);
		gameview.setBackgroundColor(Color.WHITE);
		setContentView(gameview);
=======

		TestingView testview = new TestingView(this);
		testview.setBackgroundColor(Color.WHITE);
		setContentView(testview);
		/*
        View content = getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        Log.d("DISPLAY", content.getWidth() + " x " + content.getHeight());
        */
>>>>>>> origin/master
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

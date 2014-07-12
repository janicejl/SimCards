package com.example.simcards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

/**
 * Created by Antonio on 7/12/2014.
 */
public class HungerActivity extends Activity {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_HEIGHT = size.y;
        SCREEN_WIDTH = size.x;

        HungerView hungerView = new HungerView(this);
        hungerView.setBackgroundColor(Color.parseColor("#002400"));
    }
}

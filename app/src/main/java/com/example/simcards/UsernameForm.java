package com.example.simcards;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.content.Intent;
import android.widget.EditText;

import com.example.simcards.R;

public class UsernameForm extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_username_form);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.username_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startCardacopia(View view) {
        Intent intent = new Intent(this, CardacopiaInterface.class);

        EditText player1 = (EditText) findViewById(R.id.player1);
        String player = player1.getText().toString();
        intent.putExtra("com.example.simcards.player1", player);

        EditText player2 = (EditText) findViewById(R.id.player2);
        player = player2.getText().toString();
        intent.putExtra("com.example.simcards.player2", player);

        EditText player3 = (EditText) findViewById(R.id.player3);
        player = player3.getText().toString();
        intent.putExtra("com.example.simcards.player3", player);

        EditText player4 = (EditText) findViewById(R.id.player4);
        player = player4.getText().toString();
        intent.putExtra("com.example.simcards.player4", player);

        startActivity(intent);
    }
}

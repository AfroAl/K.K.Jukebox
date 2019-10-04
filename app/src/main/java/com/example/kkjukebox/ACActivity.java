package com.example.kkjukebox;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class ACActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private int playPause = -1;
    private ImageButton musicControl;
    private Timer t = new Timer();

    private RadioButton sunny, rainy;
    private RadioGroup weatherRadio;

    private Button acnl, accf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);

        //Set pause/play button
        playPause = 1;
        musicControl = findViewById(R.id.pause_acnl);

        //Set radio buttons
        sunny = findViewById(R.id.sunny_acnl);
        rainy = findViewById(R.id.rainy_acnl);

        //Set listener for when a radio button is pressed
        weatherRadio = findViewById(R.id.weather_acnl);
        weatherRadio.setOnCheckedChangeListener(this);

        //Set buttons to change game music
        acnl = findViewById(R.id.ac_acnl);
        accf = findViewById(R.id.accf_acnl);


        if(sunny.isChecked()) //Start schedule for sunny music
        {
            t.schedule(new TimeCheck_acnl(this, 0), 0, 500);
        }
        else if(rainy.isChecked())  //Start schedule for rainy music
        {
            t.schedule(new TimeCheck_acnl(this, 1), 0, 500);
        }
    }

    //Called on BACK button pressed on phone/ app dismissed in Overview
    protected void onDestroy() {
        super.onDestroy();
        t.cancel();
        t.purge();
        stopService(new Intent(this, SunnyService_ac.class));
        stopService(new Intent(this, RainyService_ac.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //Change weather type via Radio Buttons
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(playPause == 0) {
            musicControl.setImageResource(R.drawable.pause);
            playPause = 1;
        }

        if(sunny.isChecked()) {
            t.schedule(new TimeCheck_ac(this, 0), 0, 500);
        }
        else if(rainy.isChecked()) {
            t.schedule(new TimeCheck_ac(this, 1), 0, 500);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == acnl) { //Switch to Animal Crossing: New Leaf music
            Intent i = new Intent(this, ACNLActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else if(v == accf) { //Switch to Animal Crossing: City Folk music
            Intent i = new Intent(this, ACCFActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else if(v == musicControl) { //Pause/ play music
            if(playPause == 0) { //Play music
                musicControl.setImageResource(R.drawable.pause);
                onCheckedChanged(weatherRadio, 0);
                playPause = 1;
            }
            else if(playPause == 1){ //Stop music
                t = new Timer();
                musicControl.setImageResource(R.drawable.play);
                stopService(new Intent(this, SunnyService_ac.class));
                stopService(new Intent(this, RainyService_ac.class));
                playPause = 0;
            }
        }
    }
}

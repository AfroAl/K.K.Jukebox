package com.example.kkjukebox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Timer;

public class ACNLActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private int playPause = -1;
    private ImageButton musicControl;
    private Timer t = new Timer();

    private RadioButton sunny, rainy, snowy;
    private RadioGroup weatherRadio;
    private boolean[] radio = new boolean[2];

    private Button ac, accf;
    private ImageButton kk;

    public static final String RAINY_STATE = "Rainy_State";
    public static final String SNOWY_STATE = "Snowy_State";
    public static final String PREFERENCES = "Prefs";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acnl);
        Toolbar toolbar = findViewById(R.id.toolbar_acnl);
        setSupportActionBar(toolbar);

        //Get saved preferences for radio buttons
        sp = getSharedPreferences(PREFERENCES, 0);
        boolean rs = sp.getBoolean(RAINY_STATE, false);
        boolean ss = sp.getBoolean(SNOWY_STATE, false);

        //Set pause/play button
        playPause = 1;
        musicControl = findViewById(R.id.pause_acnl);

        //Set radio buttons
        sunny = findViewById(R.id.sunny_acnl);
        rainy = findViewById(R.id.rainy_acnl);
        snowy = findViewById(R.id.snowy_acnl);

        //Set listener for when a radio button is pressed
        weatherRadio = findViewById(R.id.weather_acnl);
        weatherRadio.setOnCheckedChangeListener(this);

        //Decide which radio button to press at launch
        if(!rs && !ss) {
            sunny.setChecked(true);
        }
        else {
            if(rs) {
                rainy.setChecked(rs);
            }
            else {
                snowy.setChecked(ss);
            }
        }

        //Set buttons to change game music
        ac = findViewById(R.id.ac_acnl);
        accf = findViewById(R.id.accf_acnl);

        kk = findViewById(R.id.kk_acnl);
        kk.setOnClickListener(this);
    }

    //Called on BACK button pressed on phone/ app dismissed in Overview
    protected void onDestroy() {
        super.onDestroy();
        t.cancel();
        t.purge();
        stopService(new Intent(this, SunnyService_acnl.class));
        stopService(new Intent(this, RainyService_acnl.class));
        stopService(new Intent(this, SnowyService_acnl.class));
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

        SharedPreferences.Editor e = sp.edit();

        if(sunny.isChecked()) {
            e.putBoolean(RAINY_STATE, false);
            e.putBoolean(SNOWY_STATE, false);
            t.schedule(new TimeCheck_acnl(this, 0), 0, 500);
        }
        else if(rainy.isChecked()) {
            e.putBoolean(RAINY_STATE, rainy.isChecked());
            e.putBoolean(SNOWY_STATE, false);
            t.schedule(new TimeCheck_acnl(this, 1), 0, 500);
        }
        else if(snowy.isChecked()) {
            e.putBoolean(RAINY_STATE, false);
            e.putBoolean(SNOWY_STATE, snowy.isChecked());
            t.schedule(new TimeCheck_acnl(this, 2), 0, 500);
        }

        e.apply();
    }


    @Override
    public void onClick(View v) {
        if(v == ac) { //Switch to Animal Crossing (GC) music
            Intent i = new Intent(this, ACActivity.class);
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
                stopService(new Intent(this, SunnyService_acnl.class));
                stopService(new Intent(this, RainyService_acnl.class));
                stopService(new Intent(this, SnowyService_acnl.class));
                playPause = 0;
            }
        }
    }
}

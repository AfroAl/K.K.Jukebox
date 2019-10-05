package com.example.kkjukebox;

import android.app.Dialog;
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
    private ImageButton kk;
    private int kk_pref;

    private RadioButton always;
    private RadioButton saturday;
    private RadioButton never;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ac);

        int weather = getIntent().getIntExtra("weather", 0);
        kk_pref = getIntent().getIntExtra("kk", 2);

        //Set pause/play button
        playPause = 1;
        musicControl = findViewById(R.id.pause_ac);

        //Set radio buttons
        sunny = findViewById(R.id.sunny_ac);
        rainy = findViewById(R.id.rainy_ac);

        //Set listener for when a radio button is pressed
        weatherRadio = findViewById(R.id.weather_ac);
        weatherRadio.setOnCheckedChangeListener(this);
        weatherRadio.clearCheck();

        //Set buttons to change game music
        acnl = findViewById(R.id.acnl_ac);
        accf = findViewById(R.id.accf_ac);

        kk = findViewById(R.id.kk_ac);
        kk.setOnClickListener(this);

        if(weather == 0) {
            sunny.setChecked(true);
        }
        else if(weather > 0) {
            rainy.setChecked(true);
        }
    }

    //Called on BACK button pressed on phone/ app dismissed in Overview
    protected void onDestroy() {
        super.onDestroy();
        t.cancel();
        t.purge();
        stopService(new Intent(this, SunnyService_ac.class));
        stopService(new Intent(this, RainyService_ac.class));
        stopService(new Intent(this, kkAlwaysService.class));
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
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
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
            t.schedule(new TimeCheck_ac(this, 0, kk_pref), 0, 500);
        }
        else if(rainy.isChecked()) {
            t.schedule(new TimeCheck_ac(this, 1, kk_pref), 0, 500);
        }
    }

    private void showRadioButtonDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.radiobutton_dialog);
        RadioGroup rg = dialog.findViewById(R.id.kk_ac);
        always = dialog.findViewById(R.id.always);
        saturday = dialog.findViewById(R.id.saturday);
        never = dialog.findViewById(R.id.never);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println(kk_pref);
                if(always.isChecked()) {
                    kk_pref = 0;
                }
                else if(saturday.isChecked()) {
                    kk_pref = 1;
                }
                else if(never.isChecked()) {
                    kk_pref = 2;
                }

                ACActivity.this.onCheckedChanged(weatherRadio, 0);
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == kk) {
            showRadioButtonDialog();
        }
        else if(v == acnl) { //Switch to Animal Crossing: New Leaf music
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
                stopService(new Intent(this, kkAlwaysService.class));
                playPause = 0;
            }
        }
    }
}

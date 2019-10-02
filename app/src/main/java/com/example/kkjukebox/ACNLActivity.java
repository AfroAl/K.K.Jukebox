package com.example.kkjukebox;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Timer;

public class ACNLActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private ImageButton stop;
    private int playPause = -1;
    private Timer t = new Timer();
    private RadioButton sunny, rainy, snowy;
    private Button ac, acww, accf, acnl;
    private int weatherButton = -1;
    private RadioGroup weatherRadio;

    private Button startTimer, resetTimer;
    private CountDownTimer countDownTimer;
    private TextView tv;
    private EditText n;
    //private Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acnl);
        Toolbar toolbar = findViewById(R.id.toolbar_acnl);
        setSupportActionBar(toolbar);

        sunny = findViewById(R.id.sunny_acnl);
        rainy = findViewById(R.id.rainy_acnl);
        snowy = findViewById(R.id.snowy_acnl);

        ac = findViewById(R.id.ac_acnl);
        acww = findViewById(R.id.acww_acnl);
        accf = findViewById(R.id.accf_acnl);
        acnl = findViewById(R.id.acnl_acnl);

        startTimer = findViewById(R.id.setTimer);
        resetTimer = findViewById(R.id.resetTimer);
        n = findViewById(R.id.minsToSleep);
        tv = findViewById(R.id.TIME);

        startTimer.setOnClickListener(this);
        resetTimer.setOnClickListener(this);


        stop = findViewById(R.id.pause_acnl);
        playPause = 1;
        weatherRadio = findViewById(R.id.weather_acnl);
        weatherRadio.setOnCheckedChangeListener(this);

        if(sunny.isChecked())
        {
            weatherButton = 0;
            t.schedule(new TimeCheck_acnl(this, 0), 0, 500);
        }
        else if(rainy.isChecked())
        {
            weatherButton = 1;
            t.schedule(new TimeCheck_acnl(this, 1), 0, 500);
        }
        else if(snowy.isChecked())
        {
            weatherButton = 2;
            t.schedule(new TimeCheck_acnl(this, 2), 0, 500);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        t.cancel();
        t.purge();
        stopService(new Intent(this, SunnyService_acnl.class));
        stopService(new Intent(this, RainyService_acnl.class));
        stopService(new Intent(this, SnowyService_acnl.class));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        t.purge();
        if(sunny.isChecked())
        {
            weatherButton = 0;
            t.schedule(new TimeCheck_acnl(this, weatherButton), 0, 500);
        }
        else if(rainy.isChecked())
        {
            weatherButton = 1;
            t.schedule(new TimeCheck_acnl(this, weatherButton), 0, 500);
        }
        else if(snowy.isChecked())
        {
            weatherButton = 2;
            t.schedule(new TimeCheck_acnl(this, weatherButton), 0, 500);
        }
    }

    @Override
    public void onClick(View v) {

        /*if(v == startTimer) {
            countDownTimer = new CountDownTimer(Integer.parseInt(n.getText().toString())*60000, 1000) {
                private int minutes = Integer.parseInt(n.getText().toString());
                private int seconds = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    if(seconds == 0)
                    {
                        minutes--;
                        seconds = 59;
                    }

                    String s;
                    if(seconds < 10 && minutes < 10) {
                        s = "0" + minutes + ":0" + seconds;
                    }
                    else if(seconds < 10) {
                        s = minutes + ":0" + seconds;
                    }
                    else if(minutes < 10) {
                        s = "0" + minutes + ":" + seconds;
                    }
                    else {
                        s = minutes + ":" + seconds;
                    }

                    millisUntilFinished = millisUntilFinished/1000;
                    tv.setText(s);
                    seconds--;
                }

                @Override
                public void onFinish() {
                    onDestroy();
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                }
            }.start();
        }
        else if(v == resetTimer) {
            countDownTimer.cancel();
            tv.setText("00:00");
        }*/
        if(v == ac) {
            Intent i = new Intent(this, ACActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
        else if(v == acww ) {
            Intent i = new Intent(this, ACWWActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        else if(v == accf) {
            Intent i = new Intent(this, ACCFActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else if(v == acnl) {
            //Do Nothing
        }
        else if(v == stop) {
            if(playPause == 0)
            {
                System.out.println("YES");
                stop.setImageResource(R.drawable.pause);
                onCheckedChanged(weatherRadio, 0);
                playPause = 1;
                //show_Notification_Ongoing();
            }
            else if(playPause == 1)
            {
                stop.setImageResource(R.drawable.play);
                stopService(new Intent(this, SunnyService_acnl.class));
                stopService(new Intent(this, RainyService_acnl.class));
                stopService(new Intent(this, SnowyService_acnl.class));
                playPause = 0;
                //show_Notification();
            }
        }

    }
}

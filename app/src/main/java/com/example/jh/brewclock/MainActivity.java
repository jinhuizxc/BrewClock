package com.example.jh.brewclock;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    
     private static final String TAG = "MainActivity";
    /**
     * Properties
     **/
    protected Button brewAddTime;
    protected Button brewDecreaseTime;
    protected Button startBrew;
    protected TextView brewCountLabel;
    protected TextView brewTimeLabel;

    protected int brewTime = 3; // 默认3分钟
    protected CountDownTimer brewCountDownTimer;
    protected int brewCount = 0;
    protected boolean isBrewing = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect interface elements to properties
        brewAddTime = (Button) findViewById(R.id.brew_time_up);
        brewDecreaseTime = (Button) findViewById(R.id.brew_time_down);
        startBrew = (Button) findViewById(R.id.brew_start);
        brewCountLabel = (TextView) findViewById(R.id.brew_count_label);
        brewTimeLabel = (TextView) findViewById(R.id.brew_time);

        // Setup ClickListeners
        brewAddTime.setOnClickListener(this);
        brewDecreaseTime.setOnClickListener(this);
        startBrew.setOnClickListener(this);

        // Set the initial brew values
        setBrewCount(0);
        setBrewTime(3);
    }

    // 设置初始时间
    @SuppressLint("SetTextI18n")
    private void setBrewTime(int minutes) {
        if (isBrewing)
            return;

        brewTime = minutes;
        Log.e(TAG, "brewTime = " + brewTime);
        if (brewTime < 1) {
            brewTime = 1;
        }

        brewTimeLabel.setText(String.valueOf(brewTime) + "m");
    }

    private void setBrewCount(int count) {
        brewCount = count;
        brewCountLabel.setText(String.valueOf(brewCount));
    }

    @Override
    public void onClick(View view) {
        if (view == brewAddTime) {
            setBrewTime(brewTime + 1);
        } else if (view == brewDecreaseTime) {
            setBrewTime(brewTime - 1);
        } else if (view == startBrew) {
            if (isBrewing) {
                stopBrew();
            } else {
                startBrew();
            }
        }
    }

    /**
     * 停止计时
     */
    private void stopBrew() {
        if (brewCountDownTimer != null) {
            brewCountDownTimer.cancel();
        }
        isBrewing = false;
        startBrew.setText("Start");
    }

    /**
     * 开始倒计时
     */
    private void startBrew() {
        brewCountDownTimer = new CountDownTimer(brewTime * 60 * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                // 倒计时的log时间为ms单位
                //  millisUntilFinished = 59999
                // 04-13 14:15:50.537 9750-9750/com.example.jh.brewclock E/MainActivity: millisUntilFinished = 58999
                // 04-13 14:15:51.540 9750-9750/com.example.jh.brewclock E/MainActivity: millisUntilFinished = 57996
                // 04-13 14:15:52.542 9750-9750/com.example.jh.brewclock E/MainActivity: millisUntilFinished = 56995
                Log.e(TAG, "millisUntilFinished = " + millisUntilFinished);
                brewTimeLabel.setText(String.valueOf(millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish");
                isBrewing = false;
                startBrew.setText("Start");
                brewTimeLabel.setText("Brew Up!");
                // 设置brewcount + 1
                setBrewCount(brewCount + 1);

            }
        };

        brewCountDownTimer.start();
        startBrew.setText("Stop");
        isBrewing = true;
    }
    
}

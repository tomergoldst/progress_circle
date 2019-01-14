/*
 Copyright 2016 Tomer Goldstein

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tomergoldst.progresscircledemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.tomergoldst.progress_circle.ProgressCircle;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener,
        View.OnClickListener{

    public static final int minWidthDp = 2;

    private ProgressCircle mProgressCircle;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressCircle = findViewById(R.id.circle_progress);

        SeekBar seekBarCurrentProgress = findViewById(R.id.seek_bar_current_progress);
        SeekBar seekBarProgressWidth = findViewById(R.id.seek_bar_progress_width);
        SeekBar seekBarOutlineWidth = findViewById(R.id.seek_bar_outline_width);
        SeekBar seekBarProgressViewSize = findViewById(R.id.seek_bar_progress_view_size);

        seekBarCurrentProgress.setOnSeekBarChangeListener(this);
        seekBarProgressWidth.setOnSeekBarChangeListener(this);
        seekBarOutlineWidth.setOnSeekBarChangeListener(this);
        seekBarProgressViewSize.setOnSeekBarChangeListener(this);

        mProgressCircle.setProgressWithoutAnimation(seekBarCurrentProgress.getProgress());
        mProgressCircle.setProgressWidth(dpToPx(seekBarProgressWidth.getProgress()));
        mProgressCircle.setOutlineWidth(dpToPx(seekBarOutlineWidth.getProgress()));

        View backgroundPrimary = findViewById(R.id.view_color_primary);
        View backgroundAccent = findViewById(R.id.view_color_accent);
        View backgroundGreen = findViewById(R.id.view_color_green);
        View backgroundOrange = findViewById(R.id.view_color_orange);

        backgroundPrimary.setOnClickListener(this);
        backgroundAccent.setOnClickListener(this);
        backgroundGreen.setOnClickListener(this);
        backgroundOrange.setOnClickListener(this);

        mHandler = new Handler();
        final Random random = new Random();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mProgressCircle.setProgress(random.nextInt(100));
                mHandler.postDelayed(this, 1000);

            }
        };

        Button startAnimationBtn = (Button) findViewById(R.id.button_start_animate);
        startAnimationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.post(runnable);
            }
        });

        Button stopAnimationBtn = (Button) findViewById(R.id.button_stop_animate);
        stopAnimationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.removeCallbacksAndMessages(null);
            }
        });


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()){
            case R.id.seek_bar_current_progress:
                mProgressCircle.setProgressWithoutAnimation(i);
                break;
            case R.id.seek_bar_progress_width:
                mProgressCircle.setProgressWidth(dpToPx(i + minWidthDp));
                break;
            case R.id.seek_bar_outline_width:
                mProgressCircle.setOutlineWidth(dpToPx(i + minWidthDp));
                break;
            case R.id.seek_bar_progress_view_size:
                int pPx = dpToPx(i * 5);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mProgressCircle.getLayoutParams();
                params.setMargins(pPx, pPx, pPx, pPx);
                mProgressCircle.setLayoutParams(params);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private int dpToPx(int valueDp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                valueDp, getResources().getDisplayMetrics());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.view_color_primary:
                mProgressCircle.setProgressColor(getResources().getColor(R.color.colorPrimary));
                mProgressCircle.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.view_color_accent:
                mProgressCircle.setProgressColor(getResources().getColor(R.color.colorAccent));
                mProgressCircle.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case R.id.view_color_green:
                mProgressCircle.setProgressColor(getResources().getColor(android.R.color.holo_green_light));
                mProgressCircle.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                break;
            case R.id.view_color_orange:
                mProgressCircle.setProgressColor(getResources().getColor(android.R.color.holo_orange_light));
                mProgressCircle.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
                break;
        }
    }
}

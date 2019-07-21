package com.example.pocketmodular.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.pocketmodular.MyApplication;
import com.example.pocketmodular.R;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import org.puredata.core.PdBase;

public class Oscillator extends FrameLayout {
    /*vars*/
    private MyApplication mApplication;
    private Boolean isCollapsed;

    /*ui*/
    private FrameLayout mOscName;
    private LinearLayout mOcsControls;
    private RangeSeekBar mWaveformSeekBar;
    private RangeSeekBar mTuneSeekBar;
    private RangeSeekBar mPwSeekBar;
    private RangeSeekBar mFmSeekBar;

    public Oscillator(Context context) {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_oscillator, this);
        mOscName = findViewById(R.id.oscillatorNameFrameLayout);
        mOcsControls = findViewById(R.id.oscillatorControlsLinearLayout);
        mWaveformSeekBar = findViewById(R.id.waveformSeekBar);
        mTuneSeekBar = findViewById(R.id.tuneSeekBar);
        mPwSeekBar = findViewById(R.id.pwSeekBar);
        mFmSeekBar = findViewById(R.id.fmSeekBar);

        mOscName.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mOscName.getLayoutParams().height));
        mOcsControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), mOcsControls.getLayoutParams().height));

        mTuneSeekBar.setIndicatorTextDecimalFormat("0.0");
        mPwSeekBar.setIndicatorTextDecimalFormat("0.0");
        mFmSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*OnClick*/
        mOscName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mOcsControls.getHeight());
                mOcsControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        mWaveformSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if (leftValue == 0) {
                    PdBase.sendFloat("oscSine", 0f);
                    PdBase.sendFloat("oscTriangle", 0f);
                    PdBase.sendFloat("oscSquare", 0f);
                } else if (leftValue == 1) {
                    PdBase.sendFloat("oscSine", 1.0f);
                    PdBase.sendFloat("oscTriangle", 0f);
                    PdBase.sendFloat("oscSquare", 0f);
                } else if (leftValue == 2) {
                    PdBase.sendFloat("oscSine", 0f);
                    PdBase.sendFloat("oscTriangle", 1.0f);
                    PdBase.sendFloat("oscSquare", 0f);
                } else if (leftValue == 3) {
                    PdBase.sendFloat("oscSine", 0f);
                    PdBase.sendFloat("oscTriangle", 0f);
                    PdBase.sendFloat("oscSquare", 1.0f);
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mTuneSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mPwSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mFmSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });
    }
}

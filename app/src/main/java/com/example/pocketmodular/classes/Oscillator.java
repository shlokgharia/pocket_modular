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

import java.io.IOException;

public class Oscillator extends FrameLayout {
    /*vars*/
    MyApplication mApplication;
    private PdModule pdModule;
    private Boolean isCollapsed;

    /*ui*/
    private LinearLayout oscControlsContainer;
    private RangeSeekBar waveformSeekBar;
    private RangeSeekBar tuneSeekBar;
    private RangeSeekBar pwSeekBar;
    private RangeSeekBar fmSeekBar;

    public Oscillator(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        pdModule = new PdModule();
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_oscillator, this);
        FrameLayout oscNameContainer = findViewById(R.id.oscillatorName_container);
        oscNameContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), oscNameContainer.getLayoutParams().height));
        oscControlsContainer = findViewById(R.id.oscillatorControls_container);
        oscControlsContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), oscControlsContainer.getLayoutParams().height));

        waveformSeekBar = findViewById(R.id.waveformSeekBar);
        tuneSeekBar = findViewById(R.id.tuneSeekBar);
        pwSeekBar = findViewById(R.id.pwSeekBar);
        fmSeekBar = findViewById(R.id.fmSeekBar);

        tuneSeekBar.setIndicatorTextDecimalFormat("0.0");
        pwSeekBar.setIndicatorTextDecimalFormat("0.0");
        fmSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*initPd*/
        pdModule.openPatch(context, "oscillator");

        /*OnClick*/
        oscNameContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, oscControlsContainer.getHeight());
                oscControlsContainer.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        waveformSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
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

        tuneSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        pwSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        fmSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
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

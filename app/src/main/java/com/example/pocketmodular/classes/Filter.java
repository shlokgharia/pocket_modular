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

public class Filter extends FrameLayout {
    /*vars*/
    private MyApplication mApplication;
    private Boolean isCollapsed;

    /*ui*/
    private FrameLayout mFilterName;
    private LinearLayout mFilterControls;
    private RangeSeekBar mFrequencySeekBar;
    private RangeSeekBar mResonanceSeekBar;
    private RangeSeekBar mFilterTypeSeekBar;

    public Filter(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_filter, this);
        mFilterName = findViewById(R.id.filterNameFrameLayout);
        mFilterControls = findViewById(R.id.filterControlsLinearLayout);
        mFilterTypeSeekBar = findViewById(R.id.filterTypeSeekBar);
        mFrequencySeekBar = findViewById(R.id.frequencySeekBar);
        mResonanceSeekBar = findViewById(R.id.resonanceSeekBar);

        mFilterName.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mFilterName.getLayoutParams().height));
        mFilterControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), mFilterControls.getLayoutParams().height));

        mFrequencySeekBar.setIndicatorTextDecimalFormat("0.0");
        mResonanceSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*OnClick*/
        mFilterName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mFilterControls.getHeight());
                mFilterControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        mFilterTypeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if (leftValue == 0) {
                    PdBase.sendFloat("fltLoOn", 0f);
                    PdBase.sendFloat("fltHighOn", 0f);
                    PdBase.sendFloat("fltBandOn", 0f);
                } else if (leftValue == 1) {
                    PdBase.sendFloat("fltLoOn", 1.0f);
                    PdBase.sendFloat("fltHighOn", 0f);
                    PdBase.sendFloat("fltBandOn", 0f);
                } else if (leftValue == 2) {
                    PdBase.sendFloat("fltLoOn", 0f);
                    PdBase.sendFloat("fltHighOn", 1.0f);
                    PdBase.sendFloat("fltBandOn", 0f);
                } else if (leftValue == 3) {
                    PdBase.sendFloat("fltLoOn", 0f);
                    PdBase.sendFloat("fltHighOn", 0f);
                    PdBase.sendFloat("fltBandOn", 1.0f);
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mFrequencySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("fltFrequency", leftValue/40);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mResonanceSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("fltResonance", leftValue*25);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

    }
}

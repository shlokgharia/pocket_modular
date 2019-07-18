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
    MyApplication mApplication;
    private PdModule pdModule;
    private Boolean isCollapsed;

    /*ui*/
    private LinearLayout filterControlsContainer;
    private RangeSeekBar frequencySeekBar;
    private RangeSeekBar resonanceSeekBar;
    private RangeSeekBar filterTypeSeekBar;

    public Filter(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        pdModule = new PdModule();
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_filter, this);
        FrameLayout filterNameContainer = findViewById(R.id.filterName_container);
        filterNameContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), filterNameContainer.getLayoutParams().height));
        filterControlsContainer = findViewById(R.id.filterControls_container);
        filterControlsContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), filterControlsContainer.getLayoutParams().height));

        filterTypeSeekBar = findViewById(R.id.filterTypeSeekBar);
        frequencySeekBar = findViewById(R.id.frequencySeekBar);
        resonanceSeekBar = findViewById(R.id.resonanceSeekBar);

        frequencySeekBar.setIndicatorTextDecimalFormat("0");
        resonanceSeekBar.setIndicatorTextDecimalFormat("0.00");

        /*initPd*/
        pdModule.openPatch(context, "filter");

        /*OnClick*/
        filterNameContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, filterControlsContainer.getHeight());
                filterControlsContainer.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        filterTypeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
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

        frequencySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("fltFrequency", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        resonanceSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("fltResonance", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

    }
}

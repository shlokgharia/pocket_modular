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
    private RangeSeekBar feedbackSeekBar;

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

        frequencySeekBar = findViewById(R.id.frequencySeekBar);
        resonanceSeekBar = findViewById(R.id.resonanceSeekBar);
        feedbackSeekBar = findViewById(R.id.feedbackSeekBar);

        frequencySeekBar.setIndicatorTextDecimalFormat("0.0");
        resonanceSeekBar.setIndicatorTextDecimalFormat("0.0");
        feedbackSeekBar.setIndicatorTextDecimalFormat("0.0");

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

        feedbackSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
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

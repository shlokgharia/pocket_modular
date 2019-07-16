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

public class Amplifier extends FrameLayout {
    /*vars*/
    MyApplication mApplication;
    private PdModule pdModule;
    private Boolean isCollapsed;

    /*ui*/
    private LinearLayout amplifierControlsContainer;
    private RangeSeekBar volumeSeekBar;

    public Amplifier(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        pdModule = new PdModule();
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_amplifier, this);
        FrameLayout filterNameContainer = findViewById(R.id.amplifierName_container);
        filterNameContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), filterNameContainer.getLayoutParams().height));
        amplifierControlsContainer = findViewById(R.id.amplifierControls_container);
        amplifierControlsContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), amplifierControlsContainer.getLayoutParams().height));

        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*initPd*/
        pdModule.openPatch(context, "amplifier");

        /*OnClick*/
        filterNameContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, amplifierControlsContainer.getHeight());
                amplifierControlsContainer.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        volumeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("ampVolume", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

    }
}

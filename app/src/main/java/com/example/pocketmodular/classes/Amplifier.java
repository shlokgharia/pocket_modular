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
    private MyApplication mApplication;
    private Boolean isCollapsed;

    /*ui*/
    private FrameLayout mAmplifierName;
    private LinearLayout mAmplifierControls;
    private RangeSeekBar mVolumeSeekBar;

    public Amplifier(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_amplifier, this);
        mAmplifierName = findViewById(R.id.amplifierName_container);
        mAmplifierControls = findViewById(R.id.amplifierControls_container);
        mVolumeSeekBar = findViewById(R.id.volumeSeekBar);

        mAmplifierName.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mAmplifierName.getLayoutParams().height));
        mAmplifierControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth()/2, mAmplifierControls.getLayoutParams().height));

        mVolumeSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*OnClick*/
        mAmplifierName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mAmplifierControls.getHeight());
                mAmplifierControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        mVolumeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("ampVolume", leftValue/100);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

    }
}

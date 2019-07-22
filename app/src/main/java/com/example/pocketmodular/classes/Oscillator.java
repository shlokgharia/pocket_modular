package com.example.pocketmodular.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private TextView mOscNameText;
    private FrameLayout mOscNameLayout;
    private LinearLayout mOcsControls;
    private RangeSeekBar mWaveformSeekBar;

    public Oscillator(Context context, final int moduleID) {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_oscillator, this);
        mOscNameText = findViewById(R.id.oscillatorNameText);
        mOscNameLayout = findViewById(R.id.oscillatorNameFrameLayout);
        mOcsControls = findViewById(R.id.oscillatorControlsLinearLayout);
        mWaveformSeekBar = findViewById(R.id.waveformSeekBar);

        mOscNameText.setText("osc " + moduleID);
        mOscNameLayout.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mOscNameLayout.getLayoutParams().height));
        mOcsControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth()/2, mOcsControls.getLayoutParams().height));

        /*OnClick*/
        mOscNameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth()/2 : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mOcsControls.getHeight());
                mOcsControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        mWaveformSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if (leftValue == 0) {
                    PdBase.sendFloat("oscSine_" + moduleID, 0f);
                    PdBase.sendFloat("oscTriangle_" + moduleID, 0f);
                    PdBase.sendFloat("oscSquare_" + moduleID, 0f);
                } else if (leftValue == 1) {
                    PdBase.sendFloat("oscSine_" + moduleID, 1.0f);
                    PdBase.sendFloat("oscTriangle_" + moduleID, 0f);
                    PdBase.sendFloat("oscSquare_" + moduleID, 0f);
                } else if (leftValue == 2) {
                    PdBase.sendFloat("oscSine_" + moduleID, 0f);
                    PdBase.sendFloat("oscTriangle_" + moduleID, 1.0f);
                    PdBase.sendFloat("oscSquare_" + moduleID, 0f);
                } else if (leftValue == 3) {
                    PdBase.sendFloat("oscSine_" + moduleID, 0f);
                    PdBase.sendFloat("oscTriangle_" + moduleID, 0f);
                    PdBase.sendFloat("oscSquare_" + moduleID, 1.0f);
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });
    }
}

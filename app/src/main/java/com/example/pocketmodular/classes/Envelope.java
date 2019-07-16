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

public class Envelope extends FrameLayout {
    /*vars*/
    MyApplication mApplication;
    private PdModule pdModule;
    private Boolean isCollapsed;

    /*ui*/
    private LinearLayout envelopeControlsContainer;
    private RangeSeekBar attackSeekBar;
    private RangeSeekBar decaySeekBar;
    private RangeSeekBar sustainSeekBar;
    private RangeSeekBar releaseSeekBar;

    public Envelope(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        pdModule = new PdModule();
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_envelope, this);
        FrameLayout envelopeNameContainer = findViewById(R.id.envelopeName_container);
        envelopeNameContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), envelopeNameContainer.getLayoutParams().height));
        envelopeControlsContainer = findViewById(R.id.envelopeControls_container);
        envelopeControlsContainer.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), envelopeControlsContainer.getLayoutParams().height));

        attackSeekBar = findViewById(R.id.attackSeekBar);
        decaySeekBar = findViewById(R.id.decaySeekBar);
        sustainSeekBar = findViewById(R.id.sustainSeekBar);
        releaseSeekBar = findViewById(R.id.releaseSeekBar);

        attackSeekBar.setIndicatorTextDecimalFormat("0.0");
        decaySeekBar.setIndicatorTextDecimalFormat("0.0");
        sustainSeekBar.setIndicatorTextDecimalFormat("0.0");
        releaseSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*initPd*/
        pdModule.openPatch(context, "envelope");

        /*OnClick*/
        envelopeNameContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, envelopeControlsContainer.getHeight());
                envelopeControlsContainer.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        attackSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envAttack", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        decaySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envDecay", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        sustainSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envSustain", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        releaseSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envRelease", leftValue);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });
    }
}

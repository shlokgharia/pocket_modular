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
    private MyApplication mApplication;
    private Boolean isCollapsed;

    /*ui*/
    private FrameLayout mEnvelopeName;
    private LinearLayout mEnvelopeControls;
    private RangeSeekBar mAttackSeekBar;
    private RangeSeekBar mDecaySeekBar;
    private RangeSeekBar mSustainSeekBar;
    private RangeSeekBar mReleaseSeekBar;

    public Envelope(Context context) throws IOException {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_envelope, this);
        mEnvelopeName = findViewById(R.id.envelopeNameFrameLayout);
        mEnvelopeControls = findViewById(R.id.envelopeControlsLinearLayout);
        mAttackSeekBar = findViewById(R.id.attackSeekBar);
        mDecaySeekBar = findViewById(R.id.decaySeekBar);
        mSustainSeekBar = findViewById(R.id.sustainSeekBar);
        mReleaseSeekBar = findViewById(R.id.releaseSeekBar);

        mEnvelopeName.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mEnvelopeName.getLayoutParams().height));
        mEnvelopeControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), mEnvelopeControls.getLayoutParams().height));

        mAttackSeekBar.setIndicatorTextDecimalFormat("0.0");
        mDecaySeekBar.setIndicatorTextDecimalFormat("0.0");
        mSustainSeekBar.setIndicatorTextDecimalFormat("0.0");
        mReleaseSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*OnClick*/
        mEnvelopeName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mEnvelopeControls.getHeight());
                mEnvelopeControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        mAttackSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envAttack", leftValue/5);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mDecaySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envDecay", leftValue/3);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mSustainSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envSustain", leftValue/10);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mReleaseSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envRelease", leftValue*100);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });
    }
}

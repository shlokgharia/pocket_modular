package com.example.pocketmodular.classes;

import android.content.Context;
import android.util.Log;
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

public class Envelope extends FrameLayout {
    /*vars*/
    private MyApplication mApplication;
    private Boolean isCollapsed;

    /*ui*/
    private TextView mEnvelopeNameText;
    private FrameLayout mEnvelopeNameLayout;
    private LinearLayout mEnvelopeControls;
    private RangeSeekBar mAttackSeekBar;
    private RangeSeekBar mDecaySeekBar;
    private RangeSeekBar mSustainSeekBar;
    private RangeSeekBar mReleaseSeekBar;

    public Envelope(Context context, final int moduleID) {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;
        final int attackNormalize = 3;
        final int decayNormalize = 5;
        final int sustainNormalize = 100;
        final int releaseNormalize = 10;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_envelope, this);
        mEnvelopeNameText = findViewById(R.id.envelopeNameText);
        mEnvelopeNameLayout = findViewById(R.id.envelopeNameFrameLayout);
        mEnvelopeControls = findViewById(R.id.envelopeControlsLinearLayout);
        mAttackSeekBar = findViewById(R.id.attackSeekBar);
        mDecaySeekBar = findViewById(R.id.decaySeekBar);
        mSustainSeekBar = findViewById(R.id.sustainSeekBar);
        mReleaseSeekBar = findViewById(R.id.releaseSeekBar);

        mEnvelopeNameText.setText("env " + moduleID);
        mEnvelopeNameLayout.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mEnvelopeNameLayout.getLayoutParams().height));
        mEnvelopeControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), mEnvelopeControls.getLayoutParams().height));

        mAttackSeekBar.setIndicatorTextDecimalFormat("0.0");
        mDecaySeekBar.setIndicatorTextDecimalFormat("0.0");
        mSustainSeekBar.setIndicatorTextDecimalFormat("0.0");
        mReleaseSeekBar.setIndicatorTextDecimalFormat("0.0");

        /*OnClick*/
        mEnvelopeNameLayout.setOnClickListener(new OnClickListener() {
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
                PdBase.sendFloat("envAttack_" + moduleID, leftValue*attackNormalize);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mDecaySeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envDecay_" + moduleID, leftValue*decayNormalize);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mSustainSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envSustain_" + moduleID, leftValue);
                Log.d("ENVELOPE", "onRangeChanged: Sustain" + leftValue/sustainNormalize);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });

        mReleaseSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                PdBase.sendFloat("envRelease_" + moduleID, leftValue);
                Log.d("ENVELOPE", "onRangeChanged: Release" + leftValue*releaseNormalize);
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {}
            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {}
        });
    }
}

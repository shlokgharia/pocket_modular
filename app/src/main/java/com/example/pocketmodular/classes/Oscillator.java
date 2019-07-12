package com.example.pocketmodular.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.pocketmodular.R;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.io.IOException;

public class Oscillator extends FrameLayout {
    /*ui*/
    RangeSeekBar rangeSeekBar;
    Button sineBtn;
    Button sawBtn;
    Button deleteBtn;

    /*vars*/
    private PdModule pdModule;
    private int waveshape;
    private float tune;
    private float pw;
    private float fm;

    public Oscillator(Context context) {
        super(context);
        pdModule = new PdModule();

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_oscillator, this);
        rangeSeekBar = findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setIndicatorTextDecimalFormat("0.0");

        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                Log.d("RangeSeek", leftValue + "");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });

        sineBtn = findViewById(R.id.sine);
        sawBtn = findViewById(R.id.saw);
        deleteBtn = findViewById(R.id.delete);

        sineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pdModule.openPatch(getContext(), "test");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pdModule.openPatch(getContext(), "test2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdModule.closePatch();
            }
        });

    }

    public void setTune(float tune) {
        this.tune = tune;
    }

    public void setPw(float pw) {
        this.pw = pw;
    }

    public void setFm(float fm) {
        this.fm = fm;
    }

}

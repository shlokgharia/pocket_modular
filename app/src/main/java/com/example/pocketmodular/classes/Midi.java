package com.example.pocketmodular.classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pocketmodular.MyApplication;
import com.example.pocketmodular.R;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import org.puredata.core.PdBase;

public class Midi extends FrameLayout {
    /*vars*/
    private MyApplication mApplication;
    private Boolean isCollapsed;
    private int mVoices;

    /*ui*/
    private FrameLayout mMidiNameLayout;
    private LinearLayout mMidiControls;
    private TextView mVoicesText;

    public Midi(Context context) {
        super(context);
        /*vars*/
        mApplication = ((MyApplication)context.getApplicationContext());
        isCollapsed = false;
        mVoices = 1;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_midi, this);
        mMidiNameLayout = findViewById(R.id.midiNameFrameLayout);
        mMidiControls = findViewById(R.id.midiControlsLinearLayout);
        Button mVoicesUpBtn = findViewById(R.id.voicesUpBtn);
        final Button mVoicesDownBtn = findViewById(R.id.voicesDownBtn);
        mVoicesText = findViewById(R.id.voicesText);

        mMidiNameLayout.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mMidiNameLayout.getLayoutParams().height));
        mMidiControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth()/2, mMidiControls.getLayoutParams().height));

        PdBase.sendFloat("midi_setting", mVoices);

        /*OnClick*/
        mMidiNameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed)? mApplication.getModuleControlsWidth()/2 : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mMidiControls.getHeight());
                mMidiControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        mVoicesUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoices < 4) {
                    PdBase.sendFloat("clear_setting", 1.0f);
                    PdBase.sendFloat("midi_setting",  ++mVoices);
                    mVoicesText.setText(Integer.parseInt(mVoicesText.getText().toString())+1 + "");
                }
            }
        });
        mVoicesDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoices > 1) {
                    PdBase.sendFloat("clear_setting", 1.0f);
                    PdBase.sendFloat("midi_setting", --mVoices);
                    mVoicesText.setText(Integer.parseInt(mVoicesText.getText().toString())-1 + "");
                }
            }
        });

    }
}

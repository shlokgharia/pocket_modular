package com.example.pocketmodular.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pocketmodular.MyApplication;
import com.example.pocketmodular.R;

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
        Button mVoicesDownBtn = findViewById(R.id.voicesDownBtn);
        mVoicesText = findViewById(R.id.voicesText);

        mMidiNameLayout.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mMidiNameLayout.getLayoutParams().height));
        mMidiControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth()/2, mMidiControls.getLayoutParams().height));

        PdBase.sendBang("midi_setting_1");

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
                    mVoices++;
                    setVoices();
                }
            }
        });
        mVoicesDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVoices > 1) {
                    mVoices--;
                    setVoices();
                }
            }
        });

    }

    private void setVoices() {
        String midiSetting = "midi_setting_";
        midiSetting += mVoices;

        PdBase.sendBang("clear_setting");
        PdBase.sendBang(midiSetting);

        mVoicesText.setText(mVoices + "");
    }
}

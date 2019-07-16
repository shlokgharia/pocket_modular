package com.example.pocketmodular;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pocketmodular.classes.Amplifier;
import com.example.pocketmodular.classes.Envelope;
import com.example.pocketmodular.classes.Filter;
import com.example.pocketmodular.classes.Note;
import com.example.pocketmodular.classes.Oscillator;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.core.PdBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /*vars*/
    List <FrameLayout> mPdModules;
    int mOctave;                    // current octave
    List<Note> mNotes;              // implements an onTouchListener for each note in the keyboard (see Notes.java)
    int screenWidth;

    /*ui*/
    LinearLayout mModuleContainer;
    TextView mOctaveDisplay;
    LinearLayout mKeyboardDisplay;  // this is the final keyboard display
                                    // it contains multiple octaves
                                    // each octave of notes is created through layout_notes.xml

    /*----Initial Setup----*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initPd();
        } catch (IOException e) {
            Log.d("Exception:", e + "");
            finish();
        }
        initGui();
        try {
            createRack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initGui() {
        /*vars*/
        mPdModules = new ArrayList<>();
        mNotes = new ArrayList<>();
        mOctave = 4;

        // Get & Set screen width
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        ((MyApplication) getApplication()).setScreenWidth(screenWidth);

        /*ui*/
        hideNotificationBar();
        mModuleContainer = findViewById(R.id.module_container);
        mKeyboardDisplay = findViewById(R.id.keyboard_display);
        mOctaveDisplay = findViewById(R.id.octave_display);
        Button mOctaveUpBtn = findViewById(R.id.octave_up_btn);
        Button mOctaveDownBtn = findViewById(R.id.octave_down_btn);

        /*constructs keyboard with 3 octaves*/
        List<View> tempOctaveDisplay = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            // gets an octave layout from "layout_notes"
            tempOctaveDisplay.add(LayoutInflater.from(this).inflate(R.layout.layout_notes, mKeyboardDisplay, false));
            // adds octave layout to keyboard display
            mKeyboardDisplay.addView(tempOctaveDisplay.get(i));
        }

        /*sets onTouchListener for every note of every octave in keyboard display*/
        final String[] tempNoteList = {"C", "Cs", "D", "Ds", "E", "F", "Fs", "G", "Gs", "A", "As", "B"};
        for (int octavePos = 0; octavePos < mKeyboardDisplay.getChildCount(); octavePos++) {
            for (int notPos = 0; notPos < tempNoteList.length; notPos++) {
                Note newNote = new Note(notPos,octavePos + mOctave);
                int id = getResources().getIdentifier(tempNoteList[notPos], "id", this.getPackageName());
                mKeyboardDisplay.getChildAt(octavePos).findViewById(id).setOnTouchListener(newNote);
                mNotes.add(newNote);
            }
        }

        /*octaveBtn onClick*/
        mOctaveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOctave(1);
                mOctaveDisplay.setText(Integer.parseInt(mOctaveDisplay.getText().toString())+1 + "");
            }
        });
        mOctaveDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOctave(-1);
                mOctaveDisplay.setText(Integer.parseInt(mOctaveDisplay.getText().toString())-1 + "");
            }
        });

    }

    /*init for Pd audio and patches*/
    private void initPd() throws IOException {
        int sampleRate = AudioParameters.suggestSampleRate();
        PdAudio.initAudio(sampleRate, 0, 2, 1, true);
    }

    /*----Helper Functions----*/
    private void createRack() throws IOException {
        Oscillator newMidiModule = new Oscillator(this);
        mModuleContainer.addView(newMidiModule, mModuleContainer.getChildCount());
        mPdModules.add(newMidiModule);

        Filter newFilter = new Filter(this);
        mModuleContainer.addView(newFilter, mModuleContainer.getChildCount());
        mPdModules.add(newFilter);

        Envelope newEnvelope = new Envelope(this);
        mModuleContainer.addView(newEnvelope, mModuleContainer.getChildCount());
        mPdModules.add(newEnvelope);

        Amplifier newAmplifier = new Amplifier(this);
        mModuleContainer.addView(newAmplifier, mModuleContainer.getChildCount());
        mPdModules.add(newAmplifier);
    }

    private void updateOctave(int offSet) {
        mOctave += offSet;
        for (Note note : mNotes) {
            note.setOctave(note.getOctave() + offSet);
        }
    }

    private void hideNotificationBar() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    /*hides notification bar on add module popup*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNotificationBar();
        }
    }

    /*----Activity Lifecycle----*/
    @Override
    protected void onStart() {
        super.onStart();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onStop() {
        PdAudio.stopAudio();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        PdAudio.release();
        PdBase.release();
        super.onDestroy();
    }

}

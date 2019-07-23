package com.example.pocketmodular.classes;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import org.puredata.core.PdBase;

public class Note implements View.OnTouchListener {
    private int position;
    private int octave;
    private int velocity;

    public Note(int position, int octave) {
        this.position = position;
        this.octave = octave;
        velocity = 100;
    }

    public void setOctave(int octave) {
        this.octave = octave;
    }

    public int getOctave() {
        return octave;
    }

    public void setVelocity(int velocity) {
        this.velocity = (velocity > 100) ? 100 : (velocity < 0) ? 0 : velocity;
    }

    public int getVelocity() {
        return velocity;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /*ui*/
        ImageButton noteVector = v.findViewById(v.getId());

        /*onTouch*/
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                noteVector.setColorFilter(Color.argb(155, 255, 0, 0));
                PdBase.sendNoteOn(0, position + (octave * 12), velocity);
                return true;
            case MotionEvent.ACTION_UP:
                noteVector.clearColorFilter();
                return true;
        }
        return false;
    }

}


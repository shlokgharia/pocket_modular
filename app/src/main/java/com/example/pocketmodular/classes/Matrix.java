package com.example.pocketmodular.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.pocketmodular.MyApplication;
import com.example.pocketmodular.R;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import org.puredata.core.PdBase;

import java.util.ArrayList;
import java.util.List;

public class Matrix extends FrameLayout {
    /*vars*/
    private MyApplication mApplication;
    private Boolean isCollapsed;

    /*ui*/
    private FrameLayout mMatrixName;
    private LinearLayout mMatrixControls;
    private LinearLayout mMatrixRoutingLayout;
    private List<Routing> mRoutedModules;
    private List<Routing> mRoutedMidi;

    public Matrix(Context context) {
        super(context);
        /*vars*/
        mApplication = ((MyApplication) context.getApplicationContext());
        mRoutedModules = new ArrayList<>();
        mRoutedMidi = new ArrayList<>();
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_matrix, this);
        mMatrixName = findViewById(R.id.matrixNameFrameLayout);
        mMatrixControls = findViewById(R.id.matrixControlsLinearLayout);
        mMatrixRoutingLayout = findViewById(R.id.matrixRoutingLinearLayout);

        mMatrixName.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mMatrixName.getLayoutParams().height));
        mMatrixControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), mMatrixControls.getLayoutParams().height));

        /*OnClick*/
        // adjusts width upon clicking naming bar
        mMatrixName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed) ? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mMatrixControls.getHeight());
                mMatrixControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        /*Creates modules and midi routing layouts in matrix*/
        for (int moduleQuantity = 1; moduleQuantity <= 24; moduleQuantity++) {
            Routing newRoutedModule = new Routing(context, moduleQuantity, false);
            mRoutedModules.add(newRoutedModule);
            mMatrixRoutingLayout.addView(newRoutedModule, mMatrixRoutingLayout.getChildCount());
        }

        for (int midiQuantity = 1; midiQuantity <= 8; midiQuantity++) {
            Routing newRoutedMidi = new Routing(context, midiQuantity, true);
            mRoutedMidi.add(newRoutedMidi);
            mMatrixRoutingLayout.addView(newRoutedMidi, mMatrixRoutingLayout.getChildCount());
        }

        /*init osc 1 to master and osc 1 pitch to midi 1*/
        mRoutedModules.get(0).mSources.setSelection(1);
        mRoutedModules.get(0).mDestinations.setSelection(31);
        mRoutedMidi.get(0).mSources.setSelection(1);
        mRoutedMidi.get(0).mDestinations.setSelection(1);
        PdBase.sendFloat("toggle_mod_matrix_path_IN_1", 1.0f);
        PdBase.sendFloat("toggle_mod_matrix_path_IN_31", 1.0f);
        PdBase.sendFloat("toggle_midi_mod_matrix_path_1", 1.0f);
        PdBase.sendFloat("toggle_midi_mod_matrix_path_OUT_1", 1.0f);

    }

     private class Routing extends LinearLayout{
        private Spinner mSources;
        private Spinner mDestinations;
        private RangeSeekBar mDepthSeekBar;
        private String modMatrixTypeSource;
        private String modMatrixTypeDestination;

         public Routing(Context context, final int modMatrixChannel, final boolean isMidi) {
             super(context);
             /*ui*/
             LayoutInflater.from(context).inflate(R.layout.layout_routing, this);
             mSources = findViewById(R.id.moduleSourcesSpinner);
             mDestinations = findViewById(R.id.moduleDestinationsSpinner);
             mDepthSeekBar = findViewById(R.id.depthSeekBar);

             /*depth init*/
             final int NORMALIZE_DEPTH = 100;
             mDepthSeekBar.setIndicatorTextDecimalFormat("0.0");
             mDepthSeekBar.setProgress(100.0f);
             PdBase.sendFloat("channel_DEPTH_" + modMatrixChannel, 1.0f);

             /*spinner dropdown look*/
             ArrayAdapter<CharSequence> sourcesAdapter;
             ArrayAdapter<CharSequence> destinationsAdapter;

             if (!isMidi) {
                 modMatrixTypeSource = "toggle_mod_matrix_path_IN_";
                 modMatrixTypeDestination = "toggle_mod_matrix_path_OUT_";
                 sourcesAdapter = ArrayAdapter.createFromResource(context, R.array.sources, android.R.layout.simple_spinner_item);
                 destinationsAdapter = ArrayAdapter.createFromResource(context, R.array.destinations, android.R.layout.simple_spinner_item);
             } else {
                 mDepthSeekBar.setVisibility(INVISIBLE);
                 modMatrixTypeSource = "toggle_midi_mod_matrix_path_";
                 modMatrixTypeDestination = "toggle_midi_mod_matrix_path_OUT_";
                 sourcesAdapter = ArrayAdapter.createFromResource(context, R.array.midiSources, android.R.layout.simple_spinner_item);
                 destinationsAdapter = ArrayAdapter.createFromResource(context, R.array.midiDestinations, android.R.layout.simple_spinner_item);
             }

             sourcesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             destinationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

             mSources.setAdapter(sourcesAdapter);
             mDestinations.setAdapter(destinationsAdapter);

             /*OnClick*/
             mSources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     PdBase.sendFloat(modMatrixTypeSource + modMatrixChannel, position);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });

             mDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     PdBase.sendFloat(modMatrixTypeDestination + modMatrixChannel, position);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });

             mDepthSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
                 @Override
                 public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                     PdBase.sendFloat("channel_DEPTH_" + modMatrixChannel, leftValue/NORMALIZE_DEPTH);
                 }

                 @Override
                 public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                 }

                 @Override
                 public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                 }
             });
         }
    }

}

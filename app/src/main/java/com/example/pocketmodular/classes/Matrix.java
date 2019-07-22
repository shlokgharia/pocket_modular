package com.example.pocketmodular.classes;

import android.content.Context;
import android.util.Log;
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

    public Matrix(Context context) {
        super(context);
        /*vars*/
        mApplication = ((MyApplication) context.getApplicationContext());
        mRoutedModules = new ArrayList<>();
        isCollapsed = false;

        /*ui*/
        LayoutInflater.from(context).inflate(R.layout.layout_matrix, this);
        mMatrixName = findViewById(R.id.matrixNameFrameLayout);
        mMatrixControls = findViewById(R.id.matrixControlsLinearLayout);
        mMatrixRoutingLayout = findViewById(R.id.matrixRoutingLinearLayout);

        mMatrixName.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleNameWidth(), mMatrixName.getLayoutParams().height));
        mMatrixControls.setLayoutParams(new LinearLayout.LayoutParams(mApplication.getModuleControlsWidth(), mMatrixControls.getLayoutParams().height));

        /*OnClick*/
        mMatrixName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int moduleSize = (isCollapsed) ? mApplication.getModuleControlsWidth() : 0;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(moduleSize, mMatrixControls.getHeight());
                mMatrixControls.setLayoutParams(params);
                isCollapsed = !isCollapsed;
            }
        });

        /*Creates*/
        for (int moduleID = 1; moduleID <= 20; moduleID++) {
            Routing newRoutedModule = new Routing(context, moduleID, false);
            mRoutedModules.add(newRoutedModule);
            mMatrixRoutingLayout.addView(newRoutedModule, mMatrixRoutingLayout.getChildCount());
        }

        for (int midiID = 1; midiID <= 8; midiID++) {
            Routing newRoutedModule = new Routing(context, midiID, true);
            mRoutedModules.add(newRoutedModule);
            mMatrixRoutingLayout.addView(newRoutedModule, mMatrixRoutingLayout.getChildCount());
        }

    }

     private class Routing extends LinearLayout{
        private Spinner mSources;
        private Spinner mDestinations;
        private RangeSeekBar mDepthSeekBar;
        private String modMatrixType;

         public Routing(Context context, final int modMatrixChannel, boolean isMidi) {
             super(context);
             /*ui*/
             LayoutInflater.from(context).inflate(R.layout.layou_routing, this);
             mSources = findViewById(R.id.moduleSourcesSpinner);
             mDestinations = findViewById(R.id.moduleDestinationsSpinner);
             mDepthSeekBar = findViewById(R.id.depthSeekBar);
             mDepthSeekBar.setIndicatorTextDecimalFormat("0.0");

             /*spinner dropdown look*/
             ArrayAdapter<CharSequence> sourcesAdapter;
             ArrayAdapter<CharSequence> destinationsAdapter;

             if (!isMidi) {
                 modMatrixType = "toggle_mod_matrix_path";
                 sourcesAdapter = ArrayAdapter.createFromResource(context, R.array.sources, android.R.layout.simple_spinner_item);
                 destinationsAdapter = ArrayAdapter.createFromResource(context, R.array.destinations, android.R.layout.simple_spinner_item);
             } else {
                 modMatrixType = "toggle_midi_mod_matrix_path";
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
                     PdBase.sendFloat(modMatrixType + "_IN_" + modMatrixChannel, position);
                     Log.d("SOURCE", "onItemSelected: " + position);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });

             mDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     PdBase.sendFloat(modMatrixType + "_OUT_" + modMatrixChannel, position);
                     Log.d("DESTINATIONS", "onItemSelected: " + position);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });

             mDepthSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
                 @Override
                 public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                     //PdBase.sendFloat("" + moduleID, leftValue);
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

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

import java.io.IOException;
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

    public Matrix(Context context) throws IOException {
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

        /*Creates 24 routing containers*/
        for (int i = 0; i < 24; i++) {
            Routing newRoutedModule = new Routing(context);
            mRoutedModules.add(newRoutedModule);
            mMatrixRoutingLayout.addView(newRoutedModule, mMatrixRoutingLayout.getChildCount());
        }

    }

     private class Routing extends LinearLayout{
        private Spinner mSources;
        private Spinner mDestinations;
        private RangeSeekBar mDepthSeekBar;

         public Routing(Context context) {
             super(context);
             /*ui*/
             LayoutInflater.from(context).inflate(R.layout.layout_routing, this);
             mSources = findViewById(R.id.moduleSourcesSpinner);
             mDestinations = findViewById(R.id.moduleDestinationsSpinner);
             mDepthSeekBar = findViewById(R.id.depthSeekBar);
             mDepthSeekBar.setIndicatorTextDecimalFormat("0.0");

             /*spinner dropdown look*/
             ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.sources, android.R.layout.simple_spinner_item);
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             mSources.setAdapter(adapter);
             mDestinations.setAdapter(adapter);

             /*OnClick*/
             mSources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     String source = parent.getItemAtPosition(position).toString();
                     //PdBase.sendFloat(source, 1.0f);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });

             mDestinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     String destination = parent.getItemAtPosition(position).toString();
                     //PdBase.sendFloat(destination, 1.0f);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parent) {

                 }
             });

             mDepthSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
                 @Override
                 public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

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

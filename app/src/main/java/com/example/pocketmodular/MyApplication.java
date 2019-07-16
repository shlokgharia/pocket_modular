package com.example.pocketmodular;

import android.app.Application;

public class MyApplication extends Application {
    int screenWidth;
    int moduleControlsWidth;
    int moduleNameWidth;

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
        this.moduleControlsWidth =  (int) Math.round(screenWidth/2.5);
        this.moduleNameWidth = moduleControlsWidth/8;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getModuleControlsWidth() {
        return moduleControlsWidth;
    }

    public int getModuleNameWidth() {
        return moduleNameWidth;
    }

}

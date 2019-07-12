package com.example.pocketmodular.classes;

import android.content.Context;

import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdModule {
    private List<Integer> handles;
    private PdModule input;
    private PdModule output;

    public PdModule(){
        handles = new ArrayList<>();
    }

    public void openPatch(Context context, String module) throws IOException {
        File dir = context.getFilesDir();
        File pdPatch = new File(dir, module + ".pd");
        int pdZip = context.getResources().getIdentifier(module, "raw", context.getPackageName());
        IoUtils.extractZipResource(context.getResources().openRawResource(pdZip), dir, true);
        handles.add(PdBase.openPatch(pdPatch.getAbsolutePath()));
    }

    public void closePatch() {
        for (int handle : handles) {
            PdBase.closePatch(handle);
        }
    }

    public void setInput(PdModule input) {
        this.input = input;
    }

    public PdModule getInput() {
        return input;
    }

    public void setOutput(PdModule output) {
        this.output = output;
    }

    public PdModule getOutput() {
        return output;
    }

}

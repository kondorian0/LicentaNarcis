package com.narcis.neamtiu.licentanarcis.util;

import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioFileHelper {
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static String saveAudio() {
        int count = 0;
        String getPath = null;

        File sdDirectory = Environment.getStorageDirectory();
        File subDirectory = new File(sdDirectory.toString() + "/Music/Record");

        if(subDirectory.exists()) {
            File[] existing = subDirectory.listFiles();

            for(File file : existing) {
                if(file.getName().endsWith(".3gp") || file.getName().endsWith(".mp3")) {
                    count++;
                }
            }
        } else {
            subDirectory.mkdir();
        }

        if(subDirectory.exists()) {
            File audio =  new File(subDirectory, "/record_" + (count + 1) + ".3gp" );
            FileOutputStream fileOutputStream;

            getPath = audio.getPath();

            try {
                fileOutputStream = new FileOutputStream(audio);
                fileOutputStream.flush();
                fileOutputStream.close();

            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
        return getPath;
    }
}

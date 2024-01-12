package com.kanha.statussaver.Util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MediaFile {

    private static final String TAG = "MediaFile";


    public static int copy(File src, String fileName) throws IOException {
        File dst = new File("/storage/emulated/0/Pictures/StatusSaver/" + fileName);
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                return 1;
            } catch (Exception e) {
                Log.e(TAG, "copy: Error", e);
                return -1;
            }
        }
    }

}

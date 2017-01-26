package xyz.hans.alpha.utils;

import android.util.Log;

import java.io.File;

/**
 * Created by Hans on 17/1/26.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 创建目录
     * @param dir
     */
    public static void mkdirs(File dir) {
        if (!dir.exists()) {
            Log.d(TAG, "创建目录成功:" + dir);
            dir.mkdirs();
        }
    }
}

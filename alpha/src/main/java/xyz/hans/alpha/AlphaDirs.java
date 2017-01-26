package xyz.hans.alpha;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import xyz.hans.alpha.utils.FileUtils;

/**
 * Created by Hans on 17/1/26.
 */

public class AlphaDirs {
    private static final String TAG = "AlphaDirs";
    private static AlphaDirs sInstance;
    private Context mContext;
    /**
     * /data/data/{package_name}/files/alpha
     */
    private static String ROOT_PATH;
    private File mRootFolder;
    /**
     * /data/data/{package_name}/files/alpha (目前不是按照这样的路径,是在闪存储的根目录下的
     */
    private static String APK_PATH;
    private File mApkFolder;
    /**
     * /data/data/{package_name}/files/alpha/odex
     */
    private static String DEX_PATH;
    private File mDexFolder;
    /**
     * /data/data/{package_name}/files/alpha/odex
     */
    private static String ODEX_PATH;
    private File mOdexFolder;

    private AlphaDirs(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        ROOT_PATH = mContext.getCacheDir().getAbsolutePath() + File.separator + "alpha";
        Log.d(TAG, "root file path:" + ROOT_PATH);
        APK_PATH = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "alpha"
                + File.separator + "apk";
        Log.d(TAG, "apk path:" + APK_PATH);
        ODEX_PATH = ROOT_PATH + File.separator + "odex";
        Log.d(TAG, "odex path:" + ODEX_PATH);
        DEX_PATH = ROOT_PATH + File.separator + "dex";
        Log.d(TAG, "dex path:" + DEX_PATH);
        mRootFolder = new File(ROOT_PATH);
        FileUtils.mkdirs(mRootFolder);
        mOdexFolder = new File(ODEX_PATH);
        FileUtils.mkdirs(mOdexFolder);
        mDexFolder = new File(DEX_PATH);
        FileUtils.mkdirs(mDexFolder);
        mApkFolder = new File(APK_PATH);
        FileUtils.mkdirs(mApkFolder);
    }

    public static AlphaDirs getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AlphaDirs.class) {
                if (sInstance == null)
                    sInstance = new AlphaDirs(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    public String getOdexPath() {
        return ODEX_PATH;
    }

    public String getRootFilePath() {
        return ROOT_PATH;
    }

    public String getDexPath() {
        return DEX_PATH;
    }

    public File getDexFolder() {
        return mDexFolder;
    }

    public String getApkPath() {
        return APK_PATH;
    }

    public File getOdexFolder() {
        return mOdexFolder;
    }

    public File getApkFolder() {
        return mApkFolder;
    }
}

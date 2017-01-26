package xyz.hans.alpha;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;

import xyz.hans.alpha.core.DexElementsMerger;
import xyz.hans.alpha.core.DexReleaser;
import xyz.hans.alpha.reflect.FieldUtils;
import xyz.hans.alpha.utils.Check;

/**
 * Created by Hans on 17/1/26.
 */

public class Alpha {
    private static Alpha sAlpha;
    private static final String TAG = "Alpha";
    private AlphaDirs mAlphaDirs;


    public static Alpha getInstance() {
        if (sAlpha == null) {
            synchronized (Alpha.class) {
                if (sAlpha == null) {
                    sAlpha = new Alpha();
                }
            }
        }
        return sAlpha;
    }

    public void fix(Context context) {
        ClassLoader cl = Alpha.class.getClassLoader();
        init(context);
        /**
         * 1.根据Apk解压dex出来,并且拷贝到指定目录
         * 2.分别使用DexClassLoader将所有的dex加载一遍
         * 3.将加载过Dex的DexClassLoader中Elements数组的内容合并为一个数组
         * 4.通过反射将当前的PathClassLoader的Elements数组替换,实现代码级别的修复
         */
        File[] apks = mAlphaDirs.getApkFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".apk");
            }
        });
        if (Check.isEmpty(apks)) {
            Log.d(TAG, "没有Apk文件,不进行修复");
            return;
        }
        File apk = apks[0];//获取需要修复的apk
        File dexFolder = mAlphaDirs.getDexFolder();
        File odexFolder = mAlphaDirs.getOdexFolder();
        //第一步:解压Apk并且输出到指定目录
        DexReleaser.releaseDexes(apk, mAlphaDirs.getDexFolder());
        Object dexElements = null;
        try {
            //第二步,第三步:加载并且合并
            dexElements = DexElementsMerger.merge(dexFolder, odexFolder);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.d(TAG, "合并DexElements发生异常,合并失败");
            return;
        }
        if (dexElements == null) {
            Log.d(TAG, "DexElements为空");
            return;
        }
        //第四步:反射注入到当前的ClassLoader
        Object dexPathList = null;
        try {
            //获取当前的PathClassLoader(即加载了Alpha这个类的类加载器
            dexPathList = DexElementsMerger.getPathList(Alpha.class.getClassLoader());
            if (dexPathList != null)
                FieldUtils.writeField(dexPathList, "dexElements", dexElements);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void init(Context context) {
        mAlphaDirs = AlphaDirs.getInstance(context);
    }
}

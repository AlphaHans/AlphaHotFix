package xyz.hans.alpha.core;

import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;
import xyz.hans.alpha.reflect.FieldUtils;
import xyz.hans.alpha.utils.Check;

/**
 * Created by Hans on 17/1/26.
 */

public class DexElementsMerger {
    private static final String TAG = "DexElementsMerger";

    public static Object merge(File dexFolder, File odexFolder) throws IllegalAccessException {
        File[] dexFiles = dexFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".dex");
            }
        });
        if (Check.isEmpty(dexFiles)) {
            Log.d(TAG, "dex文件为空,合并dex失败");
            return null;
        }
        Object originalElements = getElements(getPathList(DexElementsMerger.getPathClassLoader()));
        Class<?> localClass = originalElements.getClass().getComponentType();
        int length = dexFiles.length;
        Object dexElements = Array.newInstance(localClass, length);
        Log.d(TAG, "开始合并DexElements");
        for (int i = 0; i < dexFiles.length; i++) {
            File dexFile = dexFiles[i];
            Log.d(TAG, String.format("正在合并第%s个dex", i));
            ClassLoader classLoader = loadDexWithClassLoader(dexFile, odexFolder);
            Object pathList = getPathList(classLoader);
            Object elements = getElements(pathList);

            int _length = Array.getLength(elements);
            if (_length == 0) {
                continue;
            }
            //全部放入
            for (int k = 0; k < _length; k++) {
                Array.set(dexElements, i, Array.get(elements, k));
            }
        }
        Log.d(TAG, "合并DexElements完毕");
        return dexElements;
    }

    private static ClassLoader loadDexWithClassLoader(File dexFile, File odexFolder) {
        DexClassLoader dexClassLoader = new DexClassLoader(dexFile.getPath(),
                odexFolder.getAbsolutePath(),
                null,
                getPathClassLoader()
        );
        return dexClassLoader;
    }

    public static Object getPathList(ClassLoader dexClassLoader) throws IllegalAccessException {
        return FieldUtils.readField(dexClassLoader, "pathList");
    }

    private static Object getElements(Object pathList) throws IllegalAccessException {
        return FieldUtils.readField(pathList, "dexElements");
    }

    private static PathClassLoader getPathClassLoader() {
        PathClassLoader pathClassLoader = (PathClassLoader) DexElementsMerger.class.getClassLoader();
        return pathClassLoader;
    }
}

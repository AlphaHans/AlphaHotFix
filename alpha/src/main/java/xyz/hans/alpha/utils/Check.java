package xyz.hans.alpha.utils;


/**
 * Created by Hans on 17/1/26.
 */

public class Check {

    public static <T> boolean isEmpty(T[] t) {
        return t == null || t.length == 0;
    }
}

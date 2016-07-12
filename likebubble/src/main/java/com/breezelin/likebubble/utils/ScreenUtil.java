package com.breezelin.likebubble.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

public class ScreenUtil {

    public static Bitmap takeScreenshot(View view) {
        Bitmap screenshot = null;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache(false);
        Bitmap cache = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;// Activity 的顶端， 就是状态栏的高度。
        screenshot = Bitmap.createBitmap(cache, 0, statusHeight, cache.getWidth(), cache.getHeight() - statusHeight);
        view.destroyDrawingCache();
        return screenshot;
    }

    /**
     * 状态栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight(View view) {
        // 获取状态栏高度
        Rect frame = new Rect();
        view.getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;// Activity 的顶端， 就是状态栏的高度。
        return statusBarHeight;
    }


    public static int getScreenWidth(Context context) {
        int width = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        return width;
    }

    public static int getScreenHeight(Context context) {
        int height = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        height = dm.heightPixels;
        return height;
    }

    public static int getScreenDensity(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int density = dm.densityDpi;
        return density;
    }

    /**
     * px (pixels)像素 – 是像素，就是屏幕上实际的像素点单位。
     * dip或dp (device independent pixels)设备独立像素， 与设备屏幕有关。
     * dpi(dot per inch):屏幕像素密度，每英寸多少像素
     * <p/>
     * 换算公式：px = dip * (dpi / 160)
     * DisplayMetrics中的density = dpi / 160
     * DisplayMetrics中的densityDpi就是dpi
     */
    public static float getDensity(Context context) {
//        DisplayMetrics metrics = StarZoneApplication.context.getResources().getDisplayMetrics();
//        return metrics.density;

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 根据手机的分辨率将dp/dip(设备的独立像素)转化为px(像素)值
     */
    public static int dip2px(float dpValue, Context context) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp/dip(设备的独立像素)
     */
    public static int px2dip(float pxValue, Context context) {
        return (int) (pxValue / getDensity(context) + 0.5f);
    }

    //获取屏幕的大小
    public static int getScreenW(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕的大小
    public static int getScreenH(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    // 是否有虚拟键盘
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w("TAG", e);
        }

        return hasNavigationBar;

    }

    //虚拟键盘的高度
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

}

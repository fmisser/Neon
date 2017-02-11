package com.fmisser.neon.common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Date: 2017/2/3.
 * Author: fmisser
 */

public class Utils {

    private static final String MANUFACTURER_XIAOMI = "Xiaomi";
    private static final String SYS_PROP_MIUI_VERSION = "ro.miui.ui.version.name";


    private Utils() {}

    /**
     * 通过资源获取状态拦高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getNavigationBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 获取DisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    /**
     * transform dp to pixels
     */
    public static float dp2px(Context context, float dpVal)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * transform sp to pixels
     */
    public static float sp2px(Context context, float spVal)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());

    }

    /**
     * 4.4+ 设置状态栏透明, 可以用来做沉浸式或者配合android:fitsSystemWindows来改变状态栏
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setTranslucentStatus(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(layoutParams);
    }

    /**
     * 5.0+ 设置状态栏颜色
     * 不能和FLAG_TRANSLUCENT_STATUS同时设置
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarColor(Activity activity, int color) {
        Window window = activity.getWindow();
        window.setStatusBarColor(color);
    }

    /**
     * 等同于style里设置android:windowLightStatusBar
     * 不能和FLAG_TRANSLUCENT_STATUS同时设置
     * @param lightMode 为true时文字为深色,适合亮色状态栏
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void setLightStatusBar(Activity activity, boolean lightMode) {
        Window window = activity.getWindow();
        if (lightMode) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 小米MIUI6+ 设置状态栏内容风格 ,
     * 参考小米文档: @see http://dev.xiaomi.com/doc/p=4769/index.html
     * @param darkMode 为true时文字为深色,适合亮色状态栏
     */
    public static void setMIUIStatusBarDarkMode(Activity activity, boolean darkMode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkMode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 魅族flyme4+设置状态栏内容风格 ,
     * 参考魅族文档: @see http://open-wiki.flyme.cn/index.php?title=Flyme%E7%B3%BB%E7%BB%9FAPI
     * 和 @see http://open-wiki.flyme.cn/index.php?title=%E7%8A%B6%E6%80%81%E6%A0%8F%E5%8F%98%E8%89%B2
     * @param isFontColorDark 为true时文字为深色,适合亮色状态栏
     */
    public static void setFLYMEStatusBarLightMode(Activity activity, boolean isFontColorDark) {
        Window window = activity.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isFontColorDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取小米系统的版本号,参考小米提供的方法, @see http://dev.xiaomi.com/doc/?p=254
     * @return 小米系统版本,返回null表示非小米系统
     */
    public static String getMIUIVersion() {
        if (Build.MANUFACTURER.equals(MANUFACTURER_XIAOMI)) {
            return systemPropertiesGet(SYS_PROP_MIUI_VERSION, "", String.class);
        }
        return null;
    }

    /**
     * 读取系统属性值
     */
    @SuppressWarnings("unchecked")
    private static <T> T systemPropertiesGet(T prop, T defVal, Class<T> tClass) {
        try {
            Class<?> propClass = Class.forName("android.os.SystemProperties");
            Method method = propClass.getMethod("get", tClass, tClass);
            return (T) method.invoke(propClass.newInstance(), prop, defVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defVal;
    }

    /**
     * BottomNavigationView 当item大于3个的时候会使用 shift mode 模式, 此函数用来禁用该模式.
     * 如果混淆后无法工作,可以在混淆规则中添加:
     * -keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
     boolean mShiftingMode;
     }
     */
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i=0; i< menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

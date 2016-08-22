package com.gq.mylib.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Lenovo on 2016/3/21.
 */
public class AppUtil {

    public AppUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本号]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null || cm.getActiveNetworkInfo() == null)
            return false;

        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    //获取手机唯一标识码
    public static String getPhoneImei(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();

        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        LogUtil.i(uniqueId, "UUID");
        return uniqueId;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    /**
     * 获取视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     * @param kind      参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                           int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        //System.out.println("w"+bitmap.getWidth());
        //System.out.println("h"+bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 打开视频文件
     *
     * @param context
     * @param file    视频文件
     */
    public static void intentVideo(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "video/*";
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }

    /**
     * 获取系统所有APP应用
     *
     * @param context
     */
   /* public static ArrayList<AppInfo> getAllApp(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        for (ResolveInfo info : apps) {
            AppInfo appInfo = new AppInfo();

            appInfo.setAppLable(info.loadLabel(manager) + "");
            appInfo.setAppIcon(info.loadIcon(manager));
            appInfo.setAppPackage(info.activityInfo.packageName);
            appInfo.setAppClass(info.activityInfo.name);
            appList.add(appInfo);
            System.out.println("info.activityInfo.packageName=" + info.activityInfo.packageName);
            System.out.println("info.activityInfo.name=" + info.activityInfo.name);
        }

        return appList;
    }*/

    /**
     * 获取用户安装的APP应用
     *
     * @param context
     */
   /* public static ArrayList<AppInfo> getUserApp(Context context) {
        PackageManager manager = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        // 将获取到的APP的信息按名字进行排序
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
        for (ResolveInfo info : apps) {
            AppInfo appInfo = new AppInfo();
            ApplicationInfo ainfo = info.activityInfo.applicationInfo;
            if ((ainfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
                appInfo.setAppLable(info.loadLabel(manager) + "");
                appInfo.setAppIcon(info.loadIcon(manager));
                appInfo.setAppPackage(info.activityInfo.packageName);
                appInfo.setAppClass(info.activityInfo.name);
                appList.add(appInfo);
            }
        }

        return appList;
    }*/

    /**
     * 根据包名和Activity启动类查询应用信息
     *
     * @param cls
     * @param pkg
     * @return
     */
   /* public static AppInfo getAppByClsPkg(Context context, String pkg, String cls) {
        AppInfo appInfo = new AppInfo();

        PackageManager pm = context.getPackageManager();
        Drawable icon;
        CharSequence label = "";
        ComponentName comp = new ComponentName(pkg, cls);
        try {
            ActivityInfo info = null;
            info = pm.getActivityInfo(comp, 0);
            icon = pm.getApplicationIcon(info.applicationInfo);
            label = pm.getApplicationLabel(pm.getApplicationInfo(pkg, 0));
        } catch (PackageManager.NameNotFoundException e) {
            icon = pm.getDefaultActivityIcon();
        }
        appInfo.setAppClass(cls);
        appInfo.setAppIcon(icon);
        appInfo.setAppLable(label + "");
        appInfo.setAppPackage(pkg);

        return appInfo;
    }*/

    /**
     * 跳转到WIFI设置
     *
     * @param context
     */
    public static void intentWifiSetting(Context context) {
        if (android.os.Build.VERSION.SDK_INT > 10) {
            // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
            context.startActivity(new Intent(
                    android.provider.Settings.ACTION_SETTINGS));
        } else {
            context.startActivity(new Intent(
                    android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    /**
     * WIFI网络开关
     */
    public static void toggleWiFi(Context context, boolean enabled) {
        WifiManager wm = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wm.setWifiEnabled(enabled);
    }

    /**
     * 移动网络开关
     */
    public static void toggleMobileData(Context context, boolean enabled) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method setMobileDataEnabledMethod = null; // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
                    "setMobileDataEnabled", Boolean.TYPE);
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod.setAccessible(true);
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * GPS开关 当前若关则打开 当前若开则关闭
     */
    public static void toggleGPS(Context context) {
        Intent gpsIntent = new Intent();
        gpsIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
        gpsIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调节系统音量
     *
     * @param context
     */
    public static void holdSystemAudio(Context context) {
        AudioManager audiomanage = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        // 获取系统最大音量
        // int maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取当前音量
        // int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_RING);
        // 设置音量
        // audiomanage.setStreamVolume(AudioManager.STREAM_SYSTEM, currentVolume, AudioManager.FLAG_PLAY_SOUND);

        // 调节音量
        // ADJUST_RAISE 增大音量，与音量键功能相同
        // ADJUST_LOWER 降低音量
        audiomanage.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
                AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

    }

    /**
     * 设置亮度（每30递增）
     *
     * @param
     * @param
     */
    public static void setBrightness(Activity activity) {
        ContentResolver resolver = activity.getContentResolver();
        Uri uri = android.provider.Settings.System
                .getUriFor("screen_brightness");
        int nowScreenBri = getScreenBrightness(activity);
        nowScreenBri = nowScreenBri <= 225 ? nowScreenBri + 30 : 30;
        System.out.println("nowScreenBri==" + nowScreenBri);
        android.provider.Settings.System.putInt(resolver, "screen_brightness",
                nowScreenBri);
        resolver.notifyChange(uri, null);
    }

    /**
     * 获取屏幕的亮度
     *
     * @param activity
     * @return
     */
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 跳转到系统设置
     *
     * @param context
     */
    public static void intentSetting(Context context) {
        String pkg = "com.android.settings";
        String cls = "com.android.settings.Settings";

        ComponentName component = new ComponentName(pkg, cls);
        Intent intent = new Intent();
        intent.setComponent(component);

        context.startActivity(intent);
    }

    /**
     * 获取文件夹下所有文件
     *
     * @param path
     * @return
     */
    public static ArrayList<File> getFilesArray(String path) {
        File file = new File(path);
        File files[] = file.listFiles();
        ArrayList<File> listFile = new ArrayList<File>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    listFile.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    listFile.addAll(getFilesArray(files[i].toString()));
                }
            }
        }
        return listFile;
    }
}

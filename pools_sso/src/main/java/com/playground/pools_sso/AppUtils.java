package com.playground.pools_sso;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import kotlin.jvm.internal.Intrinsics;

public class AppUtils {

    public static String POOL_DASHBOARD_PACKAGE_NAME = "com.pools.dashboard";

    public static ArrayList<String> listAppPools = new ArrayList() {{  // child applications
        add("com.pools.lottery");
        add("com.pools.vote");
        add("com.pools.slide");
        add("com.pools.keyboard");
        add("com.pools.walk");
        add("com.pools.dex");
        add("com.pools.call");
        add("com.pools.dashboard");
        add("com.wallet.pools");
        add("friendify.playground");
        add("com.pools.scratch");

    }};

    public static Boolean checkLogin(Context context) {
        boolean isLogin = false;
        try {
            Uri uri = Uri.parse((String) "content://com.pools.dashboard");
            Bundle bundle = Objects.requireNonNull(context).getContentResolver().call(uri, "getSharedPreference", "getTokenLogin", null);
            Intrinsics.checkNotNull((Object) bundle);
            String valueAccessToken = bundle.getString("accessToken");
            String valueRefreshToken = bundle.getString("refreshToken");
            if (valueAccessToken != null && !valueAccessToken.isEmpty() && valueRefreshToken != null && !valueRefreshToken.isEmpty()) {
                isLogin = true;
            }
        } catch (Exception ex) {
            Log.i("AppUtils", "getTokenLogin error: " + ex.getMessage());

        }

        return isLogin;
    }


    public static Map<String, String> getTokenLogin(Context context) {
        Map<String, String> tokens = new HashMap<>();
        try {
            Uri uri = Uri.parse("content://com.pools.dashboard");
            Bundle bundle = Objects.requireNonNull(context).getContentResolver().call(uri, "getSharedPreference", "getTokenLogin", null);

            if (bundle != null) {
                String valueAccessToken = bundle.getString("accessToken");
                String valueRefreshToken = bundle.getString("refreshToken");

                if (valueAccessToken != null && !valueAccessToken.isEmpty() && valueRefreshToken != null && !valueRefreshToken.isEmpty()) {
                    tokens.put("accessToken", valueAccessToken);
                    tokens.put("refreshToken", valueRefreshToken);
                }
            }
        } catch (Exception ex) {
            Log.i("AppUtils", "getTokenLogin error: " + ex.getMessage());
        }
        return tokens;
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true; // App is installed
        } catch (PackageManager.NameNotFoundException e) {
            return false; // App is not installed
        }
    }

    public static void openAppPassDataIsPackageName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.putExtra("fromApp", context.getPackageName());
            context.startActivity(intent);
        }
    }

    public static void openApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(intent);
        }
    }

    public static void redirectToPlayStore(Activity activity, String appPackageName) {
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}

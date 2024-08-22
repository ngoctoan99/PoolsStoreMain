package com.playground.pools_sso;

import static com.playground.pools_sso.AppUtils.POOL_DASHBOARD_PACKAGE_NAME;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
//import com.facebook.react.module.annotations.ReactModule;

import java.util.Map;
import java.util.Objects;

//@ReactModule(name = PoolsSSOModule.NAME)
public class PoolsSSOModule extends ReactContextBaseJavaModule {


    public static ReactApplicationContext mReactContext;

    public static MyBroadcastReceiver myReceiver = new MyBroadcastReceiver();

    public static final String NAME = "PoolsSSO";

    public PoolsSSOModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    AlertDialog dialogLogin;

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public void initialize() {
        mReactContext = getReactApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getReactApplicationContext().registerReceiver(myReceiver, new IntentFilter("com.playground.broadcast.dashboard"), Context.RECEIVER_EXPORTED);
        } else {
            getReactApplicationContext().registerReceiver(myReceiver, new IntentFilter("com.playground.broadcast.dashboard"));
        }


        getReactApplicationContext().addLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {

            }

            @Override
            public void onHostPause() {

            }

            @Override
            public void onHostDestroy() {
                try {
                    mReactContext.unregisterReceiver(myReceiver);

                }catch (Exception ex){

                }
                
            }
        });


        super.initialize();

    }

    @ReactMethod()
    public void sendBroadcastPoint(String json) {
        Log.i("TESTTESTPOINT", json);
        Intent intent = new Intent();
        intent.setAction("com.playground.broadcast.dashboard");
        intent.putExtra("status", "Point");
        intent.putExtra("json", json);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setComponent(new ComponentName(POOL_DASHBOARD_PACKAGE_NAME, "com.playground.pools_sso.MyBroadcastReceiver"));
        Objects.requireNonNull(getReactApplicationContext()).sendBroadcast(intent);
    }


    @ReactMethod()
    public void sendBroadcastRefreshToken(String accessToken, String refreshToken) {
        Intent intent = new Intent();
        intent.setAction("com.playground.broadcast.dashboard");
        intent.putExtra("status", "RefreshToken");
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        for (int i = 0; i < AppUtils.listAppPools.size(); i++) {
            if (AppUtils.listAppPools.get(i).equals(getReactApplicationContext().getPackageName())) {
                continue;
            }
            intent.setComponent(new ComponentName(AppUtils.listAppPools.get(i), "com.playground.pools_sso.MyBroadcastReceiver"));
            Objects.requireNonNull(getReactApplicationContext()).sendBroadcast(intent);
        }

    }


    @ReactMethod()
    public void checkIsUserLogin(Promise promise) {

        Map<String, String> tokens = AppUtils.getTokenLogin(mReactContext);

        // Check if tokens are retrieved successfully
        WritableArray values = Arguments.createArray();

        if (!tokens.isEmpty()) {
          //  Create a writable array to hold the values
            String value1 = tokens.get("accessToken");
            String value2 = tokens.get("refreshToken");
            values.pushString(value1);
            values.pushString(value2);
            promise.resolve(values);
        }else {
            promise.reject("Error",new Exception("AccessToken"));
        }

    }

    @ReactMethod()
    public void dismissDialogLogin() {

        if (dialogLogin != null) {
            dialogLogin.dismiss();
            dialogLogin = null;
        }

    }


    @ReactMethod()
    public void loginUser(Promise promise) {

        boolean token = AppUtils.checkLogin(getReactApplicationContext());
        Log.d("PoolsSSOModule", "loginUser token: " + token);

        if (!token) {

            if (dialogLogin != null) {
                dialogLogin.dismiss();
                dialogLogin = null;
            }
            dialogLogin = new AlertDialog.Builder(getReactApplicationContext())
                    .setTitle("Login")
                    .setMessage("Please log in using the Pools Dashboard app to continue")
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                        promise.reject("Error", "Cancel login");

                    })
                    .setPositiveButton("Login", (dialog, which) -> {

                                promise.resolve("OK");
                                dialog.dismiss();

                                if (AppUtils.isAppInstalled(getReactApplicationContext(), POOL_DASHBOARD_PACKAGE_NAME)) {
                                    AppUtils.openAppPassDataIsPackageName(getReactApplicationContext(), POOL_DASHBOARD_PACKAGE_NAME);
                                } else {

                                    AppUtils.redirectToPlayStore(Objects.requireNonNull(getCurrentActivity()), POOL_DASHBOARD_PACKAGE_NAME);
                                }


                            }
                    ).create();

            dialogLogin.setCancelable(false);
            dialogLogin.setCanceledOnTouchOutside(false);
            dialogLogin.show();
        } else {
            promise.reject("Error", "Account is already logged in");
        }


    }


    public static int listenerCount = 0;

    @ReactMethod
    public void addListener(String eventName) {
        if (listenerCount == 0) {
            // Set up any upstream listeners or background tasks as necessary
        }

        listenerCount += 1;
    }

    @ReactMethod
    public void removeListeners(Integer count) {
        listenerCount -= count;
        if (listenerCount == 0) {
            // Remove upstream listeners, stop unnecessary background tasks
        }
    }


}

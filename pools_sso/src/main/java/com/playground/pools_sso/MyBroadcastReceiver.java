package com.playground.pools_sso;

import static com.playground.pools_sso.PoolsSSOModule.listenerCount;
import static com.playground.pools_sso.PoolsSSOModule.mReactContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;
import java.util.Objects;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "Pools MyBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("com.playground.broadcast.dashboard")) {
            String status = intent.getStringExtra("status");

            Log.d(TAG, "Received broadcast status: " + status);
            // Handle the message here
            if (Objects.equals(status, "Login")) {
                if (listenerCount > 0) {
                    WritableMap params = Arguments.createMap();
                    params.putString("status", "Login");
                    Map<String, String> tokens = AppUtils.getTokenLogin(mReactContext);
// Check if tokens are retrieved successfully
                    if (!tokens.isEmpty()) {
                        params.putString("accessToken", tokens.get("accessToken"));
                        params.putString("refreshToken", tokens.get("refreshToken"));
                        sendEvent(mReactContext, "EventSSO", params);
                    }

                }

            } else if (Objects.equals(status, "Logout")) {
                if (listenerCount > 0) {
                    WritableMap params = Arguments.createMap();
                    params.putString("status", "Logout");
                    sendEvent(mReactContext, "EventSSO", params);
                }

            } else if (Objects.equals(status, "RefreshToken")) {
                if (listenerCount > 0) {

                    WritableMap params = Arguments.createMap();
                    params.putString("status", "RefreshToken");
                    params.putString("accessToken", intent.getStringExtra("accessToken"));
                    params.putString("refreshToken", intent.getStringExtra("refreshToken"));
                    sendEvent(mReactContext, "EventSSO", params);
                }


            } else if (Objects.equals(status, "Profile")) {
                if (listenerCount > 0) {

                    WritableMap params = Arguments.createMap();
                    params.putString("status", "Profile");

                    sendEvent(mReactContext, "EventSSO", params);
                }


            }
        }


    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}

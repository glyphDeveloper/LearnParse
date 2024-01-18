package com.glyphpass.learnparse

import android.content.Context;
import android.util.Log
import android.provider.Settings


class GetAndroidDeviceID {


    fun getDeviceID(context : Context): String {
        val deviceId = Settings.Secure.getString(
            context.getContentResolver(),
            Settings.Secure.ANDROID_ID);
        // Log.d("TAG", "Device ID is: $deviceId")
        return deviceId
    }
}
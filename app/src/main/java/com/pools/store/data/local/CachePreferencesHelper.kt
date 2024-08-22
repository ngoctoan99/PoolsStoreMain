package com.pools.store.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

open class CachePreferencesHelper @Inject constructor(context: Context) {

    companion object {
        private const val PREF_PACKAGE_NAME = "com.wallet.oscar.preferences"
        private const val PREF_KEY_LANGUAGE_APP = "language_app"
        private const val PREF_KEY_ACCESS_TOKEN = "accessToken"
        private const val PREF_KEY_LANGUAGE = "the_language"
        private const val PREF_KEY_FCM_TOKEN = "fcm_token"
        private const val PREF_KEY_SPEECH_RATE_VOICE = "SPEECH_RATE_VOICE"
        private const val PREF_KEY_SPEECH_PITCH_VOICE = "SPEECH_PITCH_VOICE"
        private const val PREF_KEY_THE_FIRST_OPEN_APP = "the_first_open_app"
        private const val PREF_KEY_USER_INFO = "user_info"
        private const val PREF_KEY_VOICE_MODE = "voice_mode"
        private const val PREF_KEY_CHARACTER = "character"
        private const val PREF_KEY_POINT_USER = "point_user"
        private const val PREF_AVATAR_USER = "avatar_user"
        private const val PREF_NAME_USER = "name_user"
        private const val PREF_CODE_USER = "code_user"
        private const val PREF_CODE_LANGUAGE = "code_language"


    }


    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_PACKAGE_NAME, Context.MODE_PRIVATE)


    var fcmToken: String
        get() = preferences.getString(PREF_KEY_FCM_TOKEN, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_FCM_TOKEN, token).apply()


    var languageCache: Int
        get() = preferences.getInt(PREF_KEY_LANGUAGE, 0).toInt()
        set(point) = preferences.edit().putInt(PREF_KEY_LANGUAGE, point).apply()


    var languageApp: String
        get() = preferences.getString(PREF_KEY_LANGUAGE_APP, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_LANGUAGE_APP, token).apply()

    var languageCode: String
        get() = preferences.getString(PREF_CODE_LANGUAGE, "en").toString()
        set(code) = preferences.edit().putString(PREF_CODE_LANGUAGE, code).apply()


    var accessToken: String
        get() = preferences.getString(PREF_KEY_ACCESS_TOKEN, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_ACCESS_TOKEN, token).apply()


    var speechRate: Float
        get() = preferences.getFloat(PREF_KEY_SPEECH_RATE_VOICE, 1.0F)
        set(token) = preferences.edit().putFloat(PREF_KEY_SPEECH_RATE_VOICE, token).apply()

    var speechPitch: Float
        get() = preferences.getFloat(PREF_KEY_SPEECH_PITCH_VOICE, 1.0F)
        set(token) = preferences.edit().putFloat(PREF_KEY_SPEECH_PITCH_VOICE, token).apply()


    var theFirstOpenApp: Boolean
        get() = preferences.getBoolean(PREF_KEY_THE_FIRST_OPEN_APP, true)
        set(isDarkMode) = preferences.edit().putBoolean(PREF_KEY_THE_FIRST_OPEN_APP, isDarkMode)
            .apply()


    var userInfo: String
        get() = preferences.getString(PREF_KEY_USER_INFO, "").toString()
        set(token) = preferences.edit().putString(PREF_KEY_USER_INFO, token).apply()

    var isModeOAI: Boolean
        get() = preferences.getBoolean(PREF_KEY_VOICE_MODE, true)
        set(isDarkMode) = preferences.edit().putBoolean(PREF_KEY_VOICE_MODE, isDarkMode)
            .apply()

    var characterId: Int
        get() = preferences.getInt(PREF_KEY_CHARACTER, 1).toInt()
        set(point) = preferences.edit().putInt(PREF_KEY_CHARACTER, point).apply()

    var pointUser: Double
        get() = preferences.getDouble(PREF_KEY_POINT_USER, 0.0)
        set(token) = preferences.edit().putDouble(PREF_KEY_POINT_USER, token).apply()

    var avatarUser: String
        get() = preferences.getString(PREF_AVATAR_USER, "").toString()
        set(token) = preferences.edit().putString(PREF_AVATAR_USER, token).apply()

    var nameUser: String
        get() = preferences.getString(PREF_NAME_USER, "").toString()
        set(token) = preferences.edit().putString(PREF_NAME_USER, token).apply()

    var codeUser: String
        get() = preferences.getString(PREF_CODE_USER, "").toString()
        set(token) = preferences.edit().putString(PREF_CODE_USER, token).apply()



    fun clearCache() {
        preferences.edit().clear().apply()
    }

    fun clearCacheForLogout() {

        //   fcmToken=""
        accessToken = ""
        userInfo = ""
    }

    fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))
}

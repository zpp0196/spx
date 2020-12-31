package me.zpp0196.spx.library

import android.app.Activity
import android.net.Uri

/**
 * @author zpp0196
 */
interface PrefsHandler {
    fun onPrefsUpdate(activity: Activity, uri: Uri): Boolean
}
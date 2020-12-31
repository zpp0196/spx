package me.zpp0196.spx.library

import android.app.Activity
import android.net.Uri

/**
 * @author zpp0196
 */
class SimplePrefsHandler(private val force: Boolean = false) : PrefsHandler {
    override fun onPrefsUpdate(activity: Activity, uri: Uri): Boolean {
        PrefsBridge.receiveDefaultPrefsFile(activity, uri)
        return force
    }
}
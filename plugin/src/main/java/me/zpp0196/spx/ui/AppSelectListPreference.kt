package me.zpp0196.spx.ui

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.util.AttributeSet
import androidx.preference.ListPreference

/**
 * @author zpp0196
 */
class AppSelectListPreference(context: Context?, attrs: AttributeSet?) :
    ListPreference(context, attrs) {

    private val resolveIntent by lazy {
        Intent().also {
            it.action = Intent.ACTION_MAIN
            it.addCategory(Intent.CATEGORY_LAUNCHER)
        }
    }
    private lateinit var activities: List<ActivityInfo>

    init {
        init()
    }

    private fun init() {
        val ris = context.packageManager.queryIntentActivities(resolveIntent, 0)
        activities = ris.map { it.activityInfo }
        val pm = context.packageManager
        entries = activities.map { it.loadLabel(pm) }.toTypedArray()
        entryValues = activities.map { it.packageName }.toTypedArray()
    }

    fun getSelectIntent(): Intent? {
        return activities.find { it.packageName == value }?.let { ai ->
            Intent(resolveIntent).let {
                it.setPackage(ai.packageName)
                it.setClassName(ai.packageName, ai.name)
            }
        }
    }
}
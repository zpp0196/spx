package me.zpp0196.spx.library

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import me.zpp0196.spx.R

/**
 * @author zpp0196
 */
class RebootDialog private constructor(private val context: Context) : PluginContext(context) {

    private fun show(pluginLabel: String, intent: Intent?) {
        val pm = context.packageManager
        val appLabel = pm.getApplicationInfo(context.packageName, 0).loadLabel(pm).toString()
        AlertDialog.Builder(context).apply {
            setCancelable(false)
            setTitle(getString(R.string.prefs_compat_tip_title))
            setMessage(
                getString(
                    R.string.prefs_compat_tip_message,
                    pluginLabel,
                    appLabel
                )
            )
            intent?.let {
                setNegativeButton(getString(R.string.prefs_compat_button_settings)) { _, _ ->
                    context.startActivity(it)
                }
            }
        }.show()
    }

    companion object {
        fun show(context: Context, pluginLabel: String) {
            val intent = Intent().let { intent ->
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                intent.data = Uri.parse("package:${context.packageName}")
                context.packageManager.runCatching {
                    resolveActivity(intent, PackageManager.MATCH_SYSTEM_ONLY)
                }.getOrNull()?.run { intent }
            }
            RebootDialog(context).show(pluginLabel, intent)
        }
    }
}
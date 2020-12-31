package me.zpp0196.spx

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.zpp0196.spx.library.PluginContext
import me.zpp0196.spx.library.PrefsBridge
import me.zpp0196.spx.library.SimplePrefsHandler
import me.zpp0196.xposed.interceptor.TextViewInterceptor

/**
 * @author zpp0196
 */
class Plugin : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "SpxPlugin"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        Log.i(TAG, "handle load package: ${lpparam.packageName}")
        PrefsBridge.handleLoadPackage(SimplePrefsHandler(true)) { app ->
            Log.i(TAG, "onApplicationCreate: $app")

            val pluginContext = PluginContext(app)
            val sp = PrefsBridge.getDefaultSharedPreferences(app)

            val switch = pluginContext.getString(R.string.pref_plugin_switch_key).let { key ->
                sp.getBoolean(key, false).also {
                    Log.i(TAG, "$key: $it")
                }
            }

            if (switch) {
                val tvText = pluginContext.getString(R.string.pref_tv_set_text_key).let { key ->
                    sp.getString(key, "").also {
                        Log.i(TAG, "$key: $it")
                    }
                }
                TextViewInterceptor.setText(tvText)
            }
        }
    }
}
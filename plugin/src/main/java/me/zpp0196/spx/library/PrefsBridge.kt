package me.zpp0196.spx.library

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.core.content.FileProvider
import me.zpp0196.xposed.interceptor.InstrumentationInterceptor
import org.apache.commons.io.FileUtils
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author zpp0196
 */
@Suppress("MemberVisibilityCanBePrivate")
object PrefsBridge {

    private const val authority = "${Constants.PLUGIN_PACKAGE}.provider"
    private const val prefsName = "${Constants.PLUGIN_PACKAGE}_preferences"
    private const val pluginLabel = "${Constants.PLUGIN_PACKAGE}_plugin_label"

    private class Lifecycle(
        private val handler: PrefsHandler
    ) : Application.ActivityLifecycleCallbacks {
        private var hasLauncherActivityOnCreate = AtomicBoolean(false)
        override fun onActivityPaused(activity: Activity) = Unit

        override fun onActivityStarted(activity: Activity) = Unit

        override fun onActivityDestroyed(activity: Activity) = Unit

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

        override fun onActivityStopped(activity: Activity) = Unit

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            if (hasLauncherActivityOnCreate.compareAndSet(false, true)) {
                val app = activity.applicationContext as? Application
                app?.unregisterActivityLifecycleCallbacks(this)

                val intent = activity.intent
                val uri = intent.data ?: return
                val pluginLabel = intent.getStringExtra(pluginLabel) ?: Constants.PLUGIN_PACKAGE
                activity.intent.data = null
                if (handler.onPrefsUpdate(activity, uri)) {
                    RebootDialog.show(activity, pluginLabel)
                }
            }
        }

        override fun onActivityResumed(activity: Activity) = Unit
    }

    fun getDefaultSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    fun handleLoadPackage(
        handler: PrefsHandler,
        onCreate: ((app: Application) -> Unit)? = null
    ) {
        InstrumentationInterceptor.callApplicationOnCreate { app ->
            registerLifecycleHandler(app, handler)
            onCreate?.invoke(app)
        }
    }

    fun launchWithDefaultPrefs(context: Context, intent: Intent) {
        val file = prepareDefaultPrefsFile(context)
        putFileToIntent(context, file, intent)
        putPluginLabelToIntent(context, intent)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun putFileToIntent(context: Context, file: File, intent: Intent): Intent {
        val fileUri = FileProvider.getUriForFile(context, authority, file)
        return intent.also {
            it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            it.setDataAndType(fileUri, context.contentResolver.getType(fileUri))
        }
    }

    fun putPluginLabelToIntent(context: Context, intent: Intent) {
        val pm = context.packageManager
        val label = pm.runCatching {
            pm.getApplicationInfo(Constants.PLUGIN_PACKAGE, 0).loadLabel(pm).toString()
        }.getOrDefault(Constants.PLUGIN_PACKAGE)
        intent.putExtra(pluginLabel, label)
    }

    fun prepareDefaultPrefsFile(context: Context): File {
        val name = "$prefsName.xml"
        val srcDir = File(context.dataDir, "shared_prefs")
        val targetDir = File(context.filesDir, "shared_prefs")
        return File(srcDir, name).copyTo(File(targetDir, name), true)
    }

    fun receiveDefaultPrefsFile(context: Context, uri: Uri) {
        context.contentResolver.openInputStream(uri)?.use { ips ->
            val file = context.run {
                val path = File(dataDir, "shared_prefs")
                File(path, "$prefsName.xml")
            }
            FileUtils.copyInputStreamToFile(ips, file)
        }
    }

    fun registerLifecycleHandler(app: Application, handler: PrefsHandler) {
        app.registerActivityLifecycleCallbacks(Lifecycle(handler))
    }
}
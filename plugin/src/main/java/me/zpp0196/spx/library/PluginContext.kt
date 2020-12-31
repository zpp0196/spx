package me.zpp0196.spx.library

import android.app.AndroidAppHelper
import android.content.Context
import androidx.annotation.StringRes

/**
 * @author zpp0196
 */
open class PluginContext(private val ctx: Context? = AndroidAppHelper.currentApplication()) {

    private val mContext: Context by lazy {
        ctx?.createPackageContext(Constants.PLUGIN_PACKAGE, Context.CONTEXT_IGNORE_SECURITY)
            ?: throw NotImplementedError("please create your context")
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: String): String {
        return mContext.getString(resId, *formatArgs)
    }
}
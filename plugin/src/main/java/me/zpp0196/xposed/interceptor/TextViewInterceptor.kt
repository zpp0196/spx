package me.zpp0196.xposed.interceptor

import android.widget.TextView
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * @author zpp0196
 */
object TextViewInterceptor {

    private val clazz = TextView::class.java

    fun setText(text: CharSequence?) {
        XposedHelpers.findAndHookMethod(
            clazz, "setText", CharSequence::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.args[0] = text
                }
            })
    }
}
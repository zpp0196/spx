package me.zpp0196.xposed.interceptor

import android.app.Application
import android.app.Instrumentation
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

/**
 * @author zpp0196
 */
object InstrumentationInterceptor {

    private val clazz = Instrumentation::class.java

    fun callApplicationOnCreate(before: (app: Application) -> Unit) {
        XposedHelpers.findAndHookMethod(clazz,
            "callApplicationOnCreate",
            Application::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    val app = param.args[0] as Application
                    before(app)
                }
            })
    }
}
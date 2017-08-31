package com.brzhang.fllipped

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.brzhang.fllipped.utils.LogUtil
import com.tencent.mta.track.StatisticsDataAPI
import com.tencent.stat.StatService
import java.net.URI
import java.net.URISyntaxException
import java.util.HashMap
import com.tencent.stat.StatCrashCallback
import com.tencent.stat.StatCrashReporter
import com.tencent.stat.StatConfig
import com.tencent.stat.StatReportStrategy





/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        Env.initEnv(this)
        initMta()

        // 请在初始化时调用，参数为Application或Activity或Service
        StatisticsDataAPI.instance(this)
        StatService.setContext(this)
        //  初始化MTA配置
        initMTAConfig(BuildConfig.DEBUG)
        // 注册Activity生命周期监控，自动统计时长
        StatService.registerActivityLifecycleCallbacks(this)
        // 初始化MTA的Crash模块，可监控java、native的Crash，以及Crash后的回调
        MTACrashModule.initMtaCrashModule(this)
    }

    companion object {
        internal lateinit var sInstance: App

        fun ApplicationContext(): Context {
            return sInstance.applicationContext
        }
    }

    fun initMta() {
        val appContext = sInstance.applicationContext
        try {
            StatService.startStatService(appContext, null, com.tencent.stat.common.StatConstants.VERSION)
            val map = getDomainName(Env.getHttpUrl())
            StatService.testSpeed(appContext, map)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。

     * @param isDebugMode
     * *            根据调试或发布条件，配置对应的MTA配置
     */
    private fun initMTAConfig(isDebugMode: Boolean) {

        if (isDebugMode) { // 调试时建议设置的开关状态
            // 查看MTA日志及上报数据内容
            StatConfig.setDebugEnable(true)
            // StatConfig.setEnableSmartReporting(false);
            // Thread.setDefaultUncaughtExceptionHandler(new
            // UncaughtExceptionHandler() {
            //
            // @Override
            // public void uncaughtException(Thread thread, Throwable ex) {
            // logger.error("setDefaultUncaughtExceptionHandler");
            // }
            // });
            // 调试时，使用实时发送
            // StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
            // // 是否按顺序上报
            // StatConfig.setReportEventsByOrder(false);
            // // 缓存在内存的buffer日志数量,达到这个数量时会被写入db
            // StatConfig.setNumEventsCachedInMemory(30);
            // // 缓存在内存的buffer定期写入的周期
            // StatConfig.setFlushDBSpaceMS(10 * 1000);
            // // 如果用户退出后台，记得调用以下接口，将buffer写入db
            // StatService.flushDataToDB(getApplicationContext());

            // StatConfig.setEnableSmartReporting(false);
            // StatConfig.setSendPeriodMinutes(1);
            // StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        } else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
            // 禁止MTA打印日志
            StatConfig.setDebugEnable(false)
            // 根据情况，决定是否开启MTA对app未处理异常的捕获
            StatConfig.setAutoExceptionCaught(true)
            // 选择默认的上报策略
            StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD)
            // 10分钟上报一次的周期
            StatConfig.setSendPeriodMinutes(10)
        }

        // 初始化java crash捕获
        StatCrashReporter.getStatCrashReporter(applicationContext).javaCrashHandlerStatus = true
        // 初始化native crash捕获，记得复制so文件
        StatCrashReporter.getStatCrashReporter(applicationContext).jniNativeCrashStatus = true
        // crash的回调，请根据需要添加
        StatCrashReporter.getStatCrashReporter(applicationContext).addCrashCallback(object : StatCrashCallback {

            override fun onJniNativeCrash(tombstoneMsg: String) {
                LogUtil.dLoge("Test", "Native crash happened, tombstone message:" + tombstoneMsg)
            }

            override fun onJavaCrash(thread: Thread, throwable: Throwable) {
                LogUtil.dLoge("Test", "Java crash happened, thread: " + thread + ",Throwable:" + throwable.toString())
            }
        })

    }

    @Throws(URISyntaxException::class)
    private fun getDomainName(url: String): Map<String, Int> {
        val uri = URI(url)
        var domain = uri.host
        domain = if (domain.startsWith("www.")) domain.substring(4) else domain
        val port = uri.port
        val map = HashMap<String, Int>()
        map.put(domain, port)
        return map
    }
}

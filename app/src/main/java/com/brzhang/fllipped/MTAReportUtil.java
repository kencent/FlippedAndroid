package com.brzhang.fllipped;

import android.app.Activity;
import android.content.Context;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatServiceImpl;
import com.tencent.stat.StatSpecifyReportedInfo;
import com.tencent.stat.StatReportStrategy;
import com.tencent.stat.StatService;

import java.util.Properties;

/**
 * MTA上报类，建议通过封装本类来调用MTA所有接口
 */
public class MTAReportUtil {
    private Context context = null;

    private static volatile MTAReportUtil instance = null;
    // 用户保存业务方定制的的appkey和渠道
    private StatSpecifyReportedInfo mtaReportInfo = new StatSpecifyReportedInfo();

    private MTAReportUtil(Context ctx) {
        this.context = ctx.getApplicationContext();
    }
    

    /**
     * 初始化mta，需要把appkey和渠道改为业务实际的值。
     * @param channel
     * @param appKey
     */
    public void initMtaConfig(String channel, String appKey) {
        // 配置MTA的appkey
        mtaReportInfo.setAppKey(appKey);
        // 设置渠道
        mtaReportInfo.setInstallChannel(channel);
        // 多进程支持
        StatConfig.setEnableConcurrentProcess(true);
        // 禁止异常捕获，防止跟rdm的接口冲突
        StatConfig.setAutoExceptionCaught(false);
        // 初始化context
        StatService.setContext(context);

        calledBeforeStat();
    }

    public static MTAReportUtil getInstance(Context ctx) {
        if (instance == null) {
            synchronized (MTAReportUtil.class) {
                if (instance == null) {
                    instance = new MTAReportUtil(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 前端使用时间统计
     * @param activity
     */
    public void onResume(Activity activity){
        calledBeforeStat();
        StatServiceImpl.onResume(activity, mtaReportInfo);
    }

    /**
     * 前端使用时间统计
     * @param activity
     */
    public void onPause(Activity activity){
        calledBeforeStat();
        StatServiceImpl.onPause(activity, mtaReportInfo);
    }

    /**
     *
     * @param eventId 事件的id
     * @param keyvalues 事件的key以及对应value
     */
    public void reportKVEvent(String eventId, Properties keyvalues) {
        calledBeforeStat();
        //MTA 上报
        StatServiceImpl.trackCustomKVEvent(context, eventId, keyvalues,
                mtaReportInfo);
    }

    public void enableDebug(boolean enableDebug) {
        // 是否打开logcat debug，标签为“MtaSDK"。上线时请关闭
        StatConfig.setDebugEnable(enableDebug);
    }


    /**
     * 这是为了覆盖qq互联里面的配置，所以每次在调用StatServiceImpl类统计接口时，都要调用一下，以免被qq互联的配置覆盖了。
     */
    private void calledBeforeStat() {
        // 设置上报策略，默认为APP启动时上报。
        // 这个可以通过前台系统在线更新
        // StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);
        // 按周期上报
        StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        // 30分钟的周期
        StatConfig.setSendPeriodMinutes(30);
        // 设置wifi网络下是否实时上报，默认开启
        StatConfig.setEnableSmartReporting(true);
        // 设置上报业务专用的上报url
        //StatConfig.setStatReportUrl("http://sngmta.qq.com:80/mstat/report/");
    }

    /**
     * 用户登陆时，上报qq号码
     * @param uin
     */
    public void setMTAUin(String uin) {
        //MTA  上报
        calledBeforeStat();
        // 设置uin号码，方便后台提取用户号码包
        StatConfig.setCustomUserId(context, uin);
        // 上报uin
        StatServiceImpl.reportQQ(context, uin, mtaReportInfo);
    }


    /**
     *
     * @param eventId 事件的id
     * @param keyvalues 事件的key以及对应value
     * @param intervalSeconds 事件的耗时，单位自定义，默认为秒
     */
    public void reportTimeKVEvent(String eventId, Properties keyvalues,
                                  int intervalSeconds) {
        // 每次上报之前调用，防止被互联sdk修改
        calledBeforeStat();
        // 业务要调用StatServiceImpl类提供的接口，额外传递mtaReportInfo参数，防止appkey的串联
        StatServiceImpl.trackCustomKVTimeIntervalEvent(context, eventId,
                keyvalues, intervalSeconds, mtaReportInfo);
    }

    /**
     * 页面统计，这个跟onResume二选一，匹配使用
     * @param pageName 开始页面
     */
    public void trackBeginPage(String pageName){
        // 每次上报之前调用，防止被互联sdk修改
        calledBeforeStat();
        // 业务要调用StatServiceImpl类提供的接口，额外传递mtaReportInfo参数，防止appkey的串联
        StatServiceImpl.trackBeginPage(context, pageName, mtaReportInfo);
    }

    /**
     * 页面统计，这个跟onPause二选一，匹配使用
     * @param pageName 结束页面。需要跟开始页面完全匹配
     */
    public void trackEndPage(String pageName){
        // 每次上报之前调用，防止被互联sdk修改
        calledBeforeStat();
        // 业务要调用StatServiceImpl类提供的接口，额外传递mtaReportInfo参数，防止appkey的串联
        StatServiceImpl.trackEndPage(context, pageName, mtaReportInfo);
    }
}

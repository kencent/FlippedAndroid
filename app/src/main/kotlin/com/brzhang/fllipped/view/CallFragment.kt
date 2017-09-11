package com.brzhang.fllipped.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ViewFlipper
import com.brzhang.fllipped.R
import com.brzhang.fllipped.model.CallRequeset
import com.brzhang.fllipped.model.CallResponse
import com.brzhang.fllipped.model.SignResponse
import com.brzhang.fllipped.pref.UserPref
import com.tencent.callsdk.*
import com.tencent.ilivesdk.ILiveCallBack
import com.tencent.ilivesdk.core.ILiveLoginManager
import okhttp3.ResponseBody
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

/**
 *
 * Created by brzhang on 2017/8/31.
 * Description :
 */
open class CallFragment : BaseFragment(), ILVIncomingListener, ILVCallListener, ILVCallNotificationListener {


    private var callTv: TextView? = null
    private var callTips: TextView? = null
    private var isLoginSdk: Boolean = false
    private var viewFlipper: ViewFlipper? = null

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_call, container, false)
        viewFlipper = view?.findViewById(R.id.call_fragment_vf) as ViewFlipper
        callTv = view?.findViewById(R.id.call_fragment_tv_call) as TextView
        callTips = view.findViewById(R.id.call_fragment_tv_call_tips) as TextView
        callTv?.setOnClickListener {
            if (mCallState != CallState.Calling) {
                if (isLoginSdk) {
                    mCallState = CallState.Calling
                    switchCallBtn()
                    callSomeOne()
                } else {
                    loginSdkAndpreCall()
//                    toast("sdk 故障，请稍后再试~~")
                }
            } else {
                cancelCall()
            }
        }
        ILVCallManager.getInstance().init(ILVCallConfig()
                .setNotificationListener(this)
                .setAutoBusy(true))
        loginSdkAndpreCall()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginOut()
    }

    private fun loginOut() {
        ILiveLoginManager.getInstance().iLiveLogout(object : ILiveCallBack<Int> {
            override fun onSuccess(data: Int) {
                isLoginSdk = false
                removeCallBack()
            }

            override fun onError(module: String, errCode: Int, errMsg: String) {
                dtoast(errMsg)
            }
        })
    }

    fun addCallBack() {
        // 设置通话回调
        removeCallBack()
        ILVCallManager.getInstance().addIncomingListener(this)
        ILVCallManager.getInstance().addCallListener(this)
    }

    fun removeCallBack() {
        ILVCallManager.getInstance().removeIncomingListener(this)
        ILVCallManager.getInstance().removeCallListener(this)
    }

    private fun loginSdkAndpreCall() {
        addCallBack()
        fllippedNetService().getLvbSig().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<SignResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        dtoast("获取视频sig失败")
                    }

                    override fun onNext(signResponse: SignResponse) {
                        loginSDK(UserPref.getUserName(context, ""), signResponse.sig)
                    }
                })
    }

    private fun switchCallBtn() {
        if (mCallState == CallState.Calling) {
            viewFlipper?.displayedChild = 1
            callTv?.text = "退出"
            callTips?.text = "退出后，点匹配Ta可以重新开始召唤Ta"
        } else {
            viewFlipper?.displayedChild = 0
            callTv?.text = "召唤Ta"
            callTips?.text = "点击后，可以开始召唤心动的Ta"
        }

    }

    /**
     * 使用userSig登录iLiveSDK(独立模式下获有userSig直接调用登录)
     */
    private fun loginSDK(id: String, userSig: String) {
        ILiveLoginManager.getInstance().iLiveLogin(id, userSig, object : ILiveCallBack<Int> {
            override fun onSuccess(data: Int?) {
                isLoginSdk = true
            }

            override fun onError(module: String?, errCode: Int, errMsg: String?) {
                toast("Login failed:$module|$errCode|$errMsg")
            }
        })
    }

    public fun cancelCall() {
        mCallState = CallState.Init
        switchCallBtn()
        mHandler.removeCallbacks(mCallRunable)
        fllippedNetService().cancelCall().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<ResponseBody>() {
                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        dtoast("取消call失败")
                    }

                    override fun onNext(callResponse: ResponseBody?) {

                    }
                })
    }

    /**
     * 去后台请求可以匹配上视频的人
     */
    private fun callSomeOne() {
        if (mCallState != CallState.Calling) {
            return
        }
        var callRequest = CallRequeset();
        fllippedNetService().callSomeOne(callRequest).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Subscriber<CallResponse>() {
                    override fun onCompleted() {

                    }

                    override fun onError(e: Throwable) {
                        dtoast("调用后台预call失败")
                        mCallState = CallState.Init
                        switchCallBtn()
                    }

                    override fun onNext(callResponse: CallResponse) {
                        if (callResponse.wait_timeout != null && callResponse.wait_timeout!! > 0L) {
                            dtoast("当前没有可以匹配的人，等待【" + callResponse.wait_timeout + "】s")
                            sendRepeatCallMessage(callResponse.wait_timeout!!)
                        } else if (callResponse.uid != null) {
                            dtoast("后台给出一个可以匹配的人，uid是" + callResponse.uid)
                            mCallState = CallState.CallSucc
                            switchCallBtn()
                            makeReallyCall(callResponse.uid!!)
                        }
                    }
                })
    }

    private val INTENT_CALLING_SUCCESS: Int = 200

    enum class CallState {
        Init, Calling, CallSucc
    }

    private var mCallState: CallState = CallState.Init

    /**
     * 已经匹配上，我call过去
     */
    private fun makeReallyCall(uid: String) {
        val nums = ArrayList<String>()
        nums.add(uid)
        val intent = Intent()
        intent.setClass(context, CallActivity::class.java)
        intent.putExtra("HostId", ILiveLoginManager.getInstance().myUserId)
        intent.putExtra("CallId", 0)
        intent.putExtra("CallType", ILVCallConstants.CALL_TYPE_VIDEO)
        intent.putStringArrayListExtra("CallNumbers", nums)
        startActivityForResult(intent, INTENT_CALLING_SUCCESS)
        onMatched()
    }

    /**
     * 匹配成功之后，去掉call
     */
    private fun onMatched() {
        mHandler.removeCallbacks(mCallRunable)
    }

    /**
     * 已经匹配上，别人call过来
     */
    private fun acceptReallyCall(hostId: String, mCurIncomingId: Int, callType: Int) {
        val intent = Intent()
        intent.setClass(context, CallActivity::class.java)
        intent.putExtra("HostId", hostId)
        intent.putExtra("CallId", mCurIncomingId)
        intent.putExtra("CallType", callType)
        startActivityForResult(intent, INTENT_CALLING_SUCCESS)
        onMatched()
    }

    /**
     * 重视频界面返回，界面应该做一下恢复工作
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTENT_CALLING_SUCCESS) {
            mCallState = CallState.Init
            switchCallBtn()
        }
    }


    private fun sendRepeatCallMessage(wait_timeout: Long) {
        mHandler.postDelayed(mCallRunable, wait_timeout)
    }

//    private class MyHandler(fragment: CallFragment) : Handler() {
//        private val mFragment: WeakReference<CallFragment>
//
//        init {
//            mFragment = WeakReference(fragment)
//        }
//
//        override fun handleMessage(msg: Message) {
//            val activity = mFragment.get()
//            if (activity != null) {
//                // ...
//            }
//        }
//    }

    private val mCallRunable = Runnable { callSomeOne() }

    private val mHandler = Handler(Looper.getMainLooper())


    override fun onRecvNotification(callid: Int, notification: ILVCallNotification<out ILVCallNotification<*>>?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallEnd(callId: Int, endResult: Int, endInfo: String?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onException(iExceptionId: Int, errCode: Int, errMsg: String?) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCallEstablish(callId: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNewIncomingCall(callId: Int, callType: Int, notification: ILVIncomingNotification?) {
        acceptReallyCall(notification?.sponsorId ?: "", callId, callType)
    }

    companion object {

        fun newInstance(): CallFragment {

            val args = Bundle()

            val fragment = CallFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
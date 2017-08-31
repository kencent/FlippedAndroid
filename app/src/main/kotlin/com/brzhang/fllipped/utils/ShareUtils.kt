package com.brzhang.fllipped.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.brzhang.fllipped.R
import com.brzhang.fllipped.WXUtil
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI


/**
 *
 * Created by brzhang on 2017/8/19.
 * Description :微信分享工具
 *
 *
 * api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
 * ShareUtils.shareMoment(api,this,......)
 * https://flippedwords.com/d.html?id=1000118
 */

object ShareUtils {
    private val THUMB_SIZE = 150

    fun shareMoment(iwxapi: IWXAPI, context: Context, url: String, title: String, description: String) {
        val webpage = WXWebpageObject()
        webpage.webpageUrl = url
        val msg = WXMediaMessage(webpage)
        msg.title = title
        msg.description = description
        val bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher)
        val thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        bmp.recycle()
        msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
//        req.scene = mTargetScene
        req.scene = SendMessageToWX.Req.WXSceneTimeline
        iwxapi.sendReq(req)
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }
}

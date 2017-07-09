package com.brzhang.fllipped.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.brzhang.fllipped.App
import com.brzhang.fllipped.BizService
import com.brzhang.fllipped.pref.UserPref
import com.tencent.cos.model.PutObjectRequest
import rx.Observable

/**
 *
 * Created by brzhang on 2017/7/9.
 * Description :
 */

object UploadUtils {

    fun uploadImage(sign: String, imagePath: String): Observable<String> {
        var bizService = BizService.instance()
        bizService.init(App.ApplicationContext())

        /** PutObjectRequest 请求对象 */
        val putObjectRequest = PutObjectRequest()
        /** 设置Bucket */
        putObjectRequest.bucket = bizService.bucket
        /** 设置cosPath :远程路径*/
        putObjectRequest.cosPath = UserPref.getUserName(App.ApplicationContext(), "nobody") + "/" + imagePath
        /** 设置srcPath: 本地文件的路径 */
        putObjectRequest.srcPath = imagePath
        /** 设置 insertOnly: 是否上传覆盖同名文件*/
        putObjectRequest.insertOnly = "1"
        /** 设置sign: 签名，此处使用多次签名 */
        putObjectRequest.sign = sign

        /** 设置sha: 是否上传文件时带上sha，一般不需要带*/
        //putObjectRequest.setSha(putObjectRequest.getsha());
        /** 设置listener: 结果回调 */
        //        putObjectRequest.listener = listener
//        putObjectRequest.listener = object : IUploadTaskListener {
//            override fun onProgress(cosRequest: COSRequest, currentSize: Long, totalSize: Long) {
//                val progress = (100.00 * currentSize / totalSize).toLong()
//                Log.w("XIAO", "progress =$progress%")
//            }
//
//            override fun onCancel(cosRequest: COSRequest, cosResult: COSResult) {
//                val result = "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg
//                Log.w("XIAO", result)
//            }
//
//            override fun onSuccess(cosRequest: COSRequest, cosResult: COSResult) {
//                val putObjectResult = cosResult as PutObjectResult
//                val stringBuilder = StringBuilder()
//                stringBuilder.append(" 上传结果： ret=" + putObjectResult.code + "; msg =" + putObjectResult.msg + "\n")
//                stringBuilder.append(if (" access_url= " + putObjectResult.access_url == null) "null" else putObjectResult.access_url + "\n")
//                stringBuilder.append(if (" resource_path= " + putObjectResult.resource_path == null) "null" else putObjectResult.resource_path + "\n")
//                stringBuilder.append(if (" url= " + putObjectResult.url == null) "null" else putObjectResult.url)
//                val result = stringBuilder.toString()
//                Log.w("XIAO", result)
//            }
//
//            override fun onFailed(cosRequest: COSRequest, cosResult: COSResult) {
//                val result = "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg
//                Log.w("XIAO", result)
//            }
//        }
        /** 发送请求：同步执行 */
//        bizService.cosClient.putObject(putObjectRequest)
        return Observable.create {
            subs->
            val result = bizService.cosClient.putObject(putObjectRequest)
            if (result.code == 0){
                subs.onNext(result.access_url)
                subs.onCompleted()
            }
            else {
                subs.onError(Error(result.msg))
            }
        }

    }

    fun UriToPath(context: Context, uri: Uri): String {
        var path: String? = null
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf(MediaStore.MediaColumns.DATA)
            val colum_name = "_data"
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                Log.w("XIAO", "count =" + cursor!!.count)
                LogUtil.dLoge("hoolly", "count =" + cursor!!.count)
                if (cursor != null && cursor.moveToFirst()) {
                    val colum_index = cursor.getColumnIndex(colum_name)
                    path = cursor.getString(colum_index)
                }
            } catch (e: Exception) {
                LogUtil.dLoge("hoolly", e.message)
            } finally {
                if (cursor != null) {
                    cursor.close()
                }
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            path = uri.path
        } else {
            LogUtil.dLoge("hoolly", "选择文件路径为空")
        }
        return path ?: ""
    }
}
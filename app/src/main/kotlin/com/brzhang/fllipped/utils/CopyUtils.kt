package com.brzhang.fllipped.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context



/**
 * Created by hoollyzhang on 2017/8/2.
 */
object CopyUtils{
    fun copyText(context: Context,text:String){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", text)
        clipboard.primaryClip = clip
    }
}
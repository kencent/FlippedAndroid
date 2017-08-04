package com.brzhang.fllipped

import com.brzhang.fllipped.model.Flippedword

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
object FlippedHelper {
//    # status取值：
//    # 	0 新发表
//    # 	100 已下发给接收人客户端
//    # 	200 接收人已读

    enum class FlippedState(val value: Int) {
        NEW(0),
        RECEIVE(100),
        READ(200)
    }

    fun getText(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("text")) {
                return it.text ?: ""
            }
        }
        return ""
    }


    fun getPic(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("picture")) {
                return it.text ?: ""
            }
        }
        return ""
    }

    fun getVoice(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("audio")) {
                return it.text ?: ""
            }
        }
        return ""
    }

    fun getVideo(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("video")) {
                return it.text ?: ""
            }
        }
        return ""
    }

    fun getDistance(flippedword: Flippedword?): String {
        var distance = flippedword?.distance ?: 0
        if (distance > 1000) {
            return String.format("%.1fkm", distance / 1000.0)
        } else if (distance == 0L) {
            return "在你附近"
        } else {
            return "${distance}m"
        }
    }

    fun getReadState(flippedword: Flippedword?): String? {
        when (flippedword?.status) {
            FlippedState.NEW.ordinal -> {
                return "对方未读"
            }
            FlippedState.RECEIVE.ordinal -> {
                return "已送达"
            }
            FlippedState.READ.ordinal -> {
                return "对方已读"
            }
            else -> {
                return "对方不在平台"
            }
        }
    }

    fun canDelete(fliipped: Flippedword?): Boolean {
        if (fliipped?.links == null || fliipped?.links!!.isEmpty()) {
            return false
        }
        return fliipped.links!!.any { it.rel.toUpperCase() == "DELETE" }
    }

}
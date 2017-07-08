package com.brzhang.fllipped.utils

import java.security.InvalidKeyException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 *
 * Created by brzhang on 2017/7/8.
 * Description :
 */
object EncodeUtils {
     fun md5(pwd: String): String {
        try {
            // 创建加密对象
            val digest = MessageDigest.getInstance("md5")

            // 调用加密对象的方法，加密的动作已经完成
            val bs = digest.digest(pwd.toByteArray())
            // 接下来，我们要对加密后的结果，进行优化，按照mysql的优化思路走
            // mysql的优化思路：
            // 第一步，将数据全部转换成正数：
            return byte2hex(bs)
        } catch (e: NoSuchAlgorithmException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return ""
    }

    fun hamcsha1(key: ByteArray, data: ByteArray): String? {
        try {
            val signingKey = SecretKeySpec(key, "HmacSHA1")
            val mac = Mac.getInstance("HmacSHA1")
            mac.init(signingKey)
            return byte2hex(mac.doFinal(data))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }

        return null
    }

    //二行制转字符串
    fun byte2hex(b: ByteArray?): String {
        val hs = StringBuilder()
        var stmp: String
        var n = 0
        while (b != null && n < b.size) {
            stmp = Integer.toHexString(b[n].toInt() and 0XFF)
            if (stmp.length == 1)
                hs.append('0')
            hs.append(stmp)
            n++
        }
        return hs.toString().toUpperCase()
    }
}
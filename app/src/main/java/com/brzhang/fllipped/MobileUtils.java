package com.brzhang.fllipped;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hoollyzhang on 2017/10/12.
 */

public class MobileUtils {

    //提取字符串中的手机号码
    public static String getMobiles(String str) {
        Pattern p = Pattern.compile("(\\+86|)?(\\d{11})");
        Matcher m = p.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            if(sb.length()>0)
                sb.append(",");
            sb.append(m.group(2));
        }
        /*
        * 不加"()"也能将手机号码输出 添加"()"是为了筛选数据添加上去的，
        * 第一对"()"是为了获取字符串"+86"，代码是System.out.println(m.group(1));，
        * 第二对"()"是获取11位纯数字电话号码， 本次的输出的手机号码中包含了"+86"，如果只要11位数字号码，
        * 可将代码改为System.out.println(m.group(2));
        */
        //System.out.println(m.groupCount());// 该行代码是输出有几对"()"，即捕获组个数，本次输出结果是2，因为有两对"()"
        return sb.toString();
    }
}

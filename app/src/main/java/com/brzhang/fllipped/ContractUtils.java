package com.brzhang.fllipped;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoollyzhang on 2017/10/12.
 */

public class ContractUtils {


    public static List<ContractModel> getContracts(Context context) {

        List<ContractModel> list = new ArrayList<>();
        //得到ContentResolver对象
        ContentResolver cr = context.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //向下移动光标
        while (cursor.moveToNext()) {

            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);

            String PhoneNumber = "";
            while (phone.moveToNext()) {
                PhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //格式化手机号
                PhoneNumber = PhoneNumber.replace("-", "");
                PhoneNumber = PhoneNumber.replace(" ", "");
            }
            ContractModel contractModel = new ContractModel(contact, PhoneNumber);
            list.add(contractModel);
        }
        return list;
    }
}

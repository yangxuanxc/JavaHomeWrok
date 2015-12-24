package com.dcapi.talk.test;

import android.util.Log;

import com.dcapi.talk.utils.HttpUtils;

/**
 * Created by Dcapi on 2015/12/24.
 */
public class TestHttpUtils {
    public void testSendInfo()
    {
        String res = HttpUtils.doGet("给我讲个笑话");
        Log.e("TAG", res);
        res = HttpUtils.doGet("给我讲个鬼故事");
        Log.e("TAG", res);
        res = HttpUtils.doGet("你好");
        Log.e("TAG", res);
        res = HttpUtils.doGet("你真美");
        Log.e("TAG", res);
    }
}

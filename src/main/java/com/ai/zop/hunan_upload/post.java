package com.ai.zop.hunan_upload;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class post {
    public static void main(String[] args) throws Exception, FieldErrorException {
        // 用户ID 定义
        String uid ="yxhl" ;
        // 签名key 定义
        String  aesKey="27205$lq*i0x43^-";
        //身份服务验证json数据
        //String msg = "{\"busiSerial\":\"0414310088741787\",\"msg\":{\"province\":\"140000\",\"city\":\"141100\",\"certName\":\"范伟\",\"certNum\":\"身份证号\"},\"timestamp\":"+System.currentTimeMillis()/1000+"}";
        //选号服务json数据
        String msg = "{\"busiSerial\":\"0414310088741787\",\"msg\":{\"provinceCode\":\"430000\",\"cityCode\":\"430100\",\"goodsId\":\"981703239394\"},\"timestamp\":"+System.currentTimeMillis()/1000+"}";

        // 针对msg内容进行签名
        String data = MsgSignatureUtil.generateSignaturedMsg(msg, aesKey);
        // 加密，加密后内容需要进行URLEncoder
        String key = URLEncoder.encode(AESPlus.encrypt(data, aesKey), "UTF-8");
        // 生成请求url
        //身份服务验证网址
//        String infoUrl = "https://www.73110010.com/portal/orderCenter/kPlan/checkCustInfo?uid=" + uid + "&key=" + key;

        //选号服务网址
        String infoUrl = "https://www.73110010.com/portal/orderCenter/kPlan/selectNumber?uid=" + uid + "&key=" + key;
        //发送请求
//        String result = request(infoUrl);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(infoUrl)
                .build();
        Response response = client.newCall(request).execute();
        String resStr = response.body().string();
        System.out.println(infoUrl);
        System.out.println("返回报文：" + resStr);
    }
}

package com.ai.zop.hunan_upload;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

//湖南接口自动化导数

public class hunan_Automatic_upload {
    public static void main(String[] args) throws Exception, FieldErrorException {
        // 用户ID 定义
        String uid = "yxhl";
        // 签名key 定义
        String aesKey = "27205$lq*i0x43^-";

        //读取文件中信息
        ArrayList<String> list = new ArrayList<String>();
        File file = new File("C:\\Users\\范伟\\Desktop\\湖南数据");
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String str = null;
                try {
                    LineNumberReader reader = null;
                    reader = new LineNumberReader(new FileReader(tempList[i]));
                    while ((str = reader.readLine()) != null) {
                        //System.out.println(str);//输出所有待提交的用户数据
                        String[] split = str.split(",");
                        //msg
                        String province = split[0];//收货省份编码
                        String cityCode = split[1];//收货地市编码
                        String countyCode = split[2];//收货区县编码
                        String userAddr = split[3];//收货地址
                        String productId = split[4];//产品ID
                        String productCode = split[5];//商品ID
                        String productName = split[6];//商品名称
                        String touchId = split[7];//触点编码

                        //customInfo客户信息
                        String certId = split[8];//证件号码
                        String certName = split[9];//客户证件姓名
                        String certType = split[10];//证件类型
                        String custPhone = split[11];//联系电话

                        Long time_stamp = System.currentTimeMillis() / 1000;//时间戳

                        //选号服务json数据
                        String msg = "{\"msg\":{" +
                                "\"province\":" + "\"" + split[0] + "\"" + "," +
                                "\"cityCode\":" + "\"" + split[1] + "\"" + "," +
                                "\"countyCode\":" + "\"" + split[2] + "\"" + "," +
                                "\"userAddr\":" + "\"" + split[3] + "\"" + "," +
                                "\"productId\":" + "\"" + split[4] + "\"" + "," +
                                "\"productCode\":" + "\"" + split[5].trim() + "\"" + "," +
                                "\"productName\":" + "\"" + split[6] + "\"" + "," +
                                "\"customInfo\":{" +
                                "\"certId\":" + "\"" + split[8] + "\"" + "," +
                                "\"custPhone\":" + "\"" + split[11] + "\"" + "," +
                                "\"certName\":" + "\"" + split[9] + "\"" + "," +
                                "\"certType\":" + "\"" + split[10] + "\"" + "}," +
                                "\"developerId\":" + "\"" + "7412174485" + "\"" + ","+
                                "\"developerName\":" + "\"" + "云溪渠HL（自主引流）" + "\"" +","+
                                "\"projectId\":" + "\"" + "07_13_kjhyxqhlsq_1682136" + "\"" +","+
                                "\"touchId\":" + "\"" + split[7] + "\"" +
                                "}," +
                                "\"timestamp\":" + "\"" + System.currentTimeMillis() / 1000 + "\"" + "," +
                                "\"uid\":" + "\"" + uid + "\"" + "," +
                                "\"busiSerial\":" + "\"" + System.currentTimeMillis() + "\"" +"}";
//                        String msg="{\"msg\":{\"province\":\"430000\",\"cityCode\":\"430100\",\"countyCode\":\"430111\",\"userAddr\":\"测试地址\",\"productId\":\"90063345\",\"productCode\":\"981610241535\",\"productName\":\"腾讯大王卡\",\"customInfo\":{\"certId\":\"430121198510078515\",\"custPhone\":\"1560814361\",\"certName\":\"彭佳垠\",\"certType\":\"11\"},\"developerId\":\"A10381629\",\"developerName\":\"www.10010.com\",\"projectId\":\"07_13_kjhaqy_934040\",\"touchId\":\"08-2206-2842-9999\",\"is_selected_number\": \"1\",\"proKey\": \"9999965450699346\",\"proType\": \"268\",\"numberProvince\": \"430000\",\"numberCity\": \"430100\",\"serialNumber\": \"18569461281\"},\"timestamp\":"+System.currentTimeMillis() / 1000+",\"uid\":\"jaring\",\"busiSerial\":\"82a8b27a143c11ea9b47acde48001122\"}";
                        System.out.println(msg);
                        // 针对msg内容进行签名
                        String data = MsgSignatureUtil.generateSignaturedMsg(msg, aesKey);
                        // 加密，加密后内容需要进行URLEncoder
                        String key = URLEncoder.encode(AESPlus.encrypt(data, aesKey), "UTF-8");
                        // 生成请求url
                        //选号服务网址
                        String infoUrl = "https://www.73110010.com/portal/orderCenter/kPlan/KOrderSync?uid=" + uid + "&key=" + key;
                        //发送请求
                        OkHttpClient client = new OkHttpClient.Builder()
                                .readTimeout(0, TimeUnit.SECONDS)
                                .build();
                        Request request = new Request.Builder()
                                .url(infoUrl)
                                .build();
                        //获取返回值
                        Response response = client.newCall(request).execute();
                        String resStr = response.body().string();
                        System.out.println("返回报文：" + resStr);

                        Map resMap = JSON.parseObject(resStr);

                        FileWriter resp_data = new FileWriter("C:\\Users\\范伟\\Desktop\\logs\\1210_大王卡.txt",true);
                        resp_data.write(str+"__"+"返回:"+resMap+"\n");
                        resp_data.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

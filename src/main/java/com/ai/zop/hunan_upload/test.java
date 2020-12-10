package com.ai.zop.hunan_upload;

public class test {
    public static void main(String[] args) {
        String uid = "yxhl";
        String str="1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";
        String[] split = str.split(",");
        String msg = "{\"msg\":{" +
                "\"province\":" + split[0] +","+
                "\"cityCode\":" + split[1] +","+
                "\"countyCode\":" + split[2] +","+
                "\"userAddr\":" + split[3] +","+
                "\"productId\":" + split[4] +","+
                "\"productCode\":" + split[5] +","+
                "\"productName\":" + split[6] +","+
                "\"touchId\":" + split[7] +","+
                "\"customInfo\":{" +
                "\"certId\":"+split[8] +","+
                "\"certName\":"+split[9] +","+
                "\"certType\":"+split[10] +","+
                "\"custPhone\":"+split[11] +"},"+
                "}," +
                "\"timestamp\":" + System.currentTimeMillis() / 1000 + ","+
                "\"uid\":" + uid + ","+
                "\"regionId\":" + 0 + ","+
                "\"busiSerial\":" + System.currentTimeMillis() + "}";
        System.out.println(msg);
    }
}

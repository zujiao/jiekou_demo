package com.ai.zop.king;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class test {
    public static void main(String[] args) {
        String orderId=System.currentTimeMillis()+"_"+String.valueOf(UUID.randomUUID()).substring(0,16);
        System.out.println(System.currentTimeMillis());
        System.out.println(orderId);
        System.out.println("didi20180101100200123451671809");

        System.out.println(String.valueOf(UUID.randomUUID()).substring(0,16));
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");//设置日期格式
        String date = df.format(System.currentTimeMillis());// new Date()为获取当前系统时间，也可使用当前时间戳
        System.out.println("hanlian"+date+"123456");
    }
}

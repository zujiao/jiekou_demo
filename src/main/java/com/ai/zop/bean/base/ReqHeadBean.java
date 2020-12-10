package com.ai.zop.bean.base;


public class ReqHeadBean  {
    //时间戳 YYYY-MM-DD HH:MM:SS.SSS
    private String timestamp;
    //流水号
    private String uuid;
    //签名
    private String sign;
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
}
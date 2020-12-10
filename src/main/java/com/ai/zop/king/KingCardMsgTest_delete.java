package com.ai.zop.king;

import com.ai.zop.bean.base.ReqHeadBean;
import com.ai.zop.bean.base.ReqObj;
import com.ai.zop.tool.safe.SecurityTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class KingCardMsgTest_delete {
    private static final String APP_CODE = "2985EF44EC164C949963D0DD07B8070C";
    //测试环境：
    //消息删除：http://demo.mall.10010.com:8104/zop/king/card/msg/del/v1
    //生产环境：
    //消息删除：http://123.125.96.156/zop/king/card/msg/del/v1
    private static final String URL = "http://mall.10010.com/zop/king/card/msg/del/v1";
    private static final String HMAC = "ywk/W/q0KVp5fMa1tXchymRm91h1WklSOg3hF0YPPQJUg7XCL8NGH00+ZFBj84FOOBvEHYn2Pns1XBoKoqdA3g==";
    private static final String AES = "pIv85I3E98DDJZCjM1ZJPg==";

    @Test
    public void test1() throws Exception {
        JSONObject baseReq = new JSONObject();
        baseReq.put("appCode", APP_CODE);
        ReqObj obj = new ReqObj();
        ReqHeadBean head = new ReqHeadBean();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        head.setTimestamp(formatter.format(new Date()));
        head.setUuid(String.valueOf(UUID.randomUUID()));
        head.setSign(makeSign(head, APP_CODE));
        obj.setHead(head);
        Map body = new HashMap();
        body.put("msgId", "1920111667036577,\n" +
                "5620111667036615");
        body.put("type", "3");//3-删除订单下单消息；4-删除订单状态变更消息
        obj.setBody(body);
        //reqObj节点需要加密时
        baseReq.put("reqObj", SecurityTool.encrypt(AES, JSON.toJSONString(obj)));
        System.out.println("请求报文：" + JSON.toJSONString(baseReq));
        MediaType json = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = RequestBody.create(json, JSON.toJSONString(baseReq));
        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        String resStr = response.body().string();
        System.out.println("返回报文：" + resStr);
        Map resMap = JSON.parseObject(resStr);
        if ("0000".equals(resMap.get("rspCode"))) {
            //成功，处理body里的消息数据
        } else {
            //失败，获取消息数据，做好本地记录，反馈给商城管理员
        }
    }

    public String makeSign(ReqHeadBean req, String appCode) throws Exception {
        StringBuffer sb = new StringBuffer();
        //appCode+head节点（除sign节点,字母升序）+hmac密钥
        sb.append("appCode").append(appCode)
                .append("timestamp").append(req.getTimestamp())
                .append("uuid").append(req.getUuid())
                .append(HMAC);
        String sign = SecurityTool.sign(HMAC, sb.toString());
        return sign;
    }
}
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
/**
 * Created by mall on 2018/4/13.
 */
public class upload {

    private static final String APP_CODE = "2985EF44EC164C949963D0DD07B8070C";

    private static final String URL = " http://cd.10010.com/zop/link//king/card/preOrder/autoNumSync";
    private static final String HMAC = "ywk/W/q0KVp5fMa1tXchymRm91h1WklSOg3hF0YPPQJUg7XCL8NGH00+ZFBj84FOOBvEHYn2Pns1XBoKoqdA3g==";
    private static final String AES = "pIv85I3E98DDJZCjM1ZJPg==";
    @Test
    public void test1() throws Exception {
        JSONObject baseReq = new JSONObject();
        baseReq.put("appCode",APP_CODE);
        ReqObj obj = new ReqObj();
        ReqHeadBean head = new ReqHeadBean();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        head.setTimestamp(formatter.format(new Date()));
        head.setUuid(String.valueOf(UUID.randomUUID()));
        head.setSign(makeSign(head,APP_CODE));
        obj.setHead(head);
        Map body = new HashMap();
//        body.put("msgChannel","hnshhl");
//        body.put("type","3"); //3-获取订单下单消息；4-获取订单状态变更消
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");//设置日期格式
        String date = df.format(System.currentTimeMillis());// new Date()为获取当前系统时间，也可使用当前时间戳
        body.put("orderId","hanlian"+date+"123456");
        body.put("goodsId","981703239395");//商品编码  此处商品ID以98开头
        body.put("certName","毛玉珊");//入网人姓名
        body.put("certNo","610102200408183540");//入网人身份证号码 （身份证中的X要求大写）
        body.put("contactNum","13186029832");//联系电话
        body.put("postProvinceCode","610000");//收货省份编码
        body.put("postCityCode","610400");//收货地市编码
        body.put("postDistrictCode","610402");//收货区县编码
        body.put("postAddr","世纪大道西安技师学院");//详细地址
        body.put("channel","08-2278-a0p5-9397");//触点编码
        body.put("createTime","2020-11-12 17:41:40");//订单创建时间
        body.put("updateTime","2020-11-12 17:41:40");//订单更新时间



        obj.setBody(body);
        //reqObj节点需要加密时
        baseReq.put("reqObj", SecurityTool.encrypt(AES, JSON.toJSONString(obj)));
        System.out.println("请求报文："+ JSON.toJSONString(baseReq));
        MediaType json  = MediaType.parse("application/json; charset=utf-8");
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
        System.out.println("返回报文："+ resStr);
        Map resMap = JSON.parseObject(resStr);
        if ("0000".equals(resMap.get("rspCode"))){
            //成功，处理body里的消息数据
        }else{
            //失败，获取消息数据，做好本地记录，反馈给商城管理员
        }
    }
    public String makeSign (ReqHeadBean req,String appCode) throws Exception {
        StringBuffer sb = new StringBuffer();
        //appCode+head节点（除sign节点,字母升序）+hmac密钥
        sb.append("appCode").append(appCode)
                .append("timestamp").append(req.getTimestamp())
                .append("uuid").append(req.getUuid())
                .append(HMAC);
        String sign = SecurityTool.sign(HMAC,sb.toString());
        return sign;
    }
}
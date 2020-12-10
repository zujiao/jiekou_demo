package hainan_upload;

import com.ai.zop.bean.base.ReqHeadBean;
import com.ai.zop.bean.base.ReqObj;
import com.ai.zop.tool.safe.SecurityTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

//海南接口自动化导数

public class hainan_Automatic_upload {

    private static final String APP_CODE = "2985EF44EC164C949963D0DD07B8070C";

    //自动选号-订单同步接口
    private static final String URL = " http://cd.10010.com/zop/link//king/card/preOrder/autoNumSync";
    private static final String HMAC = "ywk/W/q0KVp5fMa1tXchymRm91h1WklSOg3hF0YPPQJUg7XCL8NGH00+ZFBj84FOOBvEHYn2Pns1XBoKoqdA3g==";
    private static final String AES = "pIv85I3E98DDJZCjM1ZJPg==";

    @Test
    public void test1() {
        ArrayList<String> list = new ArrayList<String>();
        File file = new File("C:\\Users\\范伟\\Desktop\\测试数据_1");
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String str = null;
                try {
                    LineNumberReader reader = null;
                    reader = new LineNumberReader(new FileReader(tempList[i]));
                    while ((str = reader.readLine()) != null) {
//                        System.out.println(str);//输出所有待提交的用户数据
                        String[] split = str.split(",");
                        String goods_code = split[4];//商品编码
                        String name = split[5];//入网人姓名
                        String ID_number = split[6];//入网人身份证号
                        String contact_number = split[0];//联系电话
                        String province_code = split[8];//省份代码
                        String city_code = split[9];//市代码
                        String county_code = split[10];//区县代码
                        String detailed_address = split[7];//详细地址
                        String order_creation_time = split[2];//订单创建时间
                        String order_update_time = split[2];//订单更新时间

//                        System.out.println(goods_code + "," + name + "," + ID_number + "," + contact_number + "," + province_code + "," + city_code + "," + county_code + "," + detailed_address + "," + order_creation_time + "," + order_update_time);

                        //调用联通接口
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
                        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSS");//设置日期格式
                        String date = df.format(System.currentTimeMillis());// new Date()为获取当前系统时间，也可使用当前时间戳
                        body.put("orderId", "hanlian" + date + "123456");
                        body.put("goodsId", goods_code);//商品编码  此处商品ID以98开头
                        body.put("certName", name);//入网人姓名
                        body.put("certNo", ID_number);//入网人身份证号码 （身份证中的X要求大写）
                        body.put("contactNum", contact_number);//联系电话
                        body.put("postProvinceCode", province_code);//收货省份编码
                        body.put("postCityCode", city_code);//收货地市编码
                        body.put("postDistrictCode", county_code);//收货区县编码
                        body.put("postAddr", detailed_address.split("-")[3]);//详细地址
                        body.put("channel", "08-2278-a0p5-9397");//触点编码
                        body.put("createTime", order_creation_time);//订单创建时间
                        body.put("updateTime", order_update_time);//订单更新时间
                        obj.setBody(body);
                        //reqObj节点需要加密时
                        baseReq.put("reqObj", SecurityTool.encrypt(AES, JSON.toJSONString(obj)));
                        //输出请求报文
                        System.out.println(obj.toString());
                        //输出加密后请求报文
                        System.out.println("请求报文：" + JSON.toJSONString(baseReq));


                        //上传数据从这里开始
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

                        //结束

                        //使用FileWriter 将成功的数据和失败的数据分别存入两个文件中,用追加方式写入
                        FileWriter success_data = new FileWriter("C:\\Users\\范伟\\Desktop\\logs\\11_26上传成功数据.txt",true);
                        FileWriter fail_data = new FileWriter("C:\\Users\\范伟\\Desktop\\logs\\11_26上传失败数据.txt",true);

                        Date date1 = new Date();
                        if ("0000".equals(resMap.get("rspCode"))) {
//                            成功，处理body里的消息数据
                        success_data.write("\n");
                        success_data.write(date1.toString());//记录当前时间
                        success_data.write("\n");
                        success_data.write(str+"_____"+"成功返回:"+resMap);
                        success_data.close();
                        } else {
//                            //失败，获取消息数据，做好本地记录，反馈给商城管理员
                        fail_data.write("\n");
                        fail_data.write(date1.toString());//记录当前时间
                        fail_data.write("\n");
                        fail_data.write(str+"_____"+"错误原因:"+resMap);

                        fail_data.close();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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

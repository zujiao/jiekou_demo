package com.ai.zop.hunan_upload;

//import com.unicom.cs.portal.exception.FieldErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class MsgSignatureUtil {
    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        java.security.MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * LindedMap数据签名并返回
     *
     * @param data 待签名数据
     * @return 签名
     * @throws Exception
     */
    private static String generateSignature(LinkedHashMap<String, Object> data, String key) throws Exception, FieldErrorException {
        //为保障排序正确性签名输入数据须为LinkedHashMap
        if (data == null)
            throw new FieldErrorException("input must be LinkedHashMap");
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        try {
            for (String k : keyArray) {
                Object keyCont = data.get(k);
                //值数据类型判读,map型返回消息体，非map型返回
                if (keyCont instanceof Map || keyCont instanceof List) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    sb.append(k).append(objectMapper.writeValueAsString(keyCont));
                } else {
                    sb.append(k).append(keyCont.toString().trim());
                }
            }
            sb.append("key").append(key);
            //MsgSignatureUtil.getLogger().info("拼接后数据值：" + sb.toString());
            return MD5(sb.toString()).toUpperCase();
        } catch (Exception ex) {
            throw new FieldErrorException("Can't parse the msg");
        }
    }

    /**
     * LindedMap数据签名并返回添加签名后消息
     *
     * @param data 待签名数据
     * @return 签名后消息
     * @throws Exception
     */
    private static String generateSignaturedMsg(LinkedHashMap<String, Object> data, String key) throws Exception, FieldErrorException {
        ObjectMapper mapper = new ObjectMapper();
        String sign = generateSignature(data, key);
        data.put("sign", sign);
        return mapper.writeValueAsString(data);
    }

    /**
     * String数据签名并返回添加签名后消息
     *
     * @param data 待签名数据
     * @return 签名后消息
     * @throws Exception
     */
    public static String generateSignaturedMsg(String data, String key) throws Exception, FieldErrorException {
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap linkedHashMap = mapper.readValue(data, LinkedHashMap.class);
        return generateSignaturedMsg(linkedHashMap, key);
    }

    /**
     * LindedMap数据签名验证
     *
     * @param msg 待验证消息
     * @return boolean 签名后消息
     * @throws Exception
     */
    public static boolean vilidateSignature(String msg, String key) throws Exception, FieldErrorException {
        //getLogger().info("原始信息：" + msg);
        ObjectMapper objMapper = new ObjectMapper();
        LinkedHashMap validate = objMapper.readValue(msg, LinkedHashMap.class);
        String sign = (String) validate.get("sign");
        validate.remove("sign");
        System.out.println("签名计算结果：" + generateSignature(validate, key));
        System.out.println("实际签名：" + sign);
        return generateSignature(validate, key).equals(sign);
    }

    public static void main(String[] args) throws Exception, FieldErrorException {
        String msg = "{\"busiSerial\":\"0414310088741787\",\"msg\":{\"province\":\"430000\",\"city\":\"430100\",\"certName\":\"吴余额\",\"certNum\":\"371523199206055312\"},\"timestamp\":1586831008000}";
        System.out.println(MsgSignatureUtil.generateSignaturedMsg(msg, "27205$lq*i0x43^-"));
        boolean b = vilidateSignature(msg, "27205$lq*i0x43^-");
        String s = MD5("{\"busiSerial\":\"0414310088741787\",\"msg\":{\"province\":\"430000\",\"city\":\"430100\",\"certName\":\"吴余额\",\"certNum\":\"371523199206055312\"},\"timestamp\":1586831008000,\"uid\":yxhl}");
        System.out.println(s);

    }
}
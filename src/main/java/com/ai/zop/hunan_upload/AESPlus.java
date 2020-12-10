package com.ai.zop.hunan_upload;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESPlus {
    /**
     * 加密函数（明文不进行base64编码）
     *
     * @param input 待加密消息
     * @param key   加密使用的key
     * @return 密文
     */
    public static String encrypt(String input, String key) {
        byte[] cryptogram = null;
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cryptogram = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new BASE64Encoder().encode(cryptogram);
    }
    /**
     * 加密函数（明文进行base64编码后再进行加密）
     *
     * @param input 待加密消息
     * @param key   加密使用的key
     * @param bs64  是否需进行base64编码
     * @return 密文
     */
    public static String encrypt(String input, String key, Boolean bs64) throws Exception {
        if (bs64) {
            return encrypt(getBASE64(input), key);
        } else {
            return AESPlus.encrypt(input, key);
        }
    }
    /**
     * 加密函数（明文不进行base64编码）
     *
     * @param input 密文
     * @param key   解密使用的key
     * @return 解密后消息串
     * @throws NoSuchPaddingException    padding异常
     * @throws NoSuchAlgorithmException  解密相关异常
     * @throws IllegalBlockSizeException 解密相关异常
     * @throws BadPaddingException       解密相关异常
     * @throws InvalidKeyException       解密相关异常
     * @throws IOException               解密相关异常
     */
    public static String decrypt(String input, String key) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        byte[] output = null;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        output = cipher.doFinal(new BASE64Decoder().decodeBuffer(input));
        return new String(output);
    }
    /**
     * @param input 密文
     * @param key   解密使用的key
     * @param bs64  是否对解密后消息进行base64解码
     * @return 解密码后消息
     */
    public static String decrypt(String input, String key, Boolean bs64) throws NoSuchPaddingException, NoSuchAlgorithmException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        if (bs64) {
            return getFromBASE64(decrypt(input, key));
        } else {
            return decrypt(input, key);
        }
    }
    /**
     * @param s 输入
     * @return base64
     */
    private static String getBASE64(String s) {
        if (s == null)
            return null;
        return (new sun.misc.BASE64Encoder()).encode(s.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * @param s base64消息
     * @return 解码结果
     */
    private static String getFromBASE64(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return null;
        }
    }
    public static void main(String[] args) throws Exception {
        String result = AESPlus.encrypt("{\"busiSerial\":\"0414310088741787\",\"msg\":{\"province\":\"430000\",\"city\":\"430100\",\"certName\":\"吴余额\",\"certNum\":\"371523199206055312\"},\"timestamp\":1586831008000,\"uid\":yxhl}", "27205$lq*i0x43^-", false);
        System.out.println(result);
    }
}
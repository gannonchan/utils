package com.gannon.encrypt;

import com.sun.istack.internal.NotNull;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class EncryptUtil{
    //初始化向量
    private final static byte[] iv = { 0x30, 0x37, 0x58, 0x32, 0x3f, 0x5b
            , 0x7e, 0x34, 0x4d, 0x45, 0x63, 0x36, 0x51, 0x37, 0x7c, 0x38 };
    //十六进制映射数组
    private final static String[] hexDigits = {"0","1","2","3","4","5","6"
            ,"7","8","9","A","B","C","D","E","F"};
    /**
     * 通过AES加密算法进行加密
     * @param sSrc 需要加密的明文
     * @param sKey  需要使用到的密钥
     * @param pattern 加密模式 例: CBC模式AES/CBC/PKCS5Padding、ECB模式AES/ECB/PKCS5Padding"AES算法/ECB模式/填充补码方式"
     * @return 返回一个加密后的密文
     */
    public static String encryprtByAes(@NotNull String sSrc,@NotNull String sKey,@NotNull String pattern) throws Exception{
        if (sKey == null) {
            return "Key为空null";
        }
        // 判断Key是否为16位
        if (sKey.trim().length() != 16) {
            return "Key长度不是16位";
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(pattern);//"AES算法/CBC模式/填充补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        byte temp = 0;
        byte temp1 = 0;
        int lastIndex = encrypted.length-1;
        temp = encrypted[0];
        byte disturb = (byte)(encrypted[0] & encrypted[lastIndex]);
        temp1 = encrypted[1];
        encrypted[1] = (byte)(temp1 - disturb);
        encrypted[0] = encrypted[lastIndex];
        encrypted[lastIndex] = temp;
        //此处使用BASE64做转码功能，同时能起到2次加密的作用。
        String key = Base64.getEncoder().encodeToString(encrypted);
        System.out.println("加密后密文: "+key);
        return key;
    }

    /**
     * AES解密
     * @param sSrc 需要解密的密文
     * @param sKey 使用到的密钥
     * @param pattern 例: CBC模式AES/CBC/PKCS5Padding、ECB模式AES/ECB/PKCS5Padding"AES算法/ECB模式/填充补码方式"
     * @return 解密后的明文
     */
    public static String DecryptByAes(@NotNull String sSrc,@NotNull String sKey,@NotNull String pattern) throws Exception{
        // 判断Key是否正确
        if (sKey == null) {
            return "Key为空null";
        }
        // 判断Key是否为16位
        if (sKey.trim().length() != 16) {
            return "Key长度不是16位";
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(pattern);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(iv));
        byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
        byte temp = 0;
        byte temp1 = 0;
        int lastIndex = encrypted1.length-1;
        temp = encrypted1[0];
        byte disturb = (byte)(encrypted1[0] & encrypted1[lastIndex]);
        temp1 = encrypted1[1];
        encrypted1[1] = (byte)(temp1 + disturb);
        encrypted1[0] = encrypted1[lastIndex];
        encrypted1[lastIndex] = temp;
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original,"utf-8");
        System.out.println("解密后明文: "+originalString);
        return originalString;
    }
    public static String encryptByMessageDigest(@NotNull String src,@NotNull String algorithm) throws Exception{
        if(src.isEmpty()){
            return "请输入需要加密的明文";
        }
        byte[] raw = src.getBytes("utf-8");
        MessageDigest en = MessageDigest.getInstance(algorithm);
        en.update(raw);
        byte[] lock = en.digest();
        return byteArrayToHexString(lock);
    }
    /**
     * 轮换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    //将一个字节转化成十六进制形式的字符串
    private static String byteToHexString(byte b){
        int n = b;
        if(n<0)
            n=256+n;
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }
}

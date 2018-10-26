package com.gannon.encrypt.test;

import com.gannon.encrypt.EncryptUtil;

class EncryptUtilTest{
    public static void main(String[] args) throws Exception{
        String src = "123456790";
        String key ="jbsh1o_3spg9&fsg";
        String pattern ="AES/CBC/PKCS5Padding";
        String s = EncryptUtil.encryprtByAes(src,key,pattern);
        System.out.println(s);
        System.out.println(EncryptUtil.DecryptByAes(s,key,pattern));
        //00FB5AE2C336975F3519C9BBA16BA4C8
        System.out.println(EncryptUtil.encryptByMessageDigest(src,"md5"));

    }
}
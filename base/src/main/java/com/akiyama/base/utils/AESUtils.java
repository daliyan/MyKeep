package com.akiyama.base.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2016/1/3.
 */
public class AESUtils {

    private static final String EncodeAlgorithm = "AES";
    private static final String HEX = "0123456789ABCDEF";
    private static final String DEFALUT_KEY = "mykeep!&*hhjj*jj";

    public static String encryptDefalutKey(String text) throws Exception{
        return encrypt(DEFALUT_KEY,text);
    }


    public static String decryptDefalutKey(String encrypted) throws Exception{
        return decrypt(DEFALUT_KEY,encrypted);
    }


    public static String encrypt(String key, String text) throws Exception{
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] result = encrypt(rawKey, text.getBytes());
        return toHex(result);
    }

    public static String decrypt(String key, String encrypted)
            throws Exception {
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] enc = encrypted.getBytes();
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, EncodeAlgorithm);
        Cipher cipher = Cipher.getInstance(EncodeAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    /**
     * 256的加密密钥
     * @param seed
     * @return
     * @throws Exception
     */
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(EncodeAlgorithm);
        SecureRandom sr = null;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(256, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}

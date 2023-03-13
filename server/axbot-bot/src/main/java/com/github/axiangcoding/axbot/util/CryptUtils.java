package com.github.axiangcoding.axbot.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptUtils {

    /**
     * @param data Base64 编码的数据
     * @param key  encrypt-key
     * @return
     */
    public static String decrypt(String data, String key) {
        // Base64 解码
        String src = new String(Base64.getDecoder().decode(data));
        // 截取 IV
        String iv = src.substring(0, 16);
        // 待解密的密文
        byte[] newSecret = Base64.getDecoder().decode(src.substring(16));
        // Padding
        StringBuilder finalKeyBuilder = new StringBuilder(key);
        while (finalKeyBuilder.length() < 32) {
            finalKeyBuilder.append("\0");
        }
        // 最终 Key
        String finalKey = finalKeyBuilder.toString();

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    new SecretKeySpec(finalKey.getBytes(), "AES"),
                    new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8))
            );
            return new String(cipher.doFinal(newSecret)); // 最终解密结果
        } catch (Exception e) {
            throw new RuntimeException(e); // 这不应该发生，如果它发生了，请检查你的 JVM 安装是否正常！
        }
    }
}
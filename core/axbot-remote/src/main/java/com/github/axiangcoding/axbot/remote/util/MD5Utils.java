package com.github.axiangcoding.axbot.remote.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Utils {
    public static String calculateMD5(String input) {
        try {
            // 创建MessageDigest对象，指定为MD5算法
            MessageDigest digest = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组
            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            // 计算MD5哈希值
            byte[] hashBytes = digest.digest(inputBytes);

            // 将字节数组转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            // 返回MD5哈希值
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.warn("MD5 algorithm not found", e);
            return null;
        }
    }
}
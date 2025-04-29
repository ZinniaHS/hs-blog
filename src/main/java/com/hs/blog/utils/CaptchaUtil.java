package com.hs.blog.utils;

import org.apache.commons.lang3.RandomUtils;

public class CaptchaUtil {
    // 生成6位数字验证码
    public static String generateCaptcha() {
        int code = RandomUtils.nextInt(100000, 999999);
        return String.format("%06d", code);
    }
}

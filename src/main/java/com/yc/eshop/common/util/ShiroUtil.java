package com.yc.eshop.common.util;

import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.UUID;

/**
 * @author 余聪
 * @date 2020/12/4
 */
public class ShiroUtil {
    public static String createSalt() {
        return UUIDUtils.getUUID(5);
    }

    public static String saltEncrypt(String srcPwd, String salt) {
        return new SimpleHash("MD5", srcPwd, salt, 3).toString();
    }
}

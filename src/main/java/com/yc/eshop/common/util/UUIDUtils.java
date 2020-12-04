package com.yc.eshop.common.util;

import java.util.UUID;

/**
 * @author 余聪
 * @date 2020/12/4
 */
public class UUIDUtils {
    /**
     * 32位默认长度的uuid
     * (获取32位uuid)
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * (获取指定长度uuid)
     *
     * @return
     */
    public static String getUUID(int len) {
        if (0 >= len) {
            return null;
        }

        String uuid = getUUID();
        System.out.println(uuid);
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < len; i++) {
            str.append(uuid.charAt(i));
        }

        return str.toString();
    }
}

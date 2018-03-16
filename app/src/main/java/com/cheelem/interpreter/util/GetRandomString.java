package com.cheelem.interpreter.util;

import java.util.Date;
import java.util.Random;

/**
 * Created by 黄宇航 on 2018/3/7.
 */

public class GetRandomString {
    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static String getRandomString(int len) {
        StringBuilder stra = new StringBuilder("");
        Random random = new Random(new Date().getTime());

        for (int i = 0; i < len; i++) {
            stra.append(CHARS[random.nextInt(CHARS.length)]);
        }

        return stra.toString();
    }
}

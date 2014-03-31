package com.p2p.spider.utils;

import java.security.SecureRandom;

/**
 * generate the random data
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-23
 */
public class RandomUtil {
    public static int getInt(int inclusiveMin, int exclusiveMax) {
        if (exclusiveMax <= inclusiveMin) {
            return -1;
        }
        if (exclusiveMax - inclusiveMin == 1) {
            return inclusiveMin;
        }
        SecureRandom r = new SecureRandom();
        int value = r.nextInt(exclusiveMax);
        while (value < inclusiveMin) {
            value = r.nextInt(exclusiveMax);
        }
        return value;
    }
}

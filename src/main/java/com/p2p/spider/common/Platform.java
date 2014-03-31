package com.p2p.spider.common;

/**
 * platform name of the p2p server
 *
 * @author: haibo.lhb@alibaba-inc.com <br/>
 * User: haibo.lhb
 * Date: 14-3-26
 */
public enum Platform {
    FaZhan("fazhan");
    private final String code;

    Platform(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }
}

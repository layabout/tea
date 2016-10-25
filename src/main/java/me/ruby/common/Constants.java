package me.ruby.common;

import me.ruby.common.configuration.ConfigurableConstants;

public class Constants extends ConfigurableConstants {

    static {
        init("setting.properties");
    }

    //token
    public final static String WX_TOKEN = p.getProperty("wx_token");

    //appID
    public final static String WX_APPID = p.getProperty("wx_appId");

    //appsecret
    public final static String WX_APPSECRET = p.getProperty("wx_appsecret");

    //aesKey
    public final static String WX_ENCODING_AESKEY = p.getProperty("wx_encodingAesKey");

    //微信接口调用白名单
    public final static String WX_API_ALLOWED_IP = p.getProperty("wx_api_allowed_ip");
}

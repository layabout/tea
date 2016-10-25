package me.ruby.wechat.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ruby.Constants;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by ruby on 2016/9/21.
 * Email:liyufeng_23@163.com
 */
@Component
@Lazy(false)
public class WXAccessTokenManager {

    private static final Logger logger = LoggerFactory.getLogger(WXAccessTokenManager.class);

    private static String ACCESS_TOKEN = null;

    public static String getAccessToken() {
        if (ACCESS_TOKEN == null) {
            flushAccessToken();
        }
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }

    /**
     * 向微服务器请求accessToken
     * @param appid 第三方用户唯一凭证
     * @param appsecret 第三方用户唯一凭证密钥
     * @return
     */
    private String askAccessToken(String appid, String appsecret) {

        CloseableHttpClient client = HttpClients.createDefault();

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid +"&secret=" + appsecret;
        HttpGet get = new HttpGet(requestUrl);

        try {

            CloseableHttpResponse response = client.execute(get);
            String bodyAsString = EntityUtils.toString(response.getEntity());

            return bodyAsString;

        } catch (IOException e) {
            logger.error("请求微信accessToken失败!");
            e.printStackTrace();

        }

        return null;
    }

    /**
     * 目前公众号accessToken的有效期是2小时
     * 所以每隔1小时就自动获取一次新的accessToken
     * 缓存到本地
     */
    @Scheduled(cron="0 0 0/1 * * ?")
    public void task() {
        logger.info("获取微信accessToken");
        String result = askAccessToken(Constants.WX_APPID, Constants.WX_APPSECRET);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String,String> map = mapper.readValue(result, new TypeReference<Map<String,String>>() { });
            setAccessToken(map.get("access_token"));
        } catch (IOException e) {
            logger.error("解析accessToken错误！");
            e.printStackTrace();
        }
    }

    /**
     * 被动刷新方法
     */
    public static void flushAccessToken() {
        WXAccessTokenManager accessTokenManager = new WXAccessTokenManager();
        accessTokenManager.task();
    }

}

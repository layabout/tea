package me.ruby.common.service.impl;

import me.ruby.common.exception.BusinessException;
import me.ruby.common.service.BaseService;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;

/**
 * Created by ruby on 2016/9/30.
 * Email:liyufeng_23@163.com
 */
public class BaseServceImpl implements BaseService {

    private static final Logger logger = LoggerFactory.getLogger(BaseServceImpl.class);

    @Autowired
    protected TemplateEngine xmlTemplateEngine;


    @Override
    public String sendPostToMgrService(String data) throws Exception {
        return sendPost(data, "Constants.MOBO_ACC_MS_URL", true);
    }

    @Override
    public String sendPost(String data, String url, boolean base64) throws Exception {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);

        logger.trace("请求数据: {}", data);
        if (base64) data = Base64.encodeBase64String(data.getBytes());
        StringEntity entity = new StringEntity(data, "UTF-8");
        post.setEntity(entity);

        return sendHttpRequest(client, post);

    }

    @Override
    public String sendPost(String data, String url) throws Exception {
        return sendPost(data, url, false);
    }

    @Override
    public String sendGet(String url) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        return sendHttpRequest(client, get);
    }

    /**
     * 发送http请求
     * @param client
     * @param request
     * @return
     */
    private String sendHttpRequest(CloseableHttpClient client, HttpRequestBase request) throws Exception {
        try {
            CloseableHttpResponse response = client.execute(request);
            String bodyAsString = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.trace("返回结果: {}", bodyAsString);
            return bodyAsString;
        } catch (IOException e) {
            throw new BusinessException("请求失败", "200");
        } finally {
            client.close();
        }
    }

}

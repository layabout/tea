package me.ruby.tea.wechat.api.service.impl;

import com.alibaba.fastjson.JSON;
import me.ruby.common.exception.BusinessException;
import me.ruby.tea.Constants;
import me.ruby.tea.wechat.api.dto.WXUserInfo;
import me.ruby.tea.wechat.api.service.WXOAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by ruby on 2016/10/20.
 * Email:liyufeng_23@163.com
 */
@Service
public class WXOAuthServiceImpl extends BaseServceImpl implements WXOAuthService {

    private static final Logger logger = LoggerFactory.getLogger(WXOAuthServiceImpl.class);

    @Override
    public String getWebAccessToken(String code) {
        logger.trace("获取微信网页授权access_token");
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constants.WX_APPID + "&secret=" + Constants.WX_APPSECRET + "&code=" + code + "&grant_type=authorization_code";
        try {
            String result = sendGet(url);
            boolean valid = result.indexOf("errcode") == -1;
            logger.trace("是否正确响应: {}", valid);
            if (!valid) result = null;
            return result;

        } catch (Exception e) {
            throw new BusinessException("获取微信网页授权token失败");
        }
    }

    @Override
    public WXUserInfo getUserInfo(String accessToken, String openid) {
        logger.trace("获取微信用户信息");
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid + "&lang=zh_CN";
        try {
            String result = sendGet(url);
            boolean valid = result.indexOf("errcode") == -1;
            logger.trace("是否正确响应: {}", valid);
            if (valid) {
                WXUserInfo userInfo = JSON.parseObject(result, WXUserInfo.class);
                return userInfo;
            }
        } catch (Exception e) {
            throw new BusinessException("获取微信用户信息失败");
        }
        return null;
    }
}

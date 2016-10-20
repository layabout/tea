package me.ruby.tea.wechat.api.service;

import me.ruby.tea.wechat.api.dto.WXUserInfo;

/**
 * Created by ruby on 2016/10/20.
 * Email:liyufeng_23@163.com
 */
public interface WXOAuthService extends BaseService{

    /**
     * 获取网页授权access_token
     * @param code
     * @return
     */
    String getWebAccessToken(String code);

    /**
     * 获取微信用户信息
     * @param accessToken
     * @param openid
     * @return
     */
    WXUserInfo getUserInfo(String accessToken, String openid);
}

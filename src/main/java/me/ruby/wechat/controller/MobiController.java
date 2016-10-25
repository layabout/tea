package me.ruby.wechat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.ruby.wechat.dto.WXUserInfo;
import me.ruby.wechat.service.WXOAuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ruby on 2016/10/19.
 * Email:liyufeng_23@163.com
 */
@Controller
public class MobiController {

    private static final Logger logger = LoggerFactory.getLogger(MobiController.class);

    @Autowired
    private WXOAuthService wxoAuthService;

    @RequestMapping("/mobile")
    public String home(HttpServletRequest request, Model model) throws Exception {
        String code = request.getParameter("code");
        logger.trace("code: {}", code);

        if (StringUtils.isNotBlank(code)) {
            String result = wxoAuthService.getWebAccessToken(code);
            if (result != null) {
                ObjectMapper mapper = new ObjectMapper();
                Map m = mapper.readValue(result, Map.class);
                String accessToken = m.get("access_token").toString();
                String openid = m.get("openid").toString();
                WXUserInfo userInfo = wxoAuthService.getUserInfo(accessToken, openid);
                model.addAttribute("user", userInfo);
            }
        }

        return "mobile/index";
    }

    @RequestMapping("/mobile/merchant")
    public String merchant() throws Exception{

        return "mobile/merchant";
    }
}

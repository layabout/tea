package me.ruby.tea.wechat.api.security;

import me.ruby.common.exception.BusinessException;
import me.ruby.tea.wechat.api.support.ErrorType;
import me.ruby.tea.wechat.api.support.WXBizMsgCrypt;
import me.ruby.tea.wechat.api.support.WXUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * 安全处理
 * Created by ruby on 2016/9/29.
 * Email:liyufeng_23@163.com
 */
@Component
@Aspect
public class SecurityAspect {

    private static final Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

    @Pointcut(value = "execution(* me.ruby.tea.wechat.api.WXServiceAPI.userBind(..))")
    public void securityAspect() {
    }

    @Before("securityAspect()")
    public void doBefore(JoinPoint jp) throws Exception{
        logger.trace("安全处理开始...");

        HttpServletRequest request = null;

        Object[] params = jp.getArgs();
        for(int i = 0; i<params.length; i++) {
            if(params[i] instanceof HttpServletRequest) {
                request = (HttpServletRequest)params[i];
                break;
            }
        }
        String remoteIp = request.getRemoteAddr();
        logger.trace("请求IP地址: {}", remoteIp);

        List<ErrorType> errors = new ArrayList<ErrorType>();

        //加密数据
        String data = request.getParameter("data");
        if (StringUtils.isBlank(data))
            errors.add(ErrorType.missing_param_data);
        //签名
        String signature = request.getParameter("signature");
        if (StringUtils.isBlank(signature))
            errors.add(ErrorType.missing_param_signature);
        //时间戳
        String timestamp = request.getParameter("timestamp");
        if (StringUtils.isBlank(timestamp))
            errors.add(ErrorType.missing_param_timestamp);
        //一次性随机数
        String nonce = request.getParameter("nonce");
        if (StringUtils.isBlank(nonce))
            errors.add(ErrorType.missing_param_nonce);

        if (errors.size() != 0) {
            ErrorType errorType = errors.get(0);
            throw new BusinessException(errorType.getMessage(), errorType.getCode());
        }

        WXBizMsgCrypt wxBizMsgCrypt = WXUtils.getWxBizMsgCryptInstance();
        if (!wxBizMsgCrypt.verifySignature(data, signature, timestamp, nonce)){
            throw new BusinessException("签名验证失败！", "100");
        }

    }
}

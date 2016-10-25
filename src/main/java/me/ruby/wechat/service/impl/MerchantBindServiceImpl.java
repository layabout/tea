package me.ruby.wechat.service.impl;

import me.ruby.common.exception.BusinessException;
import me.ruby.common.service.impl.BaseServceImpl;
import me.ruby.common.util.XmlParseUtil;
import me.ruby.wechat.dto.ApiRespData;
import me.ruby.wechat.service.MerchantBindService;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

/**
 * Created by ruby on 2016/9/30.
 * Email:liyufeng_23@163.com
 */
@Service
public class MerchantBindServiceImpl extends BaseServceImpl implements MerchantBindService {

    @Override
    public ApiRespData<String> userBind(String loginId, String openId, String sendId, String activeCode) throws Exception {

        Context context = new Context();
        context.setVariable("loginId", loginId);
        context.setVariable("openID", openId);
        context.setVariable("sendID", sendId);
        context.setVariable("activeCode", activeCode);

        String reqXml = xmlTemplateEngine.process("32000044", context);

        String result = sendPostToMgrService(reqXml);

        String respCode = XmlParseUtil.getTagValue(result, "respCode");

        ApiRespData<String> apiRespData = new ApiRespData<String>();
        if (respCode.equals("00")) {
            apiRespData.setCode(apiRespData.OK);
            apiRespData.setMessage("商户账号绑定成功！");
        } else {
            apiRespData.setCode(apiRespData.ERROR);
            apiRespData.setMessage("商户账号绑定失败！");
        }

        return apiRespData;
    }

    @Override
    public ApiRespData<String> userUnbind(String openId) throws Exception{
        Context context = new Context();
        context.setVariable("openID", openId);

        String reqXml = xmlTemplateEngine.process("32000045", context);

        String result = sendPostToMgrService(reqXml);

        String respCode = XmlParseUtil.getTagValue(result, "respCode");

        ApiRespData<String> apiRespData = new ApiRespData<String>();
        if (respCode.equals("00")) {
            apiRespData.setCode(apiRespData.OK);
            apiRespData.setMessage("商户账号解绑成功!");
        } else {
            apiRespData.setCode(apiRespData.ERROR);
            apiRespData.setMessage("商户账号解绑失败!");
        }

        return apiRespData;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isBind(String openID) throws Exception {
        Context context = new Context();
        context.setVariable("openID", openID);

        String reqXml = xmlTemplateEngine.process("32000046", context);
        String result = sendPostToMgrService(reqXml);
        String respCode = XmlParseUtil.getTagValue(result, "respCode");

        Boolean isTrue;
        if (respCode.equals("00"))
            isTrue = true;
        else if (respCode.equals("99"))
            isTrue = false;
        else
            throw new BusinessException("查询商户绑定状态发生异常!", "100");

        return isTrue;
    }
}

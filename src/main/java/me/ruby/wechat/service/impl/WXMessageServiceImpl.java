package me.ruby.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import me.ruby.common.service.impl.BaseServceImpl;
import me.ruby.wechat.dto.*;
import me.ruby.wechat.enums.NotificationType;
import me.ruby.wechat.enums.ReceiveType;
import me.ruby.wechat.manager.WXAccessTokenManager;
import me.ruby.wechat.service.MerchantBindService;
import me.ruby.wechat.service.WXMessageService;
import me.ruby.wechat.utils.WXBizMsgCrypt;
import me.ruby.wechat.utils.WXUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruby on 2016/9/26.
 * Email:liyufeng_23@163.com
 */
@Service
public class WXMessageServiceImpl extends BaseServceImpl implements WXMessageService {

    private static final Logger logger = LoggerFactory.getLogger(WXMessageServiceImpl.class);

    @Autowired
    private MerchantBindService merchantBindService;

    //加密模式
    private String encryptType = "";

    //接收方
    private static String TO_USER_NAME;

    //发送方
    private static String FROM_USER_NAME;

    @Override
    public String replyTextMessage(String content) throws Exception {

        String format = "<xml>\n"
                + "<ToUserName><![CDATA[%1$s]]></ToUserName>\n"
                + "<FromUserName><![CDATA[%2$s]]></FromUserName>\n"
                + "<CreateTime>%3$s</CreateTime>\n"
                + "<MsgType><![CDATA[%4$s]]></MsgType>\n"
                + "<Content><![CDATA[%5$s]]></Content>\n"
                + "</xml>";

        // 生成时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());

        String replyXml = String.format(format, TO_USER_NAME, FROM_USER_NAME, timestamp, "text", content);

        if (StringUtils.isNotBlank(getEncryptType()) && getEncryptType().equals("aes")) {

            //生成随机数
            String nonce = WXUtils.getRandomNum(9);
            WXBizMsgCrypt msgCrypt = WXUtils.getWxBizMsgCryptInstance();
            replyXml = msgCrypt.encryptMsg(replyXml, timestamp, nonce);

        }

        logger.trace("回复消息: {}", replyXml);

        return replyXml;
    }

    @Override
    public String sendTemplateMessage(TemplateMessage message) throws Exception {

        //判断通知类型
        //TM001 - 交易通知
        //TM002 - 提现通知
        //TM003 - 转账通知
        String template = message.getTemplate();
        NotificationType notificationType = NotificationType.getNotificationType(template);
        logger.trace(notificationType.getDesc());

        //组装json报文
        String defaultColor = "#173177";
        WXTemplateMessage wxTemplateMessage = new WXTemplateMessage();
        wxTemplateMessage.setTouser(message.getTouser());
        wxTemplateMessage.setTemplate_id(notificationType.getTemplate_id());

        List<TemplateData> list = message.getItems();

        Map<String, WXTemplateData> map = new HashMap<String, WXTemplateData>();

        boolean hasFirst = false;
        boolean hasRemark = false;
        StringBuffer params = new StringBuffer();

        for(TemplateData item : list) {
            if (item.getMark().equals("first")) {
                hasFirst = true;
            }
            if (item.getMark().equals("remark")) {
                hasRemark = true;
            }

            WXTemplateData data = new WXTemplateData();
            data.setValue(item.getValue());
            //没有指定的字体颜色，则使用默认值
            if (StringUtils.isNotBlank(item.getColor()))
                data.setColor(item.getColor());
            else
                data.setColor(defaultColor);

            map.put(item.getMark(), data);

            params.append(item.getMark() + "=" + item.getValue() + "&");
        }

        if (!hasFirst) {
            WXTemplateData first = new WXTemplateData();
            first.setValue(notificationType.getFirst());
            first.setColor(defaultColor);
            map.put("first", first);
        }

        if (!hasRemark) {
            WXTemplateData remark = new WXTemplateData();
            remark.setValue(notificationType.getRemark());
            remark.setColor(defaultColor);
            map.put("remark", remark);
        }

        String url_params = params.substring(0, params.length()-1);
        wxTemplateMessage.setUrl(notificationType.getUrl() + "?params=" + Base64.encodeBase64String(url_params.getBytes()));

        wxTemplateMessage.setData(map);
        //消息组装完毕

        //调用微信服务器接口，发送模板通知消息
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + WXAccessTokenManager.getAccessToken();
        String json = JSON.toJSONString(wxTemplateMessage);

        try {
            return sendPost(json, requestUrl);
        } catch (IOException e) {
            throw new Exception("微信接口调用失败");
        }
    }

    @Override
    public String processor(String receiveXml) throws Exception {

        // 获取消息类型
        String msgType = WXUtils.getTagValue(receiveXml, "MsgType");

        // 设置消息接发方
        TO_USER_NAME = WXUtils.getTagValue(receiveXml, "FromUserName");

        FROM_USER_NAME = WXUtils.getTagValue(receiveXml, "ToUserName");

        if (msgType.equals(ReceiveType.TEXT.toString())) {

            logger.trace("接收文本消息");

            WXReceiveText message = WXUtils.convertToBean(receiveXml, WXReceiveText.class);

            String  content;

            if (message.getContent().contains("你好"))
                content = "您好！";
            else {
                content = "您好！您可以通过以下方式同我们联系：\n" +
                        "【1】登录我们的官方网站：www.mobaopay.com\n" +
                        "【2】选择拨打客服热线：4006771357\n" +
                        "【3】选择登录在线客服：（QQ）4006771357";
            }


            return replyTextMessage(content);

        } else if(msgType.equals(ReceiveType.EVENT.toString())) {

            logger.trace("接收事件推送");

            // 获取事件类型
            String event = WXUtils.getTagValue(receiveXml, "Event");
            // 订阅、取消订阅事件
            if (event.equals(WXSubscribeEvent.SUBSCRIBE) || event.equals(WXSubscribeEvent.UNSUBSCRIBE)) {

                WXSubscribeEvent subscribeEvent = WXUtils.convertToBean(receiveXml, WXSubscribeEvent.class);

                // 订阅事件
                if (subscribeEvent.getEvent().equals(WXSubscribeEvent.SUBSCRIBE)) {

                    logger.trace("订阅事件");
                    String content = "Mo宝支付（www.mobaopay.com）感谢您的关注！\n" +
                            "商务合作、疑问咨询请拨打客服热线：4006771357,或登录在线客服：（QQ）4006771357，我们将为您提供最为专业的第三方支付服务！";

                    return replyTextMessage(content);

                }
            // 模板消息发送任务完成事件
            } else if (event.equals(WXTemplateSendJobFinishEvent.TEMPLATE_SEND_JOB_FINISH)) {

                logger.trace("模板消息推送完成");
                WXTemplateSendJobFinishEvent sendJobFinishEvent = WXUtils.convertToBean(receiveXml, WXTemplateSendJobFinishEvent.class);
                logger.trace("消息{}, 推送状态: {}", sendJobFinishEvent.getMsgId(), sendJobFinishEvent.getStatus());

            } else if (event.equals(WXClickEvent.CLICK)) {

                WXClickEvent clickEvent = WXUtils.convertToBean(receiveXml, WXClickEvent.class);
                String eventKey = clickEvent.getEventKey();
                String openID = clickEvent.getFromUserName();

                logger.trace("菜单点击事件, key值: {}", eventKey);
                if (eventKey.equals("BIND")) {
                    logger.trace("用户绑定操作");
                    String content;
                    boolean isBind = merchantBindService.isBind(openID);
                    if (isBind)
                        content = "您的账号已经绑定,无需再次绑定!";
                    else {
                        WXBizMsgCrypt msgCrypt = WXUtils.getWxBizMsgCryptInstance();
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        String nonce = WXUtils.getRandomNum(6);

                        String encrypt = msgCrypt.encryptParam(clickEvent.getFromUserName());
                        String signature = msgCrypt.signature(encrypt, timestamp, nonce);

                        String params = "data="+encrypt+"&signature="+signature+"&timestamp="+timestamp+"&nonce="+nonce;

                        content = "请点击下面的链接完成绑定:\n\n" +
                                "<a href='https://www.mobaopay.com/m/bind?params="+Base64.encodeBase64String(params.getBytes())+"'>绑定账号</a>";
                    }
                    return replyTextMessage(content);

                } else if (eventKey.equals("UNBIND")) {
                    logger.trace("用户解绑操作");
                    String content = "";
                    boolean isBind = merchantBindService.isBind(openID);
                    if (isBind) {
                        ApiRespData<String> respData = merchantBindService.userUnbind(openID);
                        content = respData.getMessage();
                    } else {
                        content = "您还没有绑定商户账号";
                    }
                    return replyTextMessage(content);
                }
            }
            return "success";

        }
        return "success";
    }

    @Override
    public void setEncryptType(String type) {
        this.encryptType = type;
    }

    @Override
    public String getEncryptType() {
        return this.encryptType;
    }
}

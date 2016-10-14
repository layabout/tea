package me.ruby.tea.wechat.api.service;

import me.ruby.tea.wechat.api.dto.TemplateMessage;

/**
 * 被动消息回复接口
 * Created by ruby on 2016/9/26.
 * Email:liyufeng_23@163.com
 */
public interface WXMessageService extends BaseService {

    /**
     * 回复文本消息
     * @param content 内容
     * @return
     */
    String replyTextMessage(String content) throws Exception;

    /**
     * 发送模板通知
     * @param message
     * @return
     */
    String sendTemplateMessage(TemplateMessage message) throws Exception;

    /**
     * 消息处理器
     * @param receiveXml
     * @throws Exception
     */
    String processor(String receiveXml) throws Exception;

}

package me.ruby.tea.wechat.api.support;

/**
 * 通知类型枚举
 * Created by ruby on 2016/9/28.
 * Email:liyufeng_23@163.com
 */
public enum NotificationType {

    TM001("订单生成通知", "TM001", "bDoDivkZJ2c4J2_YROwaAEJAeoou83sVtFuRTEuXbOA", "https://www.mobaopay.com/m/orderDetail", "您好，您有一笔新的订单。", ""),
    TM002("提现提醒", "TM002", "v2-alQow4lYDcJlpxnRkwcBGPPoTtb1H_XPCpbcOfO8", "https://www.mobaopay.com/m/settDetail", "您好，您已成功申请提现。", "如有疑问，请致电客服热线400-677-1357。"),
    TM003("转账提醒", "TM003", "akp1LZpCTydS9NG23efX6NWecpnTrzG6PICa_ccdfFM", "https://www.mobaopay.com/m/transferDetail", "您好，您有一笔转账提醒。", "如有疑问，请致电客服热线400-677-1357。");

    private String desc;

    private String template;

    private String template_id;

    private String url;

    private String first;

    private String remark;

    NotificationType(String desc, String template, String template_id, String url, String first, String remark) {
        this.desc = desc;
        this.template = template;
        this.template_id = template_id;
        this.url = url;
        this.first = first;
        this.remark = remark;
    }

    public static String getTemplate(String template) {
        for (NotificationType c : NotificationType .values()) {
            if (c.getTemplate().equals(template)) {
                return c.template;
            }
        }
        return null;
    }

    public static NotificationType getNotificationType(String template) {
        for (NotificationType c : NotificationType .values()) {
            if (c.getTemplate().equals(template)) {
                return c;
            }
        }
        return null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return this.template;
    }
}

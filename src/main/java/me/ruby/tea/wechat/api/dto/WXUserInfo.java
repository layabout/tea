package me.ruby.tea.wechat.api.dto;

/**
 * Created by ruby on 2016/10/20.
 * Email:liyufeng_23@163.com
 */
public class WXUserInfo {

    //用户唯一标识
    private String openid;
    //昵称
    private String nickname;
    //性别
    private String sex;
    //省份
    private String province;
    //城市
    private String city;
    //国家
    private String country;
    //头像地址
    private String headimgurl;
    //特权信息
    private String privilege;
    //unionid
    private String unionid;

    public WXUserInfo() {
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}

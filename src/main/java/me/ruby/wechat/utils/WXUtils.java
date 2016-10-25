package me.ruby.wechat.utils;

import me.ruby.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;

/**
 * 微信公众号工具类
 * Created by ruby on 2016/9/21.
 * Email:liyufeng_23@163.com
 */
public class WXUtils {

    private static WXBizMsgCrypt wxBizMsgCrypt = null;

    public static void main(String[] args) {

//        WXReceiveText textMessage = new WXReceiveText();
//        textMessage.setContent("testtt");
//        textMessage.setToUserName("touser");
//        textMessage.setFromUserName("fromuser");
//        textMessage.setMsgType("text");
//
//        try {
//            System.out.println(WXUtils.convertToXml(textMessage));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        String requestIp = "111.226.62.83";
//        String iplist = "\"101.226.62.77\",\"101.226.62.78\",\"101.226.62.79\",\"101.226.62.80\",\"101.226.62.81\",\"101.226.62.82\",\"101.226.62.83\",\"101.226.62.84\",\"101.226.62.85\",\"101.226.62.86\",\"101.226.103.59\",\"101.226.103.60\",\"101.226.103.61\",\"101.226.103.62\",\"101.226.103.63\",\"101.226.103.69\",\"101.226.103.70\",\"101.226.103.71\",\"101.226.103.72\",\"101.226.103.73\",\"140.207.54.73\",\"140.207.54.74\",\"140.207.54.75\",\"140.207.54.76\",\"140.207.54.77\",\"140.207.54.78\",\"140.207.54.79\",\"140.207.54.80\",\"182.254.11.203\",\"182.254.11.202\",\"182.254.11.201\",\"182.254.11.200\",\"182.254.11.199\",\"182.254.11.198\",\"59.37.97.100\",\"59.37.97.101\",\"59.37.97.102\",\"59.37.97.103\",\"59.37.97.104\",\"59.37.97.105\",\"59.37.97.106\",\"59.37.97.107\",\"59.37.97.108\",\"59.37.97.109\",\"59.37.97.110\",\"59.37.97.111\",\"59.37.97.112\",\"59.37.97.113\",\"59.37.97.114\",\"59.37.97.115\",\"59.37.97.116\",\"59.37.97.117\",\"59.37.97.118\",\"112.90.78.158\",\"112.90.78.159\",\"112.90.78.160\",\"112.90.78.161\",\"112.90.78.162\",\"112.90.78.163\",\"112.90.78.164\",\"112.90.78.165\",\"112.90.78.166\",\"112.90.78.167\",\"140.207.54.19\",\"140.207.54.76\",\"140.207.54.77\",\"140.207.54.78\",\"140.207.54.79\",\"140.207.54.80\",\"180.163.15.149\",\"180.163.15.151\",\"180.163.15.152\",\"180.163.15.153\",\"180.163.15.154\",\"180.163.15.155\",\"180.163.15.156\",\"180.163.15.157\",\"180.163.15.158\",\"180.163.15.159\",\"180.163.15.160\",\"180.163.15.161\",\"180.163.15.162\",\"180.163.15.163\",\"180.163.15.164\",\"180.163.15.165\",\"180.163.15.166\",\"180.163.15.167\",\"180.163.15.168\",\"180.163.15.169\",\"180.163.15.170\",\"101.226.103.0\\/25\",\"101.226.233.128\\/25\",\"58.247.206.128\\/25\",\"182.254.86.128\\/25\",\"103.7.30.21\",\"103.7.30.64\\/26\",\"58.251.80.32\\/27\",\"183.3.234.32\\/27\",\"121.51.130.64\\/27\"";
//        System.out.println(validateWeChatIP(requestIp, iplist));

        try {
            WXBizMsgCrypt wxBizMsgCrypt = getWxBizMsgCryptInstance();
            String encryptParam = wxBizMsgCrypt.encryptParam("test");
            System.out.println(encryptParam);
            System.out.println("解密"+ wxBizMsgCrypt.decryptParam("bL4hxW05jxIBMxhDkoGiVL6nHcVffSEYeToFFgBzJ1IEbMetq3VlYSVZ4Q8SfJaJ5PjO4iI+33KMewR0v4LqE09oBbIpmXBoWcXY4CuH1vnbPYMP1voRBf1ixwyuvr0j"));

            String timestamp = String.valueOf(System.currentTimeMillis());
            String nonce = WXUtils.getRandomNum(6);

            String encyp = wxBizMsgCrypt.encryptParam("faye");
            String sign = wxBizMsgCrypt.signature(encyp,timestamp,nonce);
            System.out.println("密文"+encyp);
            System.out.println("签名"+sign);
            System.out.println(timestamp);
            System.out.println(nonce);
            System.out.println("解密"+ wxBizMsgCrypt.decryptParam(encyp, sign, timestamp, nonce));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定节点值
     * @param xmltext
     * @param tagName
     * @return
     * @throws Exception
     */
    public static String getTagValue(String xmltext, String tagName) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(xmltext);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist = root.getElementsByTagName(tagName);

        String value = nodelist.item(0).getTextContent();

        return value;
    }

    public static WXBizMsgCrypt getWxBizMsgCryptInstance() throws Exception {
        if (wxBizMsgCrypt == null) {
            wxBizMsgCrypt = new WXBizMsgCrypt(Constants.WX_TOKEN, Constants.WX_ENCODING_AESKEY, Constants.WX_APPID);
        }
        return wxBizMsgCrypt;
    }

    /**
     * 使用JAXB把xml转换为bean
     * @param xmltext
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToBean(String xmltext, Class<T> clazz) throws Exception {
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller un = context.createUnmarshaller();
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmltext));
        return (T)un.unmarshal(reader);
    }

    /**
     * JavaBean转换成xml
     * 默认编码UTF-8
     * @param obj
     * @return
     */
    public static String convertToXml(Object obj) throws Exception {
        return convertToXml(obj, "UTF-8");
    }

    /**
     * JavaBean转换成xml
     * @param obj
     * @param encoding
     * @return
     */
    public static String convertToXml(Object obj, String encoding) throws Exception{
        String result = null;

        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        result = writer.toString();

        return result;
    }

    /**
     * 生成指定位数的随机数
     * @param numLeng
     * @return
     */
    public static String getRandomNum(int numLeng) {

        // 35是因为数组是从0开始的，26个字母+10个数字
        final int maxNum = 36;

        int i; // 生成的随机数

        int count = 0;

        char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        StringBuffer StrSB = new StringBuffer();

        Random r = new Random();

        while (count < numLeng) {
            // 生成随机数，取绝对值，防止生成负数，

            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1

            if (i >= 0 && i < str.length) {
                StrSB.append(str[i]);
                count++;
            }
        }
        return StrSB.toString();
    }

}

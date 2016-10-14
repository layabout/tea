package me.ruby.common.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruby on 2016/5/25.
 * Email:liyufeng_23@163.com
 */
public class XmlParseUtil {
    /**
     * 解析xml数据，将xml数据解析好后放入map中，目前只支持元素名不重复的xml数据 取数据时直接中map中根据key值取值。
     * key采用元素路径的方式.元素用类似/root/element/data的路径形式，属性值用root/element/data/@id的形式，id是属性名称。
     *
     * @param xmlData
     * @throws Exception
     */
    public static void parse(String xmlData, Map<String, String> resultMap)
            throws Exception {
        try {
            Document doc = DocumentHelper.parseText(xmlData);
            Element root = doc.getRootElement();// 指向根节点
            parseNode(root, resultMap);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 递归方式解析所有数据
     *
     * @param node
     *            节点
     * @param resultMap
     *            存放结果的map
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private static void parseNode(Element node, Map<String, String> resultMap)
            throws Exception {
        try {
            List attList = node.attributes();
            List eleList = node.elements();
            for (int i = 0; i < attList.size(); i++) {
                Attribute att = (Attribute) attList.get(i);
                resultMap.put(att.getPath(), att.getText().trim());
            }
            resultMap.put(node.getPath(), node.getText().trim());
            Element element = null;
            for (int i = 0; i < eleList.size(); i++) {
                element = (Element) eleList.get(i);
                if (element.getName().contains("List")) {
                    attList = element.attributes();
                    for (int j = 0; j < attList.size(); j++) {
                        Attribute att = (Attribute) attList.get(j);
                        resultMap.put(att.getPath(), att.getText().trim());
                    }
                    resultMap.put(element.getPath(), element.asXML());
                } else {
                    parseNode((Element) eleList.get(i), resultMap);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @author liuhang
     * @param e
     *            传入的父级element对象
     * @param element_name
     *            element节点名
     * @return returnElement 返回给定节点名的element子节点
     */
    public static Element getChildElement(Element e, String element_name) {
        Element returnElement = null;
        List<Element> el_list = e.elements();
        if (el_list != null && !el_list.isEmpty()) {
            for (Element tempelement : el_list) {
                if (tempelement.getName().equals(element_name)) {
                    returnElement = tempelement;
                    break;
                }
            }
        }
        return returnElement;
    }

    /**
     * 把XML解析为MAP
     * @param node
     * @return
     */
    public static Map<String, Object> xml2Map(Element node) {
        return getNodes(node);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getNodes(Element node) {
        Map<String, Object> map = new HashMap<String, Object>();

        List<Element> children = node.elements();
        if (children.size() > 0) {

            for (Element e : children) {
                if (e.elements().size() > 0) {
                    List<Attribute> listAttr = e.attributes();
                    if (listAttr.size() > 0) {
                        Map<String, Object> temp = (HashMap) getNodes(e);
                        for (Attribute attr : listAttr) {
                            temp.put("_" + attr.getName(), attr.getValue());
                        }
                        map.put(e.getName(), temp);
                    } else {
                        map.put(e.getName(), getNodes(e));
                    }

                } else {

                    List<Attribute> listAttr = e.attributes();
                    Map<String, Object> temp = new HashMap<String, Object>();
                    if (listAttr.size() > 0) {
                        for (Attribute attr : listAttr) {
                            temp.put("_" + attr.getName(), attr.getValue());
                        }
                        if (e.hasContent())
                            temp.put("#text", e.getTextTrim());

                        List list = null;
                        if (map.containsKey(e.getName())) {

                            if (map.get(e.getName()) instanceof ArrayList) {
                                list = (ArrayList) map.get(e.getName());
                                list.add(temp);
                            } else {
                                list = new ArrayList();
                                list.add(map.get(e.getName()));
                                list.add(temp);
                            }

                            map.put(e.getName(), list);
                        } else {
                            map.put(e.getName(), temp);
                        }
                    } else {
                        map.put(e.getName(), e.getTextTrim());
                    }

                }

            }

        } else {
            map.put(node.getName(), node.getTextTrim());
        }

        return map;
    }

    public static Map<String, Object> xml2Map(String xmlData) throws Exception {
        Document doc = DocumentHelper.parseText(xmlData);
        Element root = doc.getRootElement().element("respData");// 指向根节点
        Map<String, Object> res = getNodes(root);
        return res;
    }

    /**
     * xml转换为list对象
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     * @throws Exception
     */
    public static  <T> List<T> xml2List(String xml, Class clazz) throws Exception {
        Document doc = DocumentHelper.parseText(xml);
        Element node = doc.getRootElement().element("respData");
        if ("00".equals(node.element("respCode").getText())) {
            List<Element> list = node.element("resultData").elements();
            List<T> resList = new ArrayList<T>(list.size());
            for (Element e : list) {
                T t = (T) clazz.newInstance();
                for (Object o : e.elements()) {
                    Element _e = (Element) o;
                    String name = _e.getName().trim();
                    try {
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        String _t = _e.getText();
                        if (!StringUtils.isEmpty(_t) && !"null".equals(_t)) BeanUtils.copyProperty(t, name, _t);
                    } catch (Exception e1) {
                    }
                }
                resList.add(t);
            }
            return resList;
        } else {
            throw new Exception(node.element("respDesc").getText());
        }
    }

    /**
     * 获取指定节点值
     * @param xmlData
     * @param tagName
     * @return
     * @throws Exception
     */
    public static String getTagValue(String xmlData, String tagName) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(xmlData);
        InputSource is = new InputSource(sr);
        org.w3c.dom.Document document = db.parse(is);

        org.w3c.dom.Element root = document.getDocumentElement();
        NodeList nodelist = root.getElementsByTagName(tagName);

        String value = nodelist.item(0).getTextContent();

        return value;
    }

}

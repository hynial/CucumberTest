package com.hynial.wechat.entity;

import com.hynial.cucumber.util.CommonUtil;
import com.hynial.wechat.entity.extract.iextract.IExtractor;
import com.hynial.wechat.entity.extract.impl.XpathExtractor;
import com.hynial.wechat.entity.search.XpathInfo;
import io.appium.java_client.MobileElement;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class WechatInfo {
    public static String ALIAS_NAME = "ALIAS_NAME";
    public static String ALIAS_VALUE = "ALIAS_VALUE";
    public static String ALIAS_EXTRACT_OBJ = "ALIAS_EXTRACT_OBJ";
//    public static String ALIAS_PAGE = "ALIAS_PAGE";

    public static String WECHAT_ID_ALIAS = "wechat id";
    public static String NICKNAME_ALIAS = "nickname";
    public static String SEX_ALIAS = "sex";
    public static String REMARK_ALIAS = "remark";
    public static String LOCATION_ALIAS = "location";
    public static String PERSONAL_SIGN_ALIAS = "personal sign";
    public static String MOBILE_ALIAS = "mobile";
    public static String DESCRIPTION_ALIAS = "description";

    /*private List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>() {{
        add(Collections.singletonMap(ALIAS_NAME, "wechat id")); // 微信号
        add(Map.ofEntries(Map.entry(ALIAS_NAME, "nickname"))); // 昵称
        add(Map.of(ALIAS_NAME, "location")); // 地区
        add(new HashMap<String, Object>() {{
            put(ALIAS_NAME, "sex"); // 性别
        }});

    }};*/

    private Map<String, Object> createMap(){
        return new HashMap<>();
    }
    private Map<String, Object> createXpathMap(String xpath, boolean isList, boolean isSearchById, String attr){
        Map<String, Object> map =  new HashMap<>();
        map.put(ALIAS_EXTRACT_OBJ, new XpathExtractor(new XpathInfo(xpath, isList, isSearchById).setExtractAttribute(attr)));
        return map;
    }

    private Map<String, Object> createXpathIdMap(String xpath){
        return createXpathMap(xpath, false, true, "text");
    }

    private Map<String, Object> createXpathAttrIdMap(String xpath, String attr){
        return createXpathMap(xpath, false, true, attr);
    }

    private Map<String, Map<String, Object>> fieldMap = new HashMap<>(){{
        put(WECHAT_ID_ALIAS,createXpathIdMap("com.tencent.mm:id/bd_"));
        put(NICKNAME_ALIAS, createXpathIdMap("com.tencent.mm:id/bd1"));
        put(LOCATION_ALIAS, createXpathIdMap("com.tencent.mm:id/bd0"));
        put(SEX_ALIAS, createXpathAttrIdMap("com.tencent.mm:id/bd7", "content-desc"));

        put(MOBILE_ALIAS, createXpathMap("//android.widget.TextView[@text='电话号码']/following-sibling::android.widget.LinearLayout/android.widget.TextView", true, false, "text"));

        put(REMARK_ALIAS, createXpathIdMap("com.tencent.mm:id/bb4")); // 备注
        put(PERSONAL_SIGN_ALIAS, createXpathMap("//android.widget.TextView[@text='个性签名']/../android.widget.TextView[last()]", false, false, "text")); // 个性签名
        put(DESCRIPTION_ALIAS, createXpathIdMap("com.tencent.mm:id/bba")); // 备注
    }};

    public void clickRightTopMore(){
        new XpathExtractor(new XpathInfo("com.tencent.mm:id/d8", false, true)).click();
    }

    public void clickRemarkTagFromDataSetting(){
        new XpathExtractor(new XpathInfo("//android.widget.TextView[@text='设置备注和标签']", false, false)).click();
    }

    public void clickMoreInfo(){
        new XpathExtractor(new XpathInfo("//android.widget.TextView[@text='更多信息']", false, false)).click();
    }

    public boolean isExistTopMoreButton(){
        return isElementExists(new XpathInfo("com.tencent.mm:id/d8", false, true));
    }

    public boolean isContactPage(){
//        String title = (String) new XpathExtractor(new XpathInfo("android:id/text1", false, true)).execute().getValue();
//        return "通讯录".equals(title);

        return isElementExists(new XpathInfo("//android.widget.TextView[@text='通讯录' and @resource-id='android:id/text1']", false, false));
    }

    public void setPersonalInfoPage(){
        setAliasExecutedValue(WECHAT_ID_ALIAS);
        setAliasExecutedValue(NICKNAME_ALIAS);
        setAliasExecutedValue(LOCATION_ALIAS);
        setAliasExecutedValue(SEX_ALIAS);
        setAliasExecutedValue(MOBILE_ALIAS);
    }

    public void setRemarkTagPage(){
        setAliasExecutedValue(REMARK_ALIAS);
        setAliasExecutedValue(DESCRIPTION_ALIAS);
    }

    public void setMoreInfoPage(){
        setAliasExecutedValue(PERSONAL_SIGN_ALIAS);
    }

    public boolean isElementExists(XpathInfo xpathInfo){
        return new XpathExtractor(xpathInfo).isElementExists();
    }

    public WechatInfo() {
        initial();
    }

    void initial(){
        this.fieldMap.forEach((x,y) -> {
            y.put(ALIAS_NAME, x);
        });
    }

    public Object getObjectByAlias(String alias){
        return this.fieldMap.get(alias).get(ALIAS_VALUE);
    }

    public String getStringByAlias(String alias){
        return getObjectByAlias(alias) == null ? null : getObjectByAlias(alias).toString();
    }

    public List<Object> getListObjectByAlias(String alias){
        Object value = getObjectByAlias(alias);
        if(value == null) return null;
        if(value instanceof List<?>){
            return (List<Object>) value;
        }else{
            throw new RuntimeException("NotAList!");
        }
    }

    public List<String> getListStringByAlias(String alias){
        return getListObjectByAlias(alias).stream().map(o -> (o == null ? "" : o.toString())).collect(Collectors.toList());
    }

    public void setAliasValue(String alias, Object obj){
        this.fieldMap.get(alias).put(ALIAS_VALUE, obj);
    }

    public IExtractor getExtractorByAlias(String alias){
        return (IExtractor) this.fieldMap.get(alias).get(ALIAS_EXTRACT_OBJ);
    }

    public void setAliasExecutedValue(String alias){
        setAliasValue(alias, getExtractorByAlias(alias).execute().getValue());
        CommonUtil.sleep(250);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map<String, Object> fieldMapValue : this.fieldMap.values()){
            stringBuilder.append(fieldMapValue.get(ALIAS_NAME)).append(":");
            stringBuilder.append(fieldMapValue.get(ALIAS_VALUE) == null ? "" : (fieldMapValue.get(ALIAS_VALUE) instanceof List<?> ? ((List)fieldMapValue.get(ALIAS_VALUE)).stream().map(String::valueOf).collect(Collectors.joining(",")): fieldMapValue.get(ALIAS_VALUE).toString())).append("\n");
        }

        return stringBuilder.toString();
    }

}

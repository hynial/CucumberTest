package com.hynial.wechat.entity;

import com.hynial.wechat.entity.extract.iextract.IExtractor;
import com.hynial.wechat.entity.extract.impl.XpathExtractor;
import com.hynial.wechat.entity.search.XpathInfo;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class WechatInfo {
    public static String ALIAS_NAME = "ALIAS_NAME";
    public static String ALIAS_VALUE = "ALIAS_VALUE";
    public static String EXTRACT_OBJ = "EXTRACT_OBJ";

    public static String WECHAT_ID_ALIAS = "wechat id";
    public static String NICKNAME_ALIAS = "nickname";
    public static String SEX_ALIAS = "sex";
    public static String REMARK_ALIAS = "remark";
    public static String LOCATION_ALIAS = "location";
    public static String PERSONAL_SIGN_ALIAS = "personal sign";

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
        map.put(EXTRACT_OBJ, new XpathExtractor(new XpathInfo(xpath, isList, isSearchById).setExtractAttribute(attr)));
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
        put(LOCATION_ALIAS, createMap());
        put(SEX_ALIAS, createXpathAttrIdMap("com.tencent.mm:id/bd7", "content-desc"));
        put(REMARK_ALIAS, createXpathIdMap("com.tencent.mm:id/bbc")); // 备注
        put(PERSONAL_SIGN_ALIAS, createMap()); // 个性签名
    }};

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
        return (IExtractor) this.fieldMap.get(alias).get(EXTRACT_OBJ);
    }

    public void setAliasExecutedValue(String alias){
        setAliasValue(alias, getExtractorByAlias(alias).execute().getValue());
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map<String, Object> fieldMapValue : this.fieldMap.values()){
            stringBuilder.append(fieldMapValue.get(ALIAS_NAME)).append(":").append(fieldMapValue.get(ALIAS_VALUE) == null ? "" : fieldMapValue.get(ALIAS_VALUE).toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}

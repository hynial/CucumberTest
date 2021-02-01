package com.hynial.wechat.entity;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class WechatInfo {
    public static String ALIAS_NAME = "ALIAS_NAME";
    public static String ALIAS_VALUE = "ALIAS_VALUE";

    /*private List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>() {{
        add(Collections.singletonMap(ALIAS_NAME, "wechat id")); // 微信号
        add(Map.ofEntries(Map.entry(ALIAS_NAME, "nickname"))); // 昵称
        add(Map.of(ALIAS_NAME, "location")); // 地区
        add(new HashMap<String, Object>() {{
            put(ALIAS_NAME, "sex"); // 性别
        }});

    }};*/

    private Map<String, Map<String, Object>> fieldMap = new HashMap<>(){{
        put("wechat id", new HashMap<>());
        put("nickname", new HashMap<>());
        put("location", new HashMap<>());
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
        Object value = getObjectByAlias(alias);
        if(value == null) return null;
        if(value instanceof List<?>){
            return ((List<Object>) value).stream().map(o -> (o == null ? "" : o.toString())).collect(Collectors.toList());
        }else{
            throw new RuntimeException("NotAList!");
        }
    }

    public void setAliasValue(String alias, Object obj){
        this.fieldMap.get(alias).put(ALIAS_VALUE, obj);
    }
}

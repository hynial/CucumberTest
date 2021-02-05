package com.hynial.cucumber.biz.exp;

import com.hynial.cucumber.util.BizUtil;
import com.hynial.cucumber.util.CommonUtil;
import com.hynial.wechat.entity.WechatInfo;

import java.util.List;
import java.util.Map;

public class Map2csv {
    public String map2csv(Map<String, WechatInfo> map) {
        String result = "";
        for (WechatInfo wechatInfo : map.values()) {
            result += wechatInfo2String(wechatInfo) + "\n";
        }
        return result;
    }

    public String wechatInfo2String(WechatInfo wechatInfo) {
        if(wechatInfo == null) return "";
        String line = "";
        List<String> titles = BizUtil.getCsvTitles();
        Map<String, List<String>> titleMap = BizUtil.getHeadMap();
        List<String> mergeFields = BizUtil.getMergeFields();
        for (int i = 0; i < titles.size(); i++) {
            String pureTitle = titles.get(i).replaceAll(" \\d+", "");
            int ind = -1;
            String indStr = titles.get(i).replaceAll(pureTitle, "").trim();
            if(!CommonUtil.isEmpty(indStr)){
                ind = Integer.parseInt(indStr);
            }
            List<String> toTitles = titleMap.get(pureTitle);
            if(toTitles == null || toTitles.size() == 0){
                line += ",";
            }else{
                String vString = "";

                for(String t : toTitles){
                    Object v = wechatInfo.getObjectByAlias(t);
                    if(v instanceof String){
                        if(!mergeFields.contains(pureTitle)) {
                            vString = (String) v;
                            break;
                        }else{
                            vString += (String) v + "/";
                        }
                    }else if(v instanceof List<?>){
                        List<String> vList = (List<String>) v;
                        if(vList == null || vList.size() == 0){
                            continue;
                        }else{
                            if(ind - 1 < vList.size()){
                                if(!mergeFields.contains(pureTitle)) {
                                    vString = vList.get(ind - 1);
                                    break;
                                }else{
                                    vString += vList.get(ind - 1) + "/";
                                }
                            }
                        }
                    }else{
                        continue;
                    }
                }

                if ("Last Name".equals(pureTitle)) {
                    if(vString.length() > 0) {
                        vString = vString.substring(0, 1);
                    }
                }
                if ("Wechat".equals(pureTitle)) {
                    vString = vString.replaceAll("微信号:  ", "");
                }
                if ("Nickname".equals(pureTitle)) {
                    vString = vString.replaceAll("昵称:  ", "");
                }
                if ("Notes".equals(pureTitle)) {
                    vString = vString.replaceAll("添加更多备注信息", "")
                            .replaceAll("昵称:  ", "")
                            .replaceAll("地区:  ", "");
                    vString = "\"" + vString + "\"";
                }
                line += vString + ",";
            }

        }

        return line.substring(0, line.length() - 1);
    }
}

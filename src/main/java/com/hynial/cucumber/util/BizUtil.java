package com.hynial.cucumber.util;

import com.hynial.cucumber.config.ConfigLoader;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BizUtil {
    public static String REG_MAP_ALIAS = "\\((.*)\\)\\s*$";
    public static List<String> getCsvTitles(){
        return getHeadTitlesMap().keySet().stream().collect(Collectors.toList());
    }

    public static String[] getHeadTitles() {
        String[] headTitles = Arrays.stream(ConfigLoader.getInstance().getPropertyUtil().getString("headMapTitles").split(",")).map(title -> title.trim()).toArray(String[]::new);
        return headTitles;
    }

    public static Map<String, List<String>> getHeadMap(){
        Map<String, List<String>> matchMap = new HashMap<>();
        String regMapValue = REG_MAP_ALIAS;
        for(String titleMap : getHeadTitles()){
            Pattern pattern = Pattern.compile(regMapValue);
            Matcher matcher = pattern.matcher(titleMap);
            if(matcher.find()){
                matchMap.put(titleMap.replaceAll(regMapValue, "").replaceAll("_\\d+", ""), Arrays.stream(matcher.group(1).split("/")).collect(Collectors.toList()));
            }
        }

        return matchMap;
    }

    public static List<String> getMergeFields(){
        return Arrays.stream(ConfigLoader.getInstance().getPropertyUtil().getString("mergeFields").split(",")).collect(Collectors.toList());
    }

    public static Map<String, Integer> getHeadTitlesMap() {
        Map<String, Integer> headMap = null;
        String[] titles = getHeadTitles();
        if (titles != null && titles.length > 0) {
            headMap = new LinkedHashMap<>();
        }

        int j = 0;
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            title = title.replaceAll(REG_MAP_ALIAS,"");
            if (title.indexOf("_") > -1) {
                String[] titleIndex = title.split("_");
                int c = Integer.parseInt(titleIndex[1]);
                for (int t = 0; t < c; t++) {
                    headMap.put(titleIndex[0] + " " + (t + 1), j++);
                }
            } else {
                headMap.put(title, j++);
            }
        }

        return headMap;
    }

    public static void main(String[] args) {
        getHeadTitles();
    }
}

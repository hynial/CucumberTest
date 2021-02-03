package com.hynial.wechat.entity.extract.impl;

import com.hynial.factory.AppiumFactory;
import com.hynial.wechat.entity.extract.iextract.IExtractor;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class RegexExtractor implements IExtractor<String> {
    private String regex;
    private String result;

    public RegexExtractor(String regex) {
        this.regex = regex;
    }

    @Override
    public String getValue() {
        return regex;
    }

    @Override
    public void extract(){
        Pattern pattern = Pattern.compile(this.regex);
        Matcher matcher = pattern.matcher(AppiumFactory.getDriver().getPageSource());
        if(matcher.find()){
            this.result = matcher.group(1);
        }
    }
}

package com.hynial.wechat.entity.search;

import io.appium.java_client.MobileElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@Builder
public class XpathInfo {
    private String xpath;
    private boolean isList = false;
    private boolean searchById = true;

    public XpathInfo(String xpath) {
        this.xpath = xpath;
    }

    public XpathInfo setList(boolean isList){
        this.isList = isList;
        return this;
    }

    public XpathInfo setSearchById(boolean searchById){
        this.searchById = searchById;
        return this;
    }

    public XpathInfo(String xpath, boolean isList, boolean searchById) {
        this.xpath = xpath;
        this.isList = isList;
        this.searchById = searchById;
    }

    private MobileElement element;
    private List<MobileElement> elementList;

    private String extractAttribute;

    public XpathInfo setExtractAttribute(String extractAttribute){
        this.extractAttribute = extractAttribute;
        return this;
    }

}

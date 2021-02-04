package com.hynial.wechat.entity.extract.impl;

import com.hynial.factory.AppiumFactory;
import com.hynial.wechat.entity.extract.iextract.IExtractor;
import com.hynial.wechat.entity.search.XpathInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.Data;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

@Data
public class XpathExtractor implements IExtractor<String> {

    public XpathExtractor(XpathInfo xpathInfo) {
        this.xpathInfo = xpathInfo;
    }

    private XpathInfo xpathInfo;
    private AppiumDriver driver = AppiumFactory.getDriver();

    @Override
    public void extract() {
        // PageFactory.initElements(driver, this); // TODO
        try {
            if (xpathInfo.isSearchById()) {
                xpathInfo.setElement((MobileElement) driver.findElementById(xpathInfo.getXpath()));
            } else {
                if (xpathInfo.isList()) {
                    xpathInfo.setElementList(driver.findElementsByXPath(xpathInfo.getXpath()));
                } else {
                    xpathInfo.setElement((MobileElement) driver.findElementByXPath(xpathInfo.getXpath()));
                }
            }
        } catch (NoSuchElementException e){
            e.printStackTrace();
            //throw new RuntimeException(e); // set a field to change the behavior? TODO
        }
    }

    public List<MobileElement> getElements(){
        return xpathInfo.getElementList();
    }

    public MobileElement getElement(){
        return xpathInfo.getElement();
    }

    public String getValue(){
        return "text".equals(xpathInfo.getExtractAttribute()) ? xpathInfo.getElement().getText() : xpathInfo.getElement().getAttribute(xpathInfo.getExtractAttribute());
    }
}

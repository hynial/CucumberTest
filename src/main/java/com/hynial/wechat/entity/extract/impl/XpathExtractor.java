package com.hynial.wechat.entity.extract.impl;

import com.hynial.factory.AppiumFactory;
import com.hynial.wechat.entity.extract.iextract.IExtractor;
import com.hynial.wechat.entity.search.XpathInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class XpathExtractor implements IExtractor<Object> {

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
        } catch (WebDriverException webDriverException){

        }
    }

    public List<MobileElement> getElements(){
        return xpathInfo.getElementList();
    }

    public MobileElement getElement(){
        return xpathInfo.getElement();
    }

    public Object getValue(){
        if(!getXpathInfo().isList()) {
            if(xpathInfo.getElement() == null) return "";
            return "text".equals(xpathInfo.getExtractAttribute()) ? xpathInfo.getElement().getText() : xpathInfo.getElement().getAttribute(xpathInfo.getExtractAttribute());
        }else{
            if(getElements() == null) return Collections.emptyList();
            List<String> valueList = new ArrayList<>();
            for(MobileElement e : getElements()){
                if("text".equals(xpathInfo.getExtractAttribute())){
                    valueList.add(e.getText());
                }else{
                    valueList.add(e.getAttribute(xpathInfo.getExtractAttribute()));
                }
            }

            return valueList;
        }
    }

    @Override
    public void click(){
        try {
            if (xpathInfo.isSearchById()) {
                driver.findElementById(xpathInfo.getXpath()).click();
            } else {
                if (xpathInfo.isList()) {
                    ((List<MobileElement>) driver.findElementsByXPath(xpathInfo.getXpath())).get(0).click();
                } else {
                    driver.findElementByXPath(xpathInfo.getXpath()).click();
                }
            }
        } catch (NoSuchElementException e){
            e.printStackTrace();
            //throw new RuntimeException(e); // set a field to change the behavior? TODO
        } catch (WebDriverException webDriverException){

        }
    }

    @Override
    public boolean isElementExists(){
//        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        boolean exists = driver.findElements(xpathInfo.isSearchById() ? By.id(xpathInfo.getXpath()) : By.xpath(xpathInfo.getXpath())).size() != 0;
//        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return exists;
    }

}

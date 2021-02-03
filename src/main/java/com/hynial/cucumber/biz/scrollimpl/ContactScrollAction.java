package com.hynial.cucumber.biz.scrollimpl;

import com.hynial.cucumber.biz.iscroll.AbstractScrollAction;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.NoSuchElementException;

import java.util.List;

public class ContactScrollAction extends AbstractScrollAction {
    private int loopIndex = 0;
    public ContactScrollAction(AppiumDriver driver, int x1, int y1, int x2, int y2) {
        super(driver, x1, y1, x2, y2);
    }

    @Override
    public void isScrollEnd() {
        try {
            MobileElement bottomElement = (MobileElement) driver.findElementById("com.tencent.mm:id/ba5");
            if (bottomElement != null) {
                String totalText = bottomElement.getText();
                if (totalText != null) {
                    System.out.println(totalText);
                    super.scrollEnd = true;
                }
            }
        } catch (NoSuchElementException noSuchElementException) {

        }
    }

    @Override
    public void scrollAction() {
        List<MobileElement> contactsLinearList = driver.findElementsByXPath(
                "//android.widget.ListView[@resource-id='com.tencent.mm:id/h4']/android.widget.LinearLayout");

        if (contactsLinearList == null || contactsLinearList.size() == 0) return;
        // 跳过第一块标签
        for (int i = (loopIndex == 0 ? 1 : 0); i < contactsLinearList.size(); i++) {
            MobileElement linearElement = contactsLinearList.get(i);
            linearElement.click();

            List<MobileElement> telephoneElements = driver.findElementsByXPath("//android.widget.TextView[@text='电话号码']/following-sibling::android.widget.LinearLayout/android.widget.TextView");
            if (telephoneElements == null || telephoneElements.size() == 0) {
                driver.navigate().back();
                continue;
            }

            for (MobileElement telTextEle : telephoneElements) {
                if (telTextEle == null) {
                    driver.navigate().back();
                    continue;
                }
                System.out.println(telTextEle.getText());
            }
            driver.navigate().back();
        }

        loopIndex++;
    }
}

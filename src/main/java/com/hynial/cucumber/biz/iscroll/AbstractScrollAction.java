package com.hynial.cucumber.biz.iscroll;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import java.time.Duration;

public abstract class AbstractScrollAction {
    protected int x1, y1, x2, y2;
    protected AppiumDriver driver;

    public AbstractScrollAction(AppiumDriver driver, int x1, int y1, int x2, int y2 ) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.driver = driver;
    }

    protected boolean scrollEnd = false;

    public abstract void isScrollEnd();
    public abstract void scrollAction();

    public void scrollStart(){
        while(!this.scrollEnd){
            isScrollEnd();

            scrollAction();

            scrollScreen();
        }
    }

    public void scrollScreen(){
        TouchAction actions = new TouchAction(driver);
        actions.press(PointOption.point(x1, y1))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(x2, y2)).release().perform();
    }

    public void scrollAndClick(String visibleText) {
        if(driver instanceof AndroidDriver) {
            ((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + visibleText + "\").instance(0))").click();
        }
    }
}

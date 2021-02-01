package com.hynial.cucumber.attach;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;

import java.time.Duration;

public class AttachAction {
    private AppiumDriver driver;

    public AttachAction(AppiumDriver driver) {
        this.driver = driver;
    }

    void action(){
        Dimension dim = driver.manage().window().getSize();
        int height = dim.getHeight();
        int width = dim.getWidth();

        MobileElement bottomBand = (MobileElement) driver.findElementById("com.tencent.mm:id/e8y");
        MobileElement topTitleBand = (MobileElement) driver.findElementById("com.tencent.mm:id/c_");
        System.out.println("Location:X:" + bottomBand.getLocation().x);
        System.out.println("Location:Y:" + bottomBand.getLocation().y);

        TouchAction actions = new TouchAction(driver);
        actions.press(PointOption.point(width / 2, height - bottomBand.getRect().getHeight() - 5))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(width/2, topTitleBand.getRect().getHeight() + 100)).release().perform();

        System.out.println();
    }
}

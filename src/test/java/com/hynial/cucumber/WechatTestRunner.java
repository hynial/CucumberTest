package com.hynial.cucumber;

import com.hynial.cucumber.attach.AppiumDebugHelp;
import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.factory.AppiumFactory;
import com.hynial.factory.createimpl.AndroidCreateDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class WechatTestRunner extends TestNGRunner {

    private boolean isAndroid(){
        AppiumDriver appiumDriver = AppiumFactory.getDriver();
        if(appiumDriver instanceof AndroidDriver){
            return true;
        }

        return false;
    }

    private void unlockDevice(){
        if(!isAndroid()) return ;

        AndroidDriver driver = (AndroidDriver) AppiumFactory.getDriver();
        if (driver.isDeviceLocked()) {
            driver.unlockDevice();

            Dimension dim = driver.manage().window().getSize();
            int height = dim.getHeight(), width = dim.getWidth();
            int x = width / 2;

            int top_y = (int) (height * 0.01), bottom_y = (int) (height * 0.90);
            int duration = 3000;
            TouchAction ts = new TouchAction(driver);
            ts.press(PointOption.point(x, bottom_y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                    .moveTo(PointOption.point(x, top_y)).release().perform();
        }
    }
    @BeforeClass
    private void beforeClass(){
        // create driver.
        // AppiumFactory.produce(new AndroidCreateDriver(ConfigLoader.getInstance()));

        // attach connection
        AppiumDebugHelp appiumDebugHelp = new AppiumDebugHelp("9e902769-aa90-4952-abb8-3a172e959cbc");
        AppiumFactory.setDriver(appiumDebugHelp.attachSession());

        // unlock
        unlockDevice();

        customize();
    }

    private void customize(){
        AppiumDriver appiumDriver = AppiumFactory.getDriver();

        System.out.println(((AndroidDriver)appiumDriver).getRemoteAddress().toString());
    }
}

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class WechatTestRunner extends TestNGRunner {
    private static final Logger logger = LoggerFactory.getLogger(WechatTestRunner.class);

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
            System.out.println("unlocking");
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
        boolean isAttach = true;
//        isAttach = false;
        if(!isAttach) {
            // create driver.
            AppiumFactory.produce(new AndroidCreateDriver(ConfigLoader.getInstance()));
        }else {
            // attach connection
            String sessionAttachId = "7b005041-6956-4b2e-82ae-19af9d91e3b9";
            logger.info("WillAttachSessionId:" + sessionAttachId);
            logger.error("Didn't do it.");
            AppiumDebugHelp appiumDebugHelp = new AppiumDebugHelp(sessionAttachId);
            AppiumFactory.setDriver(appiumDebugHelp.attachSession());
        }
        // unlock
        unlockDevice();

        customize();
    }

    private void customize(){
        AppiumDriver appiumDriver = AppiumFactory.getDriver();

    }
}

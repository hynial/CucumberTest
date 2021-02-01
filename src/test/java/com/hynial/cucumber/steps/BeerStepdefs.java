package com.hynial.cucumber.steps;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class BeerStepdefs {
    private AndroidDriver driver;

    private static long REMOTE_DRIVER_CREATE_CONSUME = 0l;

    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

//        desiredCapabilities.setCapability("app", "/Users/hynial/IdeaProjects/CucumberTest/apps/com.tencent.mm.apk");
//        desiredCapabilities.setCapability("appPackage", "com.tencent.mm");
//        desiredCapabilities.setCapability("appActivity", ".ui.LauncherUI");
        desiredCapabilities.setCapability("unicodeKeyboard", true);
        desiredCapabilities.setCapability("resetKeyboard", true);


        desiredCapabilities.setCapability("platformName", "android");
        desiredCapabilities.setCapability("deviceName", "Redmi5a");
        desiredCapabilities.setCapability("udid", "50e0e4bd7d74");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("adbExecTimeout", 60000);
        desiredCapabilities.setCapability("newCommandTimeout", 60000);
        desiredCapabilities.setCapability("noReset", true);

        desiredCapabilities.setCapability("autoWebview", false);
        desiredCapabilities.setCapability("autoWebviewTimeout", 30000);
        desiredCapabilities.setCapability("ensureWebviewsHavePages", false);

        URL remoteUrl = new URL("http://localhost:4724/wd/hub");

        long start = System.currentTimeMillis();
        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        long end = System.currentTimeMillis();
        REMOTE_DRIVER_CREATE_CONSUME = end - start;
        System.out.println("SpendSeconds:" + REMOTE_DRIVER_CREATE_CONSUME / 1000.0);

        if (driver.isDeviceLocked()) {
            System.out.println("unlocking");
            driver.unlockDevice();

            Dimension dim = driver.manage().window().getSize();
            int height = dim.getHeight();
            int width = dim.getWidth();
            int x = width / 2;

            int top_y = (int) (height * 0.01);
            int bottom_y = (int) (height * 0.90);
            int duration = 3000;
            System.out.println("coordinates :" + x + "  " + top_y + " " + bottom_y);
            TouchAction ts = new TouchAction(driver);
            //
            ts.press(PointOption.point(x, bottom_y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration)))
                    .moveTo(PointOption.point(x, top_y))
                    .release().perform();
            System.out.println();
        }
    }

    private void actions() {
        boolean isAppInstalled = driver.isAppInstalled("com.tencent.mm");
        String activityName = driver.currentActivity();
        String packageName = driver.getCurrentPackage();
        String url = driver.getCurrentUrl();
        driver.startActivity(new Activity("com.tencent.mm", ".ui.LauncherUI"));
    }

    @Given("^i have some beers$")
    public void iHaveSomeBeers() throws MalformedURLException {
        System.out.println("BeerCans I have some beers");
        this.setUp();
    }

    @And("some bananas")
    public void someBananas() {
        driver.startActivity(new Activity("com.tencent.mm", ".ui.LauncherUI"));
        String firstTitleId = "android:id/text1";

        MobileElement titleEle = null;

        int count = 0;
        while (count < 100) {
            if (titleEle != null && titleEle.getText() != null) {
                if (titleEle.getText().startsWith("微信")) {
                    break;
                }
            }
            try {
                titleEle = (MobileElement) driver.findElementById(firstTitleId);
            } catch (NoSuchElementException e) {
                // System.out.println(e.getMessage());
            }
            count++;
        }
        System.out.println("Find First Title " + count + " times");
    }

    @When("the noon at <position> is coming")
    public void theNoonAtPositionIsComing() {
        driver.findElementByXPath(
                "//android.widget.RelativeLayout[@resource-id='com.tencent.mm:id/e8y']/android.widget.LinearLayout/android.widget.RelativeLayout[2]")
                .click();
    }

    @Then("eat <counts> bananas")
    public void eatCountsBananas() {
        System.out.println("eating");

        Dimension dim = driver.manage().window().getSize();
        int height = dim.getHeight();
        int width = dim.getWidth();

        MobileElement bottomBand = (MobileElement) driver.findElementById("com.tencent.mm:id/e8y");
        MobileElement topTitleBand = (MobileElement) driver.findElementById("com.tencent.mm:id/c_");

        scrollAction(dim, bottomBand, topTitleBand);

        System.out.println("Done!");
        this.driver.quit();
    }

    private void scrollAction(Dimension dim, MobileElement bottomBand, MobileElement topTitleBand) {
        boolean seeTheBottom = false;

        int loopIndex = 0;
        while (!seeTheBottom) {
            try {
                MobileElement bottomElement = (MobileElement) driver.findElementById("com.tencent.mm:id/ba5");
                if (bottomElement != null) {
                    String totalText = bottomElement.getText();
                    if (totalText != null) {
                        System.out.println(totalText);
                        seeTheBottom = true;
                    }
                }
            } catch (NoSuchElementException noSuchElementException) {

            }

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
                    if (telTextEle == null) continue;
                    System.out.println(telTextEle.getText());
                }
                driver.navigate().back();
            }

            // 找完一页，滚动一下
            TouchAction actions = new TouchAction(driver);
            actions.press(PointOption.point(dim.getWidth() / 2, dim.getHeight() - bottomBand.getRect().getHeight() - 5))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                    .moveTo(PointOption.point(dim.getWidth() / 2, topTitleBand.getRect().getHeight() + 100)).release().perform();

            loopIndex++;
        }
    }
}

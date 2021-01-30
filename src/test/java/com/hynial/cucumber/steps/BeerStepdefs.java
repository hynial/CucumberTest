package com.hynial.cucumber.steps;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
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
        System.out.println("SpendSeconds:" + REMOTE_DRIVER_CREATE_CONSUME/1000.0);
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
        System.out.println("I have some beers");
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
        this.driver.quit();
    }
}

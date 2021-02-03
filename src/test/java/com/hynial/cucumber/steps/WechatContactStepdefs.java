package com.hynial.cucumber.steps;

import com.google.inject.Inject;
import com.hynial.cucumber.biz.scrollimpl.ContactScrollAction;
import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.wechat.support.World;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;

public class WechatContactStepdefs {
    @Inject
    private World world;

    @Given("start wechat")
    public void startWechat() {
        System.out.println("Wechat Starting");
        if(world.isAndroid()) {
            ((AndroidDriver) world.getAppiumDriver()).startActivity(new Activity(ConfigLoader.getInstance().loadAndroidCapabilities().getAppPackage(), ConfigLoader.getInstance().loadAndroidCapabilities().getAppActivity()));
        }
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
                titleEle = (MobileElement) world.getAppiumDriver().findElementById(firstTitleId);
            } catch (NoSuchElementException e) {
                // System.out.println(e.getMessage());
            }
            count++;
        }
        System.out.println("Find First Title " + count + " times");
    }

    @And("^go to (wechat|contact|discover|me) menu$")
    public void goToMenu(String menu) {
        System.out.println(menu);
        world.getAppiumDriver().findElementByXPath(
                "//android.widget.RelativeLayout[@resource-id='com.tencent.mm:id/e8y']/android.widget.LinearLayout/android.widget.RelativeLayout[2]")
                .click();
    }

    @When("loop all contacts")
    public void loopAllContacts() {
        AppiumDriver driver = world.getAppiumDriver();
        Dimension dim = driver.manage().window().getSize();
        MobileElement bottomBand = (MobileElement) driver.findElementById("com.tencent.mm:id/e8y");
        MobileElement topTitleBand = (MobileElement) driver.findElementById("com.tencent.mm:id/c_");

        int startX = dim.getWidth() / 2;
        int startY = dim.getHeight() - bottomBand.getRect().getHeight() - 5;
        int endX = dim.getWidth() / 2;
        int endY = topTitleBand.getRect().getHeight() + 100;

        ContactScrollAction contactScrollAction = new ContactScrollAction(world.getAppiumDriver(), startX, startY, endX, endY);
        contactScrollAction.scrollStart();
    }

    @Then("save or print contacts info")
    public void saveOrPrintContactsInfo() {
        System.out.println("Data Collection. Done");
    }
}

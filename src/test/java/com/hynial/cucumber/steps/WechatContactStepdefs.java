package com.hynial.cucumber.steps;

import com.google.inject.Inject;
import com.hynial.cucumber.biz.exp.Map2csv;
import com.hynial.cucumber.biz.scrollimpl.ContactScrollAction;
import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.cucumber.util.BizUtil;
import com.hynial.cucumber.util.CommonUtil;
import com.hynial.wechat.entity.WechatInfo;
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
import org.openqa.selenium.WebDriverException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WechatContactStepdefs {
    @Inject
    private World world;

    private boolean stop = true;
//    private boolean stop = false;
    @Given("start wechat")
    public void startWechat() {
        if (this.stop) return ;
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
            } catch (WebDriverException e) {
                // System.out.println(e.getMessage());
            }
            count++;
        }
        System.out.println("Find First Title " + count + " times");
    }

    @And("^go to (wechat|contact|discover|me) menu$")
    public void goToMenu(String menu) {
        if (this.stop) return ;
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
        int startY = dim.getHeight() - bottomBand.getRect().getHeight() - 20;
        int endX = dim.getWidth() / 2;
        int endY = topTitleBand.getRect().getHeight() + 100;

        ContactScrollAction contactScrollAction = new ContactScrollAction(world.getAppiumDriver(), startX, startY, endX, endY);
        contactScrollAction.scrollStart();
        world.setScenarioMap("CollectedData", contactScrollAction.getWechatInfoMap());
    }

    @Then("save or print contacts info")
    public void saveOrPrintContactsInfo() {
        Map<String, WechatInfo> collectedMap = (Map<String, WechatInfo>) world.getScenarioMap("CollectedData");
        collectedMap.forEach((x, y)->{
            System.out.println(y.toString());
        });

        // test
//        Map<String, WechatInfo> collectedMap = new HashMap<>();
//        WechatInfo wechatInfo = new WechatInfo();
//        wechatInfo.setAliasValue(WechatInfo.WECHAT_ID_ALIAS, "wechatid");
//        wechatInfo.setAliasValue(WechatInfo.SEX_ALIAS, "男");
//        wechatInfo.setAliasValue(WechatInfo.NICKNAME_ALIAS, "nick1");
//        wechatInfo.setAliasValue(WechatInfo.MOBILE_ALIAS, new ArrayList<String>(List.of("123456","3454657657", "342343")));
//        wechatInfo.setAliasValue(WechatInfo.REMARK_ALIAS, "晨桦");
//        wechatInfo.setAliasValue(WechatInfo.DESCRIPTION_ALIAS, "我家来自");
//
//        collectedMap.put("", wechatInfo);

        Map2csv map2csv = new Map2csv();
        String csvLines = map2csv.map2csv(collectedMap);
        String csvTitles = BizUtil.getCsvTitles().stream().collect(Collectors.joining(","));

        CommonUtil.writeFileWithBom("output.csv", csvTitles + "\n" + csvLines);
        System.out.println("Data Collection. Done");
    }
}

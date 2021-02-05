package com.hynial.cucumber.pages;

import com.hynial.cucumber.pages.base.BasePage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.WithTimeout;
import io.cucumber.guice.ScenarioScoped;

import java.time.temporal.ChronoUnit;

@ScenarioScoped
public class PersonalInfoPage extends BasePage {

    @WithTimeout(time = 5, chronoUnit = ChronoUnit.SECONDS)
    @AndroidFindBy(id = "com.tencent.mm:id/d8")
    private MobileElement topMoreBtn;

    public boolean isTopMoreButtonExist(){
        return topMoreBtn.isDisplayed();
    }

    public void clickTopMoreButton(){
        topMoreBtn.click();
    }
}

package com.hynial.cucumber.pages;

import com.hynial.cucumber.pages.base.BasePage;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.cucumber.guice.ScenarioScoped;
import lombok.Data;

@ScenarioScoped
@Data
public class ContactsPage extends BasePage {
    @AndroidFindBy(id = "com.tencent.mm:id/c_")
    private MobileElement topTitleBand;


    @AndroidFindBy(id = "com.tencent.mm:id/e8y")
    private MobileElement bottomBand;


}

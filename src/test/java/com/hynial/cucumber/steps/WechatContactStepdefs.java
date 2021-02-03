package com.hynial.cucumber.steps;

import com.google.inject.Inject;
import com.hynial.wechat.support.World;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WechatContactStepdefs {
    @Inject
    private World world;

    @Given("start wechat")
    public void startWechat() {
        System.out.println("Wechat Starting");
        System.out.println(world.word());
//        System.out.println(world.getAppiumDriver().getPageSource());
    }

    @And("^go to (wechat|contact|discover|me) menu$")
    public void goToMenu(String menu) {
        System.out.println(menu);
    }

    @When("loop all contacts")
    public void loopAllContacts() {

    }

    @Then("save or print contacts info")
    public void saveOrPrintContactsInfo() {
        System.out.println("Saving");
    }
}

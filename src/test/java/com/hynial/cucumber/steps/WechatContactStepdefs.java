package com.hynial.cucumber.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WechatContactStepdefs {
    @Given("start wechat")
    public void startWechat() {
        System.out.println("Wechat Starting");
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

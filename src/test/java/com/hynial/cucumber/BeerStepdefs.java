package com.hynial.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BeerStepdefs {

    @Given("^i have some beers$")
    public void iHaveSomeBeers(){
        System.out.println("I have some beers");
    }

    @And("some bananas")
    public void someBananas() {
    }

    @When("the noon at <position> is coming")
    public void theNoonAtPositionIsComing() {
    }

    @Then("eat <counts> bananas")
    public void eatCountsBananas() {
        System.out.println("eating");
    }
}

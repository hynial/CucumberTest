package com.hynial.wechat.support;

import com.hynial.factory.AppiumFactory;
import io.appium.java_client.AppiumDriver;
import io.cucumber.guice.ScenarioScoped;

// Scenario scoped it is used to show Guice
// what will be the shared classes/variables and instantiate them only in here
@ScenarioScoped
public class World {
    public AppiumDriver getAppiumDriver(){
        return AppiumFactory.getDriver();
    }
}

package com.hynial.wechat.support;

import com.google.inject.Inject;
import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.factory.AppiumFactory;
import com.hynial.factory.createimpl.AndroidCreateDriver;
import io.appium.java_client.AppiumDriver;
import io.cucumber.guice.ScenarioScoped;

// Scenario scoped it is used to show Guice
// what will be the shared classes/variables and instantiate them only in here
@ScenarioScoped
public class World {
    @Inject
    ConfigLoader configLoader;

    public String word(){
        return "w" + configLoader.loadAndroidCapabilities();
    }

    public AppiumDriver getAppiumDriver(){
        return AppiumFactory.produce(new AndroidCreateDriver(configLoader));
    }
}

package com.hynial.wechat.support;

import com.hynial.factory.AppiumFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.guice.ScenarioScoped;

import java.util.HashMap;
import java.util.Map;

// Scenario scoped it is used to show Guice
// what will be the shared classes/variables and instantiate them only in here
@ScenarioScoped
public class World {
    public AppiumDriver getAppiumDriver(){
        return AppiumFactory.getDriver();
    }

    public boolean isAndroid(){
        if(getAppiumDriver() instanceof AndroidDriver){
            return true;
        }

        return false;
    }

    private Map<String, Object> scenarioMap = new HashMap<>();

    public Object getScenarioMap(String key){
        return scenarioMap.get(key);
    }

    public Map<String, Object> setScenarioMap(String key, Object object){
        scenarioMap.put(key, object);
        return scenarioMap;
    }
}

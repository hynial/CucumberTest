package com.hynial.factory;

import com.hynial.factory.icreate.ICreateDriver;
import io.appium.java_client.AppiumDriver;

import java.util.ArrayList;
import java.util.List;

public class AppiumFactory {
    private static List<AppiumDriver> appiumDriverList = new ArrayList<>();

    public static AppiumDriver produce(ICreateDriver createDriver){
        appiumDriverList.add(createDriver.create());
        return getDriver();
    }

    public static AppiumDriver getDriver(){
        return appiumDriverList.get(0);
    }

    public static void setDriver(AppiumDriver appiumDriver){
        appiumDriverList.add(0, appiumDriver);
    }
}

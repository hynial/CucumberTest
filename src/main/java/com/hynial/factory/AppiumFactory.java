package com.hynial.factory;

import com.hynial.factory.icreate.ICreateDriver;
import io.appium.java_client.AppiumDriver;

import java.util.ArrayList;
import java.util.List;

public class AppiumFactory {
    private static ThreadLocal<List<AppiumDriver>> appiumDriverListThreadLocal = new ThreadLocal<>(){
        @Override
        protected List<AppiumDriver> initialValue() {
            return new ArrayList<>();
        }
    };

    public static AppiumDriver produce(ICreateDriver createDriver){
        List<AppiumDriver> appiumDriverList = appiumDriverListThreadLocal.get();
        appiumDriverList.add(createDriver.create());
        appiumDriverListThreadLocal.set(appiumDriverList);
        return getDriver();
    }

    public static AppiumDriver getDriver(){
        System.out.println(Thread.currentThread().getId());
        return appiumDriverListThreadLocal.get().get(0);
    }

    public static void setDriver(AppiumDriver appiumDriver){
        appiumDriverListThreadLocal.get().add(0, appiumDriver);
    }
}

package com.hynial.factory;

import com.hynial.factory.icreate.ICreateDriver;
import io.appium.java_client.AppiumDriver;

public class AppiumFactory {
    public static AppiumDriver produce(ICreateDriver createDriver){
        return createDriver.create();
    }
}

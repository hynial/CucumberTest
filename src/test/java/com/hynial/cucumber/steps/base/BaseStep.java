package com.hynial.cucumber.steps.base;

import com.hynial.factory.AppiumFactory;
import io.appium.java_client.AppiumDriver;

public class BaseStep {
    protected AppiumDriver driver = AppiumFactory.getDriver();

    public BaseStep() {
        // every step method new a step class instance.
        // System.out.println("BaseStep Construct:" + Thread.currentThread().getId());
    }
}

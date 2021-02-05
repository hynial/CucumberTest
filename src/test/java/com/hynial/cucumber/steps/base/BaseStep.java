package com.hynial.cucumber.steps.base;

import com.hynial.factory.AppiumFactory;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseStep {
    protected AppiumDriver driver = AppiumFactory.getDriver();

    public BaseStep() {
        // every step method new a step class instance.
        System.out.println("BaseStep Construct");
    }

    @BeforeClass
    public void b(){
        System.out.println("b");
    }

    @AfterClass
    public void a(){
        System.out.println("a");
    }
}

package com.hynial.cucumber;

import com.google.inject.Inject;
import com.hynial.cucumber.config.ConfigLoader;
import com.hynial.factory.AppiumFactory;
import com.hynial.factory.createimpl.AndroidCreateDriver;
import org.testng.annotations.BeforeClass;

public class WechatTestRunner extends TestNGRunner {

    @BeforeClass
    private void beforeClass(){
        // create driver.
        AppiumFactory.produce(new AndroidCreateDriver(ConfigLoader.getInstance()));
    }
}

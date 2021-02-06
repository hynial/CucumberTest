package com.hynial.cucumber;

import com.hynial.cucumber.attach.AppiumDebugHelp;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@CucumberOptions(
        dryRun = false,
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = {"src/test/resources/features"},
        glue = {"com.hynial.cucumber.steps"},
        tags = "",
        publish = false
)
public class TestNGRunner extends AbstractTestNGCucumberTests {
    public TestNGRunner() {
        System.out.println("artifact class.");
        new AppiumDebugHelp().artifactClass();
    }

    public static Map options;

    @BeforeSuite
    public void setupSuite() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Class<?> target = Thread.currentThread().getContextClassLoader().loadClass("com.hynial.cucumber.TestNGRunner");
        final CucumberOptions annotation = (CucumberOptions) target.getAnnotation(CucumberOptions.class);
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);

        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException var10) {
            throw new IllegalStateException(var10);
        } catch (SecurityException var11) {
            throw new IllegalStateException(var11);
        }

        f.setAccessible(true);

        Map memberValues;
        try {
            memberValues = (Map) f.get(handler);
        } catch (IllegalArgumentException var8) {
            throw new IllegalStateException(var8);
        } catch (IllegalAccessException var9) {
            throw new IllegalStateException(var9);
        }

        if(System.getProperty("cucumber.options.tags") != null) {
            memberValues.put("tags", System.getProperty("cucumber.options.tags"));
        }
        System.out.println("RunTags:" + memberValues.get("tags"));

        options = new HashMap();
        memberValues.forEach((x,y) -> {
            options.put(x, y);
        });


    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    private AppiumDriverLocalService appiumService;

    public void startAppiumServer() throws IOException {

        int port = 4723;
        String nodeJS_Path = "C:/Program Files/NodeJS/node.exe";
        String appiumJS_Path = "C:/Program Files/Appium/node_modules/appium/bin/appium.js";

        String osName = System.getProperty("os.name");

        if (osName.contains("Mac")) {
            appiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(("/usr/local/bin/node")))
                    .withAppiumJS(new File(("/usr/local/bin/appium")))
                    .withIPAddress("0.0.0.0")
                    .usingPort(port)
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withLogFile(new File("build/appium.log")));
        } else if (osName.contains("Windows")) {
            appiumService = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(nodeJS_Path))
                    .withAppiumJS(new File(appiumJS_Path))
                    .withIPAddress("0.0.0.0")
                    .usingPort(port)
                    .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
                    .withLogFile(new File("build/appium.log")));
        }
        appiumService.start();

    }

    public void tearDown() {
        try {
            appiumService.stop();
        } catch (Exception e) {
            System.out.println("Exception while running Tear down :" + e.getMessage());
        }
    }

    /*public static void changeDriverContextToWeb(AppiumDriver driver) {
        Set<String> allContext = driver.getContextHandles();
        for (String context : allContext) {
            if (context.contains("WEBVIEW"))
                driver.context(context);
        }
    }*/
}
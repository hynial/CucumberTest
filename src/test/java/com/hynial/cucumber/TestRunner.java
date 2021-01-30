package com.hynial.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

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
public class TestRunner extends AbstractTestNGCucumberTests {
    public static Map options;

    @BeforeSuite
    public void setupSuite() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Class<?> target = Thread.currentThread().getContextClassLoader().loadClass("com.hynial.cucumber.TestRunner");
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
}
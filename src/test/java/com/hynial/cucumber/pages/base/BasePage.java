package com.hynial.cucumber.pages.base;

import com.hynial.factory.AppiumFactory;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Function;

public abstract class BasePage {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    protected Wait<AppiumDriver> wait;

    protected AppiumDriver driver;

    public BasePage() {
        System.out.println("init base page:" + Thread.currentThread().getId()); // inject, make this only one
        driver = AppiumFactory.getDriver();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    public boolean waitForControl(WebElement mobileControl) {
        log.info("Wait for get control of element");
        wait = getWaitWithIgnore(120);
        return wait.until(appiumDriver -> mobileControl != null && mobileControl.isEnabled() && mobileControl.isDisplayed());
    }

    public void waitElementToClick(WebElement mobileElement){
        waitForControl(mobileElement);
        wait.until(ExpectedConditions.elementToBeClickable(mobileElement));
    }

    private FluentWait getWaitWithIgnore(long timeout) {
        return new FluentWait<>(AppiumFactory.getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);
    }

    private FluentWait getWait(long timeout) {
        return new FluentWait<>(AppiumFactory.getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(1));
    }

    protected MobileElement getWebDriverWait(By by, long seconds){
        return (MobileElement) (new WebDriverWait(driver, seconds)).until(ExpectedConditions.presenceOfElementLocated(by));
    }

    protected MobileElement getFluentWait(By by, long seconds){
        return (MobileElement) getWait(seconds).until(new Function<AppiumDriver, WebElement>()
        {
            public WebElement apply(AppiumDriver driver) {
                return driver.findElement(by);
            }
        });
    }

    public String getScreenTitle(){
        String title = AppiumFactory.getDriver().getTitle();
        log.info("Title of current screen: " + title);
        return title;
    }
}

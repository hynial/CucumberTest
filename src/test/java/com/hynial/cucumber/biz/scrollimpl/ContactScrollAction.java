package com.hynial.cucumber.biz.scrollimpl;

import com.google.inject.Inject;
import com.hynial.cucumber.biz.exp.Map2csv;
import com.hynial.cucumber.biz.iscroll.AbstractScrollAction;
import com.hynial.cucumber.pages.PersonalInfoPage;
import com.hynial.cucumber.util.BizUtil;
import com.hynial.cucumber.util.CommonUtil;
import com.hynial.wechat.entity.WechatInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactScrollAction extends AbstractScrollAction {
    private int loopIndex = 0;
    private int writeIndex = 0;

    @Inject
    private PersonalInfoPage personalInfoPage;

    private Map2csv map2csv = new Map2csv();
    private String partialOutputPath = "partialOutput.csv";

    public ContactScrollAction(AppiumDriver driver, int x1, int y1, int x2, int y2) {
        super(driver, x1, y1, x2, y2);
    }

    private Map<String, WechatInfo> wechatInfoMap = new HashMap<>();

    public Map<String, WechatInfo> getWechatInfoMap() {
        return wechatInfoMap;
    }

    @Override
    public void isScrollEnd() {
        try {
            String totalXpath = "com.tencent.mm:id/ba5";
            // two methods to set the wait timeout.
//            MobileElement bottomElement = (MobileElement) new FluentWait<>(AppiumFactory.getDriver())
//                    .withTimeout(Duration.ofSeconds(5))
//                    .pollingEvery(Duration.ofSeconds(1)).until(ExpectedConditions.presenceOfElementLocated(By.id(totalXpath)));

            MobileElement bottomElement = (MobileElement) (new WebDriverWait(driver, 1)).until(ExpectedConditions.presenceOfElementLocated(By.id(totalXpath)));
            if (bottomElement != null) {
                String totalText = bottomElement.getText();
                if (totalText != null) {
                    System.out.println(totalText);
                    super.scrollEnd = true;
                }
            }
        } catch (NoSuchElementException noSuchElementException) {

        } catch (WebDriverException webDriverException) {

        }
    }

    @Override
    public void scrollAction() {
        List<MobileElement> contactsLinearList = driver.findElementsByXPath(
                "//android.widget.ListView[@resource-id='com.tencent.mm:id/h4']/android.widget.LinearLayout");

        if (contactsLinearList == null || contactsLinearList.size() == 0) return;
        // 跳过第一块标签
        for (int i = (loopIndex == 0 ? 1 : 0); i < contactsLinearList.size(); i++) {
            contactsLinearList = driver.findElementsByXPath("//android.widget.ListView[@resource-id='com.tencent.mm:id/h4']/android.widget.LinearLayout");
            if (contactsLinearList.size() <= i) break;
            MobileElement linearElement = contactsLinearList.get(i);
            try {
                linearElement.click();
            } catch (StaleElementReferenceException staleElementReferenceException) {
                // wrong exception - not the actual exception, never appear here, see the appium log.
            }

            // wechat id
            WechatInfo wechatInfo = new WechatInfo();
            wechatInfo.setPersonalInfoPage();
            if (CommonUtil.isEmpty(wechatInfo.getStringByAlias(WechatInfo.WECHAT_ID_ALIAS)) || wechatInfoMap.get(wechatInfo.getStringByAlias(WechatInfo.WECHAT_ID_ALIAS)) != null) {
                back();
                continue;
            }
            // enter remark
            if (wechatInfo.isExistTopMoreButton()) {
                wechatInfo.clickRightTopMore();
                CommonUtil.sleep(250);

                wechatInfo.clickRemarkTagFromDataSetting();
                // remark tag page
                wechatInfo.setRemarkTagPage();
                back();
                back();
                // click more
            }
            wechatInfo.clickMoreInfo();
            wechatInfo.setMoreInfoPage();

            back();
            //sleep(250);
            if (!wechatInfo.isContactPage()) {
                back();
            }
            if (wechatInfoMap.get(wechatInfo.getStringByAlias(WechatInfo.WECHAT_ID_ALIAS)) == null) {
                wechatInfoMap.put(wechatInfo.getStringByAlias(WechatInfo.WECHAT_ID_ALIAS), wechatInfo);
                // partial write to file
                String record = map2csv.wechatInfo2String(wechatInfo) + "\n";

                if (writeIndex == 0) {
                    String csvTitles = BizUtil.getCsvTitles().stream().collect(Collectors.joining(","));
                    CommonUtil.writeFileWithBom(partialOutputPath, csvTitles + "\n" + record);
                } else {
                    CommonUtil.appendFile(partialOutputPath, record);
                }
                writeIndex++;
                System.out.println("HaveWrittenCount:" + writeIndex);
            }
        }

        loopIndex++;
    }

    private void back() {
        driver.navigate().back();
    }
}

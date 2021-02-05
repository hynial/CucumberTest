package com.hynial.cucumber.biz.scrollimpl;

import com.hynial.cucumber.biz.exp.Map2csv;
import com.hynial.cucumber.biz.iscroll.AbstractScrollAction;
import com.hynial.cucumber.util.BizUtil;
import com.hynial.cucumber.util.CommonUtil;
import com.hynial.wechat.entity.WechatInfo;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactScrollAction extends AbstractScrollAction {
    private int loopIndex = 0;
    private int writeIndex = 0;

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
            MobileElement bottomElement = (MobileElement) driver.findElementById("com.tencent.mm:id/ba5");
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
//                contactsLinearList = driver.findElementsByXPath("//android.widget.ListView[@resource-id='com.tencent.mm:id/h4']/android.widget.LinearLayout");
//                linearElement = contactsLinearList.get(i);
//                linearElement.click();
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

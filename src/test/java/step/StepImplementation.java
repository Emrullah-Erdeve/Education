package step;
import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import base.BaseTest;
import com.thoughtworks.gauge.Step;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;


public class StepImplementation extends BaseTest {

     Actions actions = new Actions(driver);
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;
    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(BaseTest.class);


   @Step("<key> li elemente <text> değerini yaz")
   public void valueKey(String key, String text){
        findElement(key).sendKeys(text);
   }

   @Step("<time> saniye kadar bekle")
    public void delay(long time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }

    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
        if (!key.equals("")) {
            try {
                WebElement element = findElement(key);
                hoverElement(element);
                waitByMilliSeconds(500);
                clickElement(element);
            }

            catch(Exception e){

            }
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds){
       try {
           Thread.sleep(seconds*1000);
       }
       catch (InterruptedException e){
           e.printStackTrace();
       }
    }

    private void hoverElement(WebElement element) {
        actions.moveToElement(element).build().perform();
    }

    private WebElement findElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    @Step({"Check if current URL contains the value <expectedURL>",
            "Şuanki URL <url> değerini içeriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL) {
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = driver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("Şuanki URL" + expectedURL + " değerini içeriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assertions.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step("<key> değerine sahip ürün sepete eklendi")
    public boolean sepetSayisi(String key){
       return sepetIcon(key)>0;

    }

    public int sepetIcon(String key){
      String sepet=findElement(key).getText();
        System.out.println(sepet);
        return Integer.parseInt(sepet);
    }

    @Step("<key> elementine ine sahip değerin üzerinde dur")
    public void hoverElementElektronik(String key) {
     WebElement element=  driver.findElement(By.xpath(key));
        actions.moveToElement(element).build().perform();
    }

    @Step("<key> elementinin üzerinde dur")
    public void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }
    @Step("sayfayı aşağı kaydır")
    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,450)", "");
        logger.info("Sayfa aşağı kaydırıldı!!");

    }
    @Step("<key> li element <key> text değerini içeriyormu")
    public void controlTextValue(String xpath, String key) {
        Assert.assertTrue("Element bulunamadı", driver.findElement(By.xpath(key)).getText().equals(key));
        logger.info(xpath + "Xpath li element text değerini içeriyor!!");

    }
    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <text> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {
        Boolean containsText = findElement(key).getText().contains(expectedText);
        assertTrue("Expected text is not contained", containsText);
        logger.info(key + " elementi " + expectedText + " degerini iceriyor.");
    }

    @Step("Önceki sayfaya git")
    public void goToBack(){

       driver.navigate().back();
    }
}



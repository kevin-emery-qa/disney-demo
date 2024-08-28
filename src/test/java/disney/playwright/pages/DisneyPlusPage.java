package disney.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.FileReader;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class DisneyPlusPage {
    Properties properties = new java.util.Properties();
    String baseUrl;
    private Page page;
    private final Page.WaitForSelectorOptions isVisible;

    public DisneyPlusPage(Page page) {
        this.page = page;
        try (FileReader reader = new FileReader("playwright.properties")){
            properties.load(reader);
            baseUrl = properties.getProperty("baseUrl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        isVisible = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void scrollVisible(String locator) {
        scrollIntoViewJs(locator);
        page.waitForSelector(locator, isVisible);
    }

    private void scrollIntoViewJs(String elementLocator) {
        String evalString = "const element = document.querySelector(\"" + elementLocator + "\");" +
                "if (element) {" +
                "element.scrollIntoView({ behavior: \"smooth\", block: \"start\", inline: \"nearest\" });" +
                "}";
        page.evaluate(evalString);
        assertTrue(true);
    }
 }

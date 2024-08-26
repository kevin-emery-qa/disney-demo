package playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Assertions;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class DisneyPlusHomePage extends DisneyPlusPage {
    private Page page;
    private Page.WaitForSelectorOptions isVisible;

    private final DisneyPlusPage plusPage = new DisneyPlusPage();
    private DisneyPlusHomePage homePage;

    public static class Locators {
        public static String someElement = "";
    }

    public DisneyPlusHomePage(Page page) {
        super();
        this.page = page;
        isVisible = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000);
    }

    public long measurePageLoad() {
        page.navigate(plusPage.getBaseUrl());
        long startTime = System.nanoTime();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }
}

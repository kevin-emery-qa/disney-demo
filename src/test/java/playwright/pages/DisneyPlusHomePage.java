package playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisneyPlusHomePage extends DisneyPlusPage {
    private final Page page;
    private final Page.WaitForSelectorOptions isVisible, isAttached;

    public static class Locators {
        public static String disneyLogo = "[alt*='Disney+ Logo']";
        public static String center = ".md-align-center";
        public static String centerBundle1 = center + " [data-testid='2p_bundle_cta'] span";
        public static String centerBundle2 = center + " [data-testid='max_ads_bundle'] span";
        public static String centerBundle1Terms = center + " li:has([data-testid='2p_bundle_cta']) [data-testid='mlp_link']";
        public static String centerBundle2Terms = center + " li:has([data-testid='max_ads_bundle']) [data-testid='mlp_link']";
    }

    public DisneyPlusHomePage(Page page) {
        super(page);
        this.page = page;
        isVisible = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000);
        isAttached = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(10000);
    }

    public long measurePageLoad() {
        page.navigate(super.getBaseUrl());
        long startTime = System.nanoTime();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }

    public void scrollAndVerifyElements() {
        page.waitForSelector(Locators.centerBundle1, isAttached);
        page.waitForSelector(Locators.centerBundle1Terms, isAttached);
        page.waitForSelector(Locators.centerBundle2, isAttached);
        page.waitForSelector(Locators.centerBundle2Terms, isAttached);
        scrollVisible(Locators.disneyLogo);
    }
}

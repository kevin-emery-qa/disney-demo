package playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.concurrent.TimeUnit;

public class DisneyPlusHomePage extends DisneyPlusPage {
    private Page page;
    private final Page.WaitForSelectorOptions isAttached, isVisible;

    private final DisneyPlusPage plusPage = new DisneyPlusPage();

    public static class Locators {
        public static String disneyLogo = "[alt*='Disney+ Logo']";
        public static String center = ".md-align-center";
        public static String centerGallery = center + " .gallery-images-container";
        public static String centerBundle1 = center + " [data-testid='2p_bundle_cta']";
        public static String centerBundle2 = center + " [data-testid='max_ads_bundle']";
        public static String centerBundle1Terms = center + " li:has([data-testid='2p_bundle_cta']) [data-testid='mlp_link']";
        public static String centerBundle2Terms = center + " li:has([data-testid='max_ads_bundle']) [data-testid='mlp_link']";
    }

    public DisneyPlusHomePage(Page page) {
        super();
        this.page = page;
        isAttached = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.ATTACHED)
                .setTimeout(10000);
        isVisible = new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000);
    }

    public long measurePageLoad() {
        page.navigate(plusPage.getBaseUrl());
        long startTime = System.nanoTime();
        page.waitForSelector(Locators.disneyLogo, isVisible).scrollIntoViewIfNeeded();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
    }

    public void verifyElementsLoaded() {
        page.waitForSelector(Locators.centerGallery, isAttached);
        page.waitForSelector(Locators.centerBundle1, isVisible);
        page.waitForSelector(Locators.centerBundle2, isVisible);
        //page.waitForSelector(Locators.centerBundle1Terms, isVisible).scrollIntoViewIfNeeded();
        //page.waitForSelector(Locators.centerBundle2Terms, isVisible).scrollIntoViewIfNeeded();
        page.waitForSelector(Locators.disneyLogo, isVisible).scrollIntoViewIfNeeded();
    }
}

package disney.playwright.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
        public static String planCompSection = "section#us-plan-comp";
        public static String planCompHeaderRow = planCompSection + " .header-row-body";
        public static String planSelect0 = ".feature-column-header + fieldset #select-0";
        public static String planSelect1 = ".feature-column-header + fieldset #select-1";
    }

    public static class ExpectedText {
        public static String choosePlanHeader = "Choose your plan*";
        public static String huluMaxAdBundleHeader = "Disney+, Hulu, Max Bundle (With Ads)";
        public static String huluMaxNoAdBundleHeader = "Disney+, Hulu, Max Bundle (No Ads)";
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
        // Expect these elements to be attached prior to lazy loading
        page.waitForSelector(Locators.centerBundle1, isAttached);
        page.waitForSelector(Locators.centerBundle1Terms, isAttached);
        page.waitForSelector(Locators.centerBundle2, isAttached);
        page.waitForSelector(Locators.centerBundle2Terms, isAttached);
        page.waitForSelector(Locators.planCompHeaderRow, isAttached);

        // Expect specific text to be present in the plan headers
        assertTrue(page.locator(Locators.planCompHeaderRow)
                .allTextContents().toString().contains(ExpectedText.huluMaxAdBundleHeader));
        assertTrue(page.locator(Locators.planCompHeaderRow)
                .allTextContents().toString().contains(ExpectedText.huluMaxNoAdBundleHeader));

        // Disney logo should not be visible yet because it has not lazy loaded
        assertFalse(page.locator(Locators.disneyLogo).isVisible());

        // Expect these to be visible in the plan choice section
        scrollVisible(Locators.planCompSection);
        assertTrue(page.locator(Locators.planCompSection).getByText(ExpectedText.choosePlanHeader).isVisible());
        assertTrue(page.locator(Locators.planSelect0).isVisible());
        assertTrue(page.locator(Locators.planSelect1).isVisible());

        // Scroll to the disney logo at the bottom of the page and confirm that it is visible after lazy loading
        scrollVisible(Locators.disneyLogo);
        page.waitForSelector(Locators.disneyLogo, isVisible);
    }
}

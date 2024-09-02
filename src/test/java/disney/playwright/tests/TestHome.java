package disney.playwright.tests;

import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestHome extends TestFixtures {
    @Test
    public void testHomePageLoad() {
        final float MAX_LOAD_TIME = 10; //seconds

        page.navigate(homePage.getBaseUrl());
        float loadTime = ((float) homePage.measurePageLoad() / 1000);
        String loadTimeStr = String.format("%.2f", loadTime);
        assertFalse(loadTime > MAX_LOAD_TIME, "Home page at " + homePage.getBaseUrl()
                + " took more than " + String.valueOf(MAX_LOAD_TIME) + " seconds to load (" + loadTimeStr + "s)");
    }

    @Test
    public void testLazyLoading() {
        page.navigate(homePage.getBaseUrl());
        page.waitForLoadState(LoadState.NETWORKIDLE);
        homePage.scrollAndVerifyElements();
    }
}

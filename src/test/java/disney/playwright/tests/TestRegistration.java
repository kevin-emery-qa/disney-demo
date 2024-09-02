package disney.playwright.tests;

import com.microsoft.playwright.options.LoadState;
import disney.playwright.pages.DisneyPlusRegistrationPage;
import disney.playwright.util.EmailHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRegistration extends TestFixtures {
    @Test
    public void testRegistrationLogin() {
        page.navigate(homePage.getBaseUrl());
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        registrationPage.registerHuluBundle();

        page.waitForTimeout(10000); // give it some time to let the e-mail come in
        EmailHelper emailHelper = new EmailHelper();
        try {
            assertTrue(emailHelper.isEmailSubjectPresent("New login to Disney+")
                    || emailHelper.isEmailSubjectPresent("Welcome to MyDisney"));
            emailHelper.deleteEmail();
            assertEquals(0, emailHelper.getEmailCount());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

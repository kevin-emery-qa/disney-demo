package disney.playwright.util;
import javax.mail.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailHelper {
    Properties suiteProperties = new Properties();
    Properties emailProperties = new Properties();
    String username;
    String password;
    Session session;
    Store store;

    public EmailHelper() {
        emailProperties.put("mail.store.protocol", "imaps");
        emailProperties.put("mail.imaps.host", "imap.gmail.com");
        emailProperties.put("mail.imaps.port", "993");
        emailProperties.put("mail.imaps.ssl.enable", "true");
        emailProperties.put("mail.imaps.ssl.protocols", "TLSv1.3");

        try {
            FileReader reader = new FileReader("playwright.properties");
            suiteProperties.load(reader);
            username = suiteProperties.getProperty("testEmailAddress");
            password = suiteProperties.getProperty("testEmailAppPassword");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connect() throws MessagingException {
        session = Session.getDefaultInstance(emailProperties, null);
        store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, password);
    }

    private Message[] checkEmail() throws MessagingException {
        connect();
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);
        return inbox.getMessages();
    }

    public boolean isEmailSubjectPresent(String subject) throws MessagingException {
        List<Message> messages = Arrays.asList(checkEmail());
        return messages.stream().anyMatch(message -> {
            try {
                return message.getSubject().contains(subject);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public int getEmailCount() throws MessagingException {
        connect();
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_ONLY);
        return inbox.getMessageCount();
    }

    public void deleteEmail() throws MessagingException {
        connect();
        Folder inbox = store.getFolder("inbox");
        inbox.open(Folder.READ_WRITE);
        Message[] messages = inbox.getMessages();
        for (Message message : messages) {
            message.setFlag(Flags.Flag.DELETED, true);
        }
    }
}

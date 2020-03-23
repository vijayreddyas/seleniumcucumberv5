package modules;

import org.junit.Assert;
import pages.ContactUsPage;
import pages.HomePage;

public class ContactUs extends PageFactory{

    /**
     *
     * @param email
     * @param textMessage
     */
    public void sendMessageWithError(String email, String textMessage){
        click(HomePage.contact_us);
        type(ContactUsPage.email, email);
        type(ContactUsPage.message, textMessage);
        click(ContactUsPage.submit);
        Assert.assertTrue(getText(ContactUsPage.error_message).contains("There is 1 error"));
    }

}

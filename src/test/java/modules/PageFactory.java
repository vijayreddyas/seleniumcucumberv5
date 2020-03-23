package modules;

import helpers.SeleniumUtils;

public class PageFactory extends SeleniumUtils {

    public LaunchApplicationAction LaunchApplicationAction(){
        return new LaunchApplicationAction();
    }
    public SiginInSignOutAction SiginInSignOutAction(){
        return new SiginInSignOutAction();
    }
    public ContactUs ContactUs(){
        return new ContactUs();
    }
}

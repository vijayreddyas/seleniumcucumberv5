package modules;

import base.TestBase;

public class LaunchApplicationAction extends TestBase {

    /**
     *
     * @param url
     */
    public void navigateToApplication(String url){
        getWebDriver().get(url);
        info("Navigating to URL :: " + url);
    }
}

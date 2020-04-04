package helpers;

import base.TestBase;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SeleniumUtils extends TestBase {
    public static Properties objects = new Properties();
    protected int timeout = Integer.parseInt(LoadProperties.getProperty("timeout"));
    private static final String error1 = "//*[contains(@class,'error')]";
    private static final String error2 = "//*[contains(@class,'red')]";
    private static boolean highlight = Boolean.valueOf(LoadProperties.getProperty("highlight"));
    private boolean visibilityCheck = Boolean.valueOf(LoadProperties.getProperty("visibilityCheck"));
    private String userDir = System.getProperty("user.dir");
    //private String snapshotDir = userDir + userDir.substring(userDir.lastIndexOf('\\')+1, userDir.length())+"\\screenshots";
    private String snapshotDir = userDir + "\\screenshots";
    private boolean prevVisibilityCheck = true;
    private boolean showVerifications = true;

    private void highlight(WebElement element) {
        try {
            if (highlight) {
                JavascriptExecutor js = (JavascriptExecutor) getDriver();
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "border: 2px solid red;");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to highlight the element");
        }
    }

    /**
     * Execute the javascript code using javascript driver on particular web
     * element
     *
     * @param javascriptToExecute
     * @param locator
     */
    public void executeScript(String javascriptToExecute, String locator) {
        try {
            WebElement webElement = waitForElement(locator);
            ((JavascriptExecutor) getDriver()).executeScript(javascriptToExecute, webElement);
        } catch (WebDriverException webDrivExc) {
            webDrivExc.printStackTrace();
            error("Exception on Javascript Execution occured while performing executeScript");
        }
    }

    /**
     * Clicks on element specified by locator. - Implicitly waits for the
     * element to be present before click, so no need to explicitly wait for
     * element using AjaxCondition - Logs to report on successful click - Logs
     * to report if element not present - Waits for 5 secs if element just
     * appeared before 1 sec, to allow complete page load
     *
     * @param locator
     * @return
     */
    public boolean click(String locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            try {
                element.click();
                info("Click \"" + getLocatorName(locator) + "\"");
            } catch (Error e) {
                e.printStackTrace();
                return true;
            } catch (TimeoutException e) {
                e.printStackTrace();
                return true;
            } catch (StaleElementReferenceException e) {
                element = waitForElement(locator);
                element.click();
                info("Click \"" + getLocatorName(locator) + "\"");
            } catch (Exception e) {
                try {
                    executeScript("arguments[0].click()", locator);
                    return true;
                } catch (Exception e1) {
                    e1.printStackTrace();
                    fail("Unable to Click Element :" + getLocatorName(locator) + " Error:" + (e.getMessage().length() > 100 ? e.getMessage().substring(0, 99) + "..." : e.getMessage()));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param locator
     * @return
     */
    public WebElement findElementM(String locator) {
        WebElement element = null;
        if (locator.startsWith("/") || locator.startsWith("xpath=") || locator.contains("//") || locator.startsWith("(/")) {
            info("Identifying element with xpath :: " + locator.replace("xpath=", ""));
            element = searchElement(By.xpath(locator.replace("xpath=", "")));
            return element;
        } else if (locator.startsWith("link=")) {
            info("Identifying element with link text :: " + locator.replace("link=", ""));
            element = searchElement(By.linkText(locator.replace("link=", "")));
            return element;
        } else if (locator.startsWith("linkp=")) {
            info("Identifying element with partial link text :: " + locator.replace("linkp=", ""));
            element = searchElement(By.partialLinkText(locator.replace("linkp=", "")));
            return element;
        } else if (locator.startsWith("id=")) {
            info("Identifying element with id :: " + locator.replace("id=", ""));
            element = searchElement(By.id(locator.replace("id=", "")));
            return element;
        } else if (locator.startsWith("name=")) {
            info("Identifying element with name :: " + locator.replace("name=", ""));
            element = searchElement(By.name(locator.replace("name=", "")));
            return element;
        } else if (locator.startsWith("tag=")) {
            info("Identifying element with tag :: " + locator.replace("tag=", ""));
            element = searchElement(By.tagName(locator.replace("tag=", "")));
            return element;
        } else if (locator.startsWith("css=")) {
            info("Identifying element with css selector :: " + locator.replace("css=", ""));
            element = searchElement(By.cssSelector(locator.replace("css=", "")));
            return element;
        } else if (locator.startsWith("class=")) {
            info("Identifying element with class :: " + locator.replace("class=", ""));
            element = searchElement(By.className(locator.replace("class=", "")));
            return element;
        } else {
            info("Identifying element with  :: " + locator);
            element = searchElement(By.id(locator));
            if (element == null) {
                element = searchElement(By.name(locator));
                if (element == null) {
                    element = searchElement(By.linkText(locator));
                    if (element == null) {
                        element = searchElement(By.partialLinkText(locator));
                    }
                }
            }
        }
        return element;
    }

    /**
     * Returns value of specified attribute for specified locator
     */
    public String getAttribute(String locator, String attributeName) {
        WebElement element = waitForElement(locator);
        String attributeValue = "";
        if (element != null) {
            try {
                attributeValue = element.getAttribute(attributeName);
                info("\"" + attributeName + "\" attribute for \"" + getLocatorName(locator) + "\" is \"" + attributeValue + "\"");
            } catch (Exception e) {
                fail("Unable to get attribute " + attributeName + "for \"" + getLocatorName(locator) + "\"");
            }
        }
        return attributeValue;
    }

    public String getLocatorName(String locator) {
        return objects.getProperty(locator, locator);
    }

    /**
     * Returns text of specified locator
     */
    public String getText(String locator) {
        return getText(locator, timeout);
    }

    /**
     * Returns text of specified locator
     */
    public String getText(String locator, int timeout) {
        WebElement element;
        try {
            element = waitForElement(locator, timeout);
        } catch (Exception e) {
            return "";
        }
        String text = "";
        try {
            text = element.getText().trim();
        } catch (Exception e) {
            fail("Unable to get text from Element :" + getLocatorName(locator));
        }
        if (locator.equals("//html")) {
            info("GetText from Element: \"" + getLocatorName(locator) + "\"");
        } else {
            info("GetText from Element: \"" + getLocatorName(locator) + "\", " + text);
        }
        return text;
    }

    /**
     * Wait for specified milliseconds
     */
    public void wait(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException ex) {
            error(ex.getMessage());
        }
    }

    /**
     * @param str
     * @return
     */
    public static String toSentence(String str) {
        ArrayList<Character> newStr = new ArrayList<Character>();
        newStr.add(Character.toUpperCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isLetter(c)) {
                if (Character.isUpperCase(c) && !Character.isUpperCase(str.charAt(i - 1))) {
                    newStr.add(' ');
                    newStr.add(c);
                } else if (i < str.length() - 1 && Character.isUpperCase(c) && Character.isLowerCase(str.charAt(i + 1))) {
                    newStr.add(' ');
                    newStr.add(c);
                } else if (str.charAt(i) == '_') {
                    newStr.add(' ');
                } else if (str.charAt(i - 1) == '_') {
                    newStr.add(Character.toUpperCase(c));
                } else {
                    newStr.add(c);
                }

            }
        }
        return newStr.toString().replace(", ", "").replace("_", " ").replace("  ", " ").replace("[", "").replace("]", "");
    }

    /**
     * @return
     */
    private String getError() {
        try {
            return "\nError:  " + getDriver().findElement(By.xpath(error1)).getText();
        } catch (Exception e) {
            try {
                return "\nError:  " + getDriver().findElement(By.xpath(error2)).getText();
            } catch (Exception e2) {
                return "";
            }
        }
    }

    /**
     * @param driver
     */
    public void waitForLoad(WebDriver driver) {
        new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * @param by
     * @return
     */
    private WebElement searchElement(By by) {
        WebElement element = null;
        int count = 0;
        List<WebElement> elements;
        try {
            elements = getDriver().findElements(by);
            for (Iterator<WebElement> iterator = elements.iterator(); iterator.hasNext(); ) {
                element = (WebElement) iterator.next();
                try {
                    if (element.isDisplayed()) {
                        highlight(element);
                        return element;
                    }
                } catch (Exception e) {
                }

            }
            if (element.isDisplayed()) {
                //highlight(element);
            } else {
                if (visibilityCheck) {
                    return null;
                }
                return elements.get(0);
            }
        } catch (Exception e) {
            try {
                if (count < 1 && e.getMessage().contains("alert")) {
                    acceptAlert();
                    count++;
                    searchElement(by);
                }
            } catch (Exception e1) {

            }
            return null;
        }

        return element;
    }

    /**
     * @return
     */
    public String acceptAlert() {
        return acceptAlert(timeout / 2);
    }

    /**
     * @param timeout
     * @return
     */
    public String acceptAlert(int timeout) {
        for (int seconds = 0; ; seconds++) {
            if (seconds >= timeout) {
                return "";
            } else {
                try {
                    String alertText = getDriver().switchTo().alert().getText().trim();
                    getDriver().switchTo().alert().accept();
                    return alertText;
                } catch (Exception e) {

                }
                wait(1000);
            }
        }

    }

    /**
     * Select drop down with specified value Skip selection if value is "."
     */
    public void selectValue(String locator, String value) {
        if (value.equals(".")) {
            return; // if data value equals to "." then that wont be typed. //to
            // reuse filling methods
        }
        WebElement element = waitForElement(locator);
        if (element != null) {
            new Select(element).selectByValue(value);
            info("Selected Element: \"" + getLocatorName(locator) + "\" with option value:" + value);
        }
    }

    /**
     * Set the attribute value of specified name with specified value for
     * specified element
     */
    public void setAttribute(WebElement element, String attributeName, String attributeValue) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', arguments[1]);", element, attributeValue);

    }

    /**
     * Set visibility checking before finding element to specified value
     * true/false
     */
    public void setVisibilityCheck(boolean visibilityCheck) {
        this.prevVisibilityCheck = this.visibilityCheck;
        this.visibilityCheck = visibilityCheck;
    }

    /**
     * Type specified text in element Skip to type if text="."
     */
    public WebElement type(String locator, String text) {
        if (text.equals(".")) {
            return null;
        }
        WebElement element = waitForElement(locator);
        if (element != null) {
            try {
                element.clear();
                element.sendKeys(text);
                wait(Integer.parseInt(LoadProperties.getProperty("typeWait", "0")));
                info("Type \"" + text + "\" in \"" + getLocatorName(locator) + "\"");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    element.sendKeys(text);
                    wait(Integer.parseInt(LoadProperties.getProperty("typeWait", "0")));
                    info("Type Keys  \"" + text + "\" in \"" + getLocatorName(locator) + "\"");
                } catch (Exception e1) {
                    e1.printStackTrace();
                    try {
                        setValue(element, text);
                        wait(Integer.parseInt(LoadProperties.getProperty("typeWait", "0")));
                        info("Set Value \"" + text + "\" in \"" + getLocatorName(locator) + "\"");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        fail("Unable to type in \"" + getLocatorName(locator) + "\"" + " Error: " + e.getMessage());
                    }
                }
            }
        }
        return element;
    }

    /**
     * @param element
     * @param text
     */
    private void setValue(WebElement element, String text) {
        try {
            setAttribute(element, "value", text);
        } catch (Exception e) {
            fail("Unable to set value for \"" + element.getText() + "\"" + " Error: " + e.getMessage());
        }
    }

    /**
     * @param locator
     * @param text
     */
    public void setValue(String locator, String text) {
        WebElement element = waitForElement(locator);
        try {
            setAttribute(element, "value", text);
        } catch (Exception e) {
            fail("Unable to set value for \"" + getLocatorName(locator) + "\"" + " Error: " + e.getMessage());
        }
    }

    /**
     * Un-check checkbox specified by locator
     */
    public void uncheck(String locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            try {
                if (element.isSelected()) {
                    element.click();
                }
            } catch (Exception e) {
                fail("Exception while check");
            }
            info("UnCheck \"" + getLocatorName(locator) + "\"");
        }

    }

    /**
     * Verify attribute Value of specified attribute matches expected value Log
     * verification result to report
     */
    public void verifyAttribute(String locator, String attributeName, String expectedValue) {
        String actualValue = getAttribute(locator, attributeName);
        if (actualValue.contains(expectedValue)) {
            info("Verified Expected Attribute value: " + expectedValue + " for Element: " + getLocatorName(locator) + " attribute: " + attributeName);
        } else {
            info("Incorrect Attribute, Expected : " + expectedValue + " \nActual: " + actualValue + " for Element: " + getLocatorName(locator) + " attribute: " + attributeName);
        }

    }

    /**
     * Wait for element present for default timeout 30 seconds. Fail and Stop
     * Script if not found and log in report
     */
    public WebElement waitForElement(String locator) {
        return waitForElement(locator, timeout, false, false, true, true);
    }

    /**
     * Wait for element present for specified timeout in seconds. Fail and Stop
     * Script if not found and log in report
     */
    public WebElement waitForElement(String locator, int timeout) {
        return waitForElement(locator, timeout, false, true, true, true);
    }

    /**
     * Wait for element with specified options if the element has just appeared
     * 1 second before on page indicating that the page is still loading, it
     * waits for 5 seconds to allow page load completely and searches the
     * element again to avoid stale element exception.
     *
     * @param locator         - element locator
     * @param timeout         - seconds to wait for element
     * @param soft            - if true, log fail message in report and soft fail Script(At
     *                        End of Execution); if false - halts further script execution
     *                        if fail with fail log
     * @param report          - log pass/fail result to report
     * @param present         - if true, check for element presence; if false - check for
     *                        element absence
     * @param visibilityCheck - if true, check for isDisplayed(). Set this false when
     *                        sometimes element is visually present but isDisplayed()
     *                        returns false
     * @return WebElement - returns found WebElement if found, else returns null
     */
    public WebElement waitForElement(String locator, int timeout, boolean soft, boolean report, boolean present, boolean visibilityCheck) {
        WebElement element = null;
        boolean visibilityCheckPrev = this.visibilityCheck;
        // setVisibilityCheck(visibilityCheck);
        wait(Integer.parseInt(LoadProperties.getProperty("stepWait", "0")));
        for (int seconds = 0; ; seconds++) {
            if (seconds >= timeout) {
                if (!present) {
                    if (showVerifications && report) {
                        info("UnExpected element : " + getLocatorName(locator) + " NOT present.");
                    }
                    break;
                } else {
                    if (!soft) {
                        fail("\"" + getLocatorName(locator) + "\" not found on page, Wait:" + seconds + getError());
                    } else if (report) {
                        info("\"" + getLocatorName(locator) + "\" not found on page, Wait:" + seconds);
                    }
                    break;
                }

            }
            element = findElementM(locator);
            if (element != null) {
                if (!present) {
                    if (report) {
                        info("UnExpected Element: \"" + getLocatorName(locator) + " found on page, Wait:" + seconds);
                        element = null;
                        break;
                    }
                } else {
                    if (seconds > 0) { // seconds>0 indicates that the element
                        // has just appeared 1 second before on
                        // page and page is still loading,
                        wait(5000);// so need to wait for 5 seconds to allow the
                        // page load completely.
                        element = findElementM(locator); // searching the
                        // element again
                        // after 5 seconds
                        // to avoid stale
                        // element
                        // exception.
                        if (showVerifications && report) {
                            info("Verify \"" + getLocatorName(locator) + "\" present.");
                        }

                    }
                    break;
                }

            }
            wait(1000);
        }
        setVisibilityCheck(visibilityCheckPrev);
        return element;
    }

    /**
     * Wait for element NOT present for 5 seconds by default Soft Fail Script(At
     * End of Execution) if not found and log in report
     *
     * @return WebElement if found else return null
     */
    public WebElement waitForElementNotPresent(String locator) {
        return waitForElementNotPresent(locator, 5);
    }

    /**
     * Wait for element NOT present for specified timeout in seconds Soft Fail
     * Script(At End of Execution) if not found and log in report
     *
     * @return WebElement if found else return null
     */
    public WebElement waitForElementNotPresent(String locator, int timeout) {
        return waitForElement(locator, timeout, true, true, false, true);
    }

    /**
     * @param locator
     * @return
     */
    public boolean waitForElementVisible(String locator) {
        return waitForElementVisible(locator, 5);
    }

    /**
     * @param locator
     * @param timeout
     * @return
     */
    public boolean waitForElementVisible(String locator, int timeout) {
        if (waitForElement(locator, timeout, true, true, true, true) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param expectedTitle
     * @return
     */
    public boolean waitForTitle(String expectedTitle) {
        return waitForTitle(expectedTitle, timeout / 2, "");
    }

    /**
     * @param expectedTitle
     * @param timeout
     * @param tName
     * @return
     */
    public boolean waitForTitle(String expectedTitle, int timeout, String tName) {
        String actualTitle = "";
        for (int seconds = 0; ; seconds++) {
            actualTitle = getDriver().getTitle().trim();
            if (seconds >= timeout) {
                info("Expected Value : " + expectedTitle + "\n" + "Actual Value : " + actualTitle + "  <br> " + tName + " :: FAIL");
                error(tName + "::::" + "FAILED");
                return false;
            }
            if (actualTitle.contains(expectedTitle.trim())) {
                System.setProperty("org.uncommons.reportng.escape-output", "false");
                info("driver value :: " + getDriver().toString());
                info(tName + "::::" + "PASSED");
                return true;
            }
            wait(1000);
        }
    }

    /**
     * Switch to specific frame
     *
     * @param locator
     */
    public void switchToFrame(String locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            try {
                getDriver().switchTo().frame(element);
            } catch (Exception e) {
                info(e.getMessage());
                fail("Exception while switching to frame \"" + getLocatorName(locator) + "\"");
            }
            info("Switched to frame \"" + getLocatorName(locator) + "\"");
        }
    }

    /**
     * Function to Switch to default content
     */
    public void switchToDefaultFrame() {
        try {
            getDriver().switchTo().defaultContent();
        } catch (Exception e) {
            info(e.getMessage());
            fail("Exception while switching to defaultContent");
        }
        info("Switched to default frame (defaultContent)");
    }
}
package base;

import helpers.GridHealthCheck;
import helpers.LoadProperties;
import helpers.Logfile;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TestBase extends Logfile {

    DesiredCapabilities capabilities = new DesiredCapabilities();
    public String os = System.getProperty("os.name").toLowerCase();
    public String os_arch = System.getProperty("os.arch").toLowerCase();
    URL hubUrl = null;

    /**
     * should be called for every test before execution
     *
     * @param browser
     * @param platform
     * @param os_arch
     * @throws IOException
     */
    public void initialize(String browser, String platform, String os_arch) throws IOException {
        dr = defineBrowser(browser, platform, os_arch);
        get().setWebDriver(dr);
        getDriver().manage().window().maximize();
    }

    /**
     *
     */
    public void launchNewBrowserInstance() {
        try {
            String OS = null;
            if (os.indexOf("win") >= 0)
                OS = "windows";
            else if (os.indexOf("mac") >= 0)
                OS = "mac";
            else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0)
                OS = "linux";
            else if (os.indexOf("sunos") >= 0)
                OS = "solaris";
            String arch = os_arch.toLowerCase().contains("64") ? "x64" : "x86";
            initialize(LoadProperties.getProperty("browser", "chrome"), OS, arch);
        } catch (Exception e) {
            e.printStackTrace();
            error(e.getMessage());
        }
    }

    /**
     *
     * @param browser
     * @param os
     * @param arch
     * @return
     * @throws IOException
     */
    public synchronized WebDriver defineBrowser(String browser, String os, String arch) throws IOException {
        switch (os) {
            case "windows":
                capabilities.setPlatform(Platform.WINDOWS);
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//geckodriver.exe");
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//chromedriver.exe");
                System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//IEDriverServer.exe");
                System.setProperty("webdriver.edge.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//edge.exe");
                break;
            case "mac":
                capabilities.setPlatform(Platform.MAC);
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//geckodriver");
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//chromedriver");
                break;
            case "linux":
                capabilities.setPlatform(Platform.LINUX);
                System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//geckodriver");
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") +
                        "//libs//drivers//" + os + "//" + arch + "//chromedriver");
                break;
            default:
                error("Invalid os");
        }

        if (LoadProperties.getProperty("gridBasedExecution").toLowerCase().
                equalsIgnoreCase("false")) {
            switch (browser.toLowerCase()) {
                case "firefox":
                    dr = new FirefoxDriver();
                    break;
                case "chrome":
                    dr = new ChromeDriver();
                    break;
                case "chrome_headless":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--disable-gpu");
                    dr = new ChromeDriver(chromeOptions);
                    break;
                case "safari":
                    dr = new SafariDriver();
                    break;
                case "ie":
                case "internet explorer":
                case "iexplore":
                case "iexplorer":
                case "internetexplorer":
                    dr = new InternetExplorerDriver();
                    break;
                case "edge":
                    dr = new EdgeDriver();
                    dr.manage().deleteAllCookies();
                    dr.manage().window().maximize();
                    break;
                default:
                    info("Please select the correct browser type");
                    break;
            }
        } else if (LoadProperties.getProperty("gridBasedExecution").toLowerCase().
                equalsIgnoreCase("true")) {
                       if (LoadProperties.getProperty("isSeleniumGridEnabled").toLowerCase().
                    equalsIgnoreCase("false")) {
                startGrid(LoadProperties.HOST, LoadProperties.PORT);
            }

            try {
                hubUrl = new URL("http://" + LoadProperties.HOST + ":" + LoadProperties.PORT + "/wd/hub");
                info("Selenium Hub URL :: " + hubUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                error("Error occurred @ HubURL Definition");
            }
            switch (browser.toLowerCase()) {
                case "firefox":
                    capabilities.setCapability("marionette", true);
                    FirefoxProfile profile = new FirefoxProfile();
                    profile.setPreference("browser.download.folderList", 2);
                    capabilities.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
                    dr = getRemoteWebDriver();
                    break;
                case "chrome":
                    capabilities.setBrowserName(DesiredCapabilities.chrome().getBrowserName());
                    dr = getRemoteWebDriver();
                    break;
                case "safari":
                    capabilities.setBrowserName(DesiredCapabilities.safari().getBrowserName());
                    dr = getRemoteWebDriver();
                    break;
                case "ie":
                case "internet explorer":
                case "internetexplorer":
                    capabilities.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
                    capabilities.setCapability("ignoreZoomSetting", true);
                    capabilities.setCapability("ignoreProtectedModeSettings", true);
                    dr = getRemoteWebDriver();
                    break;
                case "edge":
                    capabilities.setBrowserName(DesiredCapabilities.edge().getBrowserName());
                    dr = getRemoteWebDriver();
                    break;
                case "htmlunitdriver":
                    capabilities.setJavascriptEnabled(true);
                    capabilities.setBrowserName(DesiredCapabilities.htmlUnit().getBrowserName());
                    dr = getRemoteWebDriver();
                    break;
                default:
                    error("Please select the correct browser type");
                    break;
            }

        }
        return dr;
    }

    /**
     *
     * @return
     */
    private RemoteWebDriver getRemoteWebDriver() {
        try {
            return new RemoteWebDriver(hubUrl, capabilities);
        } catch (WebDriverException e) {
            if (e.getMessage().contains("Unable to bind")) {
                try {
                    Thread.sleep(Integer.parseInt(LoadProperties.getProperty("bindExceptionTimeOut", "60000")));
                    return new RemoteWebDriver(hubUrl, capabilities);
                } catch (Exception e1) {
                    info("Error while retrying to initialize webdriver");
                    error(e1.getMessage());
                    e1.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param host
     * @param port
     * @return
     */
    public String checkGridStatus(String host, String port) throws IOException {
        return GridHealthCheck.getGridStatus(host, port);
    }

    /**
     *
     * @param host
     * @param port
     * @throws IOException
     */
    public void startGrid(String host, String port) throws IOException {
        if (checkGridStatus(host, port).equalsIgnoreCase("true")) {
            System.out.println("@@@@@@@@@@@@@@@@@  SELENIUM GRID WITH GIVEN HOST AND PORT IS ALREADY UP & RUNNING  @@@@@@@@@@@@@@@@@");
        } else {
            File dir = new File("gridconfiguration");
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/C",
                    "Start", "rungrid.bat");
            processBuilder.directory(dir);
            try {
                processBuilder.start();
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    error(e.fillInStackTrace().toString());
                }
            } catch (IOException e) {
                error(e.fillInStackTrace().toString());
            }
        }
    }

    /**
     * This method is used to close the browser
     */
    public synchronized void browserClose() {
        info("Browser Closed");
        try {
            getDriver().quit();
        } catch (Exception e) {
            error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public byte[] getScreenshot() {
        return ((TakesScreenshot) new Augmenter().augment(getDriver())).getScreenshotAs(OutputType.BYTES);
    }

    /**
     *
     */
    public void captureScreenshotAndAttachToReport() {
        getScenario().embed(getScreenshot(), "image/png");
    }
}

package helpers;

import base.TestContext;
import org.apache.log4j.*;
import org.testng.Assert;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Properties;

public class Logfile extends TestContext {

    public static Category log = writeToCommonLog();
    private static final String date = FormatDates.getCurrentDateTime();

    /**
     *
     * @return
     */
    private static Category writeToCommonLog() {
        initializeLogger();
        log = Category.getInstance(Logfile.class);
        return log;
    }

    private static LinkedHashMap<String, Category> logStore = new LinkedHashMap<>();
    public static ThreadLocal<String> testName = new ThreadLocal<>();

    /**
     *
     * @param myTestCaseName
     * @return
     * @throws Exception
     */
    public static synchronized Category writeToTestLog(String myTestCaseName) throws Exception {
        //log = initializeTestLogger(myTestCaseName);
        testName.set(myTestCaseName);
        try {
            logStore.put(testName.get(), initializeTestLogger(myTestCaseName));
        }catch (FileNotFoundException e){
            try {
                if(new File("log/" + date + "/" + testName.get() + ".log").createNewFile())
                    log.info("Log file for Test Case [" + myTestCaseName + "] is created successfully");
                else
                    log.error("Failed to created Log file for Test Case [" + myTestCaseName + "]");
            }catch (Exception e1){
                log.error(e1.getMessage());
                throw new Exception(e1);
            }
        }
        logStore.get(testName.get()).info("Setting up Test Case ["+ testName.get() +"] Log File ");
        return logStore.get(testName.get());
    }

    /**
     *
     * @param myTestCaseName
     * @return
     * @throws Exception
     */
    private static synchronized Logger initializeTestLogger(String myTestCaseName) throws Exception {
        // Initialize the logger
        String loggerId = myTestCaseName + "_" + Thread.currentThread().getId();
        Logger myLogger = Logger.getLogger(loggerId);
        FileAppender myFileAppender;
        try {
            PatternLayout layout = new PatternLayout();
            String conversionPattern = "%d{yyyy-MM-dd HH:mm:ss} [%-5p] [%c{1}] - [%M] %m%n";
            layout.setConversionPattern(conversionPattern);

            // creates console appender
            ConsoleAppender consoleAppender = new ConsoleAppender();
            consoleAppender.setLayout(layout);
            consoleAppender.activateOptions();

            myFileAppender = new FileAppender(layout, "log/" + date + "/"
                    + myTestCaseName + ".log", false);
            myFileAppender.activateOptions();
            myLogger.addAppender(myFileAppender);
        } catch (IOException e1) {
            myLogger.info(e1.getMessage());
            log.error(e1.getMessage());
            throw new Exception(e1);
        }
        return myLogger;
    }

    /**
     *
     */
    private static void initializeLogger() {
        Properties logProperties = new Properties();
        try {
            // load our log4j properties / configuration file
            logProperties.load(new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/config/log4j.properties"));
            PropertyConfigurator.configure(logProperties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param msg
     */
    protected void error(String msg) {
        logStore.get(testName.get()).error(msg);
        getScenario().write(msg);
    }

    /**
     *
     * @param msg
     */
    protected void fail(String msg) {
        logStore.get(testName.get()).error(msg);
        getScenario().write(msg);
        Assert.fail(msg);
    }

    /**
     *
     * @param msg
     */
    protected void success(String msg) {
        logStore.get(testName.get()).info(msg);
        getScenario().write(msg);
    }

    /**
     *
     * @param msg
     */
    protected void info(String msg) {
        logStore.get(testName.get()).info(msg);
        getScenario().write(msg);
    }

    /**
     *
     * @param msg
     */
    public static void flog(String msg) {
        flog("flog.html", msg);
    }

    /**
     *
     * @param fileName
     * @param msg
     * @param suffix
     */
    public static void flog(String fileName, String msg, String suffix) {
        try {
            FileWriter fw = new FileWriter(fileName,true);
            fw.write(msg+suffix);
            fw.close();
        } catch (Exception e) {}
    }

    /**
     *
     * @param fileName
     * @param msg
     */
    public static void flog(String fileName, String msg) {
        flog(fileName, msg, "<br>\n");
    }
}

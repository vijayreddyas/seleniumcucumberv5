package helpers;

import java.io.FileReader;
import java.util.Properties;

public class LoadProperties {
    private static Properties properties=new Properties();

    public static final String UI_URL = getProperty("ui.url", "");

    //Selenium - Remote Web Driver Properties
    public static final String HOST=  getProperty("host","localhost");
    public static final String PORT=  getProperty("port","4444");

    /**
     *
     * @return
     */
    public static Properties getAllProperties(){
        try {
            properties.load(new FileReader(System.getProperty("user.dir") + "/src/test/resources/config/test.properties"));
            return properties;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue){
        if (getAllProperties().containsKey(key))
            return getProperty(key);
        else if(!properties.containsKey(key) && (!defaultValue.equalsIgnoreCase("") || defaultValue!=""||!defaultValue.isEmpty()||defaultValue!=null))
            return defaultValue;
        else if (!properties.containsKey(key) && (defaultValue.equalsIgnoreCase("") || defaultValue==""||defaultValue.isEmpty()||defaultValue==null))
            return null;
        else
            return null;
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getProperty(String key){
        if(getAllProperties().containsKey(key))
            return getAllProperties().get(key).toString();
        else
            return null;
    }

}
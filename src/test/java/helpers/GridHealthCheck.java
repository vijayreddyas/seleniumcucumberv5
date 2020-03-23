package helpers;

import io.restassured.path.json.JsonPath;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.fail;

public class GridHealthCheck {

    /**
     *
     * @param host
     * @param port
     * @return
     * @throws IOException
     */
    public static String getGridStatus(String host, String port) throws IOException {
        final String GET_URL = "http://" + host + ":" + port + "/wd/hub/status";
        StringBuffer response = null;
        URL obj = new URL(GET_URL);
        String status = "";
        HttpURLConnection con = null;
        boolean flag = true;
        try {
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.getResponseCode();
        }catch(ConnectException e){
            status = "false";
            flag = false;
        }
        if(flag) {
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                fail("GET request not worked");
            }
            JsonPath js = new JsonPath(response.toString());
            status = js.getString("value.ready");
        }
        return status;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(getGridStatus("localhost", "4444"));
        System.out.println("GET DONE");
    }
}

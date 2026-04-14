package com.acebank.lite.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsUtil {

    public static void sendSMS(String mobile, String message){

        try{
            String apiKey = ConfigLoader.getProperty("sms.fast2sms.api.key");

            String payload = "{\n" +
                    "  \"route\": \"q\",\n" +
                    "  \"message\": \""+message+"\",\n" +
                    "  \"language\": \"english\",\n" +
                    "  \"flash\": 0,\n" +
                    "  \"numbers\": \""+mobile+"\"\n" +
                    "}";

            URL url = new URL("https://www.fast2sms.com/dev/bulkV2");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("authorization", apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            System.out.println("SMS sent. Response: " + responseCode);

        }catch(Exception e){
            System.out.println("SMS failed: "+e.getMessage());
        }
    }
}
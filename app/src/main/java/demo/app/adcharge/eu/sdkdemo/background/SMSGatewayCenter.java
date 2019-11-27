package demo.app.adcharge.eu.sdkdemo.background;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SMSGatewayCenter{
        public static void main(String[] args)
        {
//Your apikey key
            String apiKey = "YourApiKey";
//OR
            String userId = "callpay";
            String password = "Raza#Winsome1";

//Send Method
            String sendMethod = "simpleMsg";

//Message type text/unicode/flash
            String msgType = "text";

//Multiple mobiles numbers separated by comma
            String mobile = "91999xxxxxxx";
//Your approved sender id
            String senderId = "SGCSMS";
//Your message to terminate, URLEncode the content
            String msg = "Hello world";
//response format
            String format = "json";

//Prepare Url
            URLConnection myURLConnection=null;
            URL myURL=null;
            BufferedReader reader=null;

//URL encode message
            String urlencodedmsg= URLEncoder.encode(msg);

//API End Point
            String mainUrl="http://www.smsgateway.center/SMSApi/rest/send?";

//API Paramters
            StringBuilder sendSmsData= new StringBuilder(mainUrl);
            sendSmsData.append("apiKey="+apiKey);
            sendSmsData.append("&userId="+userId);
            sendSmsData.append("&password="+password);
            sendSmsData.append("&sendMethod="+sendMethod);
            sendSmsData.append("&msgType="+msgType);
            sendSmsData.append("&mobile="+mobile);
            sendSmsData.append("&senderId="+senderId);
            sendSmsData.append("&msg="+urlencodedmsg);
            sendSmsData.append("&format="+format);
//final string
            mainUrl = sendSmsData.toString();
            try
            {
//prepare connection
                myURL = new URL(mainUrl);
                myURLConnection = myURL.openConnection();
                myURLConnection.connect();
                reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
//reading response
                String response;
                while ((response = reader.readLine()) != null)
//print response
                    System.out.println(response);

//finally close connection
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
}
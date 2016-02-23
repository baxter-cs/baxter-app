package org.baxter_academy.flex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Toast;

/**
 * Created by John on 2/22/2016.
 *
 * Example Usage:
 *
 * RestClient client = new RestClient("http://www.example.com/demo.php");  //Write your url here
 *  client.addParam("Name", "Bhavit"); //Here I am adding key-value parameters
 *  client.addParam("Age", "23");
 *
 *  client.addHeader("content-type", "application/json"); // Here I am specifying that the key-value pairs are sent in the JSON format
 *
 *  try {
 *      String response = client.executePost(); // In case your server sends any response back, it will be saved in this response string.
 *
 *  } catch (Exception e) {
 *      e.printStackTrace();
 *  }
 */
public class RESTClient {
    JSONObject data = new JSONObject();
    String url;
    String headerName;
    String headerValue;

    public RESTClient(String s){
        url = s;
    }


    public void addHeader(String name, String value){
        headerName = name;
        headerValue = value;

    }

    public void addParam(String key, String value){
        try {
            data.put(key, value);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public String executePost(){  // If you want to use post method to hit server
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(headerName, headerValue);
        HttpResponse response = null;
        String result = null;
        try {
            StringEntity entity = new StringEntity(data.toString(), HTTP.UTF_8);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            HttpEntity entity1 = response.getEntity();
            result = EntityUtils.toString(entity1);
            return result;
            //Toast.makeText(MainPage.this, result, Toast.LENGTH_LONG).show();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public String executeGet(){ //If you want to use get method to hit server
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        String result = null;
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            result = httpClient.execute(httpget, responseHandler);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}

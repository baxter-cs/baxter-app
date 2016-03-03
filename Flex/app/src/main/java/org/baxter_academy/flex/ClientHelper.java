package org.baxter_academy.flex;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by John on 2/24/2016.
 */
public class ClientHelper {

    public String newTask(String mTask, String mDescription, String mAssignee, String mDueDate, String uuid) {
        RESTClient client = new RESTClient(Constants.server_address_newTask);
        client.addParam("mTask", mTask);
        client.addParam("mDescription", mDescription);
        client.addParam("mAssignee", mAssignee);
        client.addParam("mDueDate", mDueDate);
        client.addParam("uuid", uuid);
        client.addParam("mOwner", "personal");
        client.addHeader("content-type", "application/json");

        String response;

        try {
            response = client.executePost();
        } catch (Exception e) {
            response = "error";
        }

        if (!response.equals("error")) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(response).getAsJsonObject();
            return json.get("response").getAsString();
        } else {
            return response;
        }
    }

    public JsonObject getTasks(String uuid) {
        RESTClient client = new RESTClient(Constants.server_address_getTasks);
        client.addParam("uuid", uuid);
        client.addParam("scope", "personal");
        client.addHeader("content-type", "application/json");

        JsonParser parser = new JsonParser();
        return parser.parse(client.executePost()).getAsJsonObject();
    }

    public String upgradeTaskStatus(String mID, String uuid) {
        RESTClient client = new RESTClient(Constants.server_address_upgradeStatus);
        client.addHeader("content-type", "application/json");
        client.addParam("mID", mID);
        client.addParam("uuid", uuid);

        String response;

        try {
            response = client.executePost();
        } catch (Exception e) {
            response = "error";
        }

        if (!response.equals("error")) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(response).getAsJsonObject();
            return json.get("response").getAsString();
        } else {
            return response;
        }
    }

    public String deleteTask(String mID, String uuid) {
        RESTClient client = new RESTClient(Constants.server_address_deleteTask);
        client.addHeader("content-type", "application/json");
        client.addParam("mID", mID);
        client.addParam("uuid", uuid);

        String response;

        try {
            response = client.executePost();
        } catch (Exception e) {
            response = "error";
        }

        if (!response.equals("error")) {
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(response).getAsJsonObject();
            return json.get("response").getAsString();
        } else {
            return response;
        }
    }

    public String verifyLogin(String uuid) {
        RESTClient client = new RESTClient(Constants.server_address_verifyLogin);
        client.addHeader("content-type", "application/json");
        client.addParam("uuid", uuid);

        String response;

        try {
            response = client.executePost();
        } catch (Exception e) {
            response = "error";
        }

        JsonParser parser = new JsonParser();

        JsonObject json = parser.parse(response).getAsJsonObject();

        return json.get("response").getAsString();
    }

    public String login(String username, String password) {
        RESTClient client = new RESTClient(Constants.server_address_login);
        client.addHeader("content-type", "application/json");
        client.addParam("username", username);
        client.addParam("password", password);

        String response;

        try {
            response = client.executePost();
        } catch (Exception e) {
            response = "invalid";
        }

        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(response).getAsJsonObject();

        return json.get("data").getAsString();
    }

    public String signUp(String username, String password, String email) {
        RESTClient client = new RESTClient(Constants.server_address_signUp);
        client.addHeader("content-type", "application/json");
        client.addParam("username", username);
        client.addParam("password", password);
        client.addParam("email", email);

        String response;

        try {
            response = client.executePost();
        } catch (Exception e) {
            response = "exception error";
        }

        return response;
    }
}

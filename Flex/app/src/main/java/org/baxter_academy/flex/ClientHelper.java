package org.baxter_academy.flex;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Created by John on 2/24/2016.
 */
public class ClientHelper {
    public String newTask(String mTask, String mDescription, String mAssignee, String mDueDate) {
        RESTClient client = new RESTClient(Constants.server_address_newTask);
        client.addParam("mTask", mTask);
        client.addParam("mDescription", mDescription);
        client.addParam("mAssignee", mAssignee);
        client.addParam("mDueDate", mDueDate);
        client.addHeader("content-type", "application/json");

        return client.executePost();
    }

    public JsonObject getTasks() {
        RESTClient client = new RESTClient(Constants.server_address_getTasks);
        client.addHeader("content-type", "application/json");

        JsonParser parser = new JsonParser();
        return parser.parse(client.executeGet()).getAsJsonObject();
    }
}

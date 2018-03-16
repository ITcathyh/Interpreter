package com.cheelem.interpreter.network;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;

/**
 * Created by 黄宇航 on 2018/3/2.
 */

public class MySocketClient extends WebSocketClient {
    private String id = null;
    private String sessionid = "default";

    MySocketClient(URI serveruri, String id) {
        super(serveruri, new Draft_17());
        this.id = id;
    }

    private void dealMsg(JSONObject json) {
        String method = null;

        try {
            JSONObject params = json.getJSONObject("params");
            method = json.getString("method");
            String id = json.getString("id");

            if (params == null || method == null || id == null) {
                resendRequest(method);
                return;
            }

            if (method.equals("session-id")) {
                String sessionid = params.getString("session-id");
                String viewurl = params.getString("view-url");
                this.sessionid = sessionid;
            } else if (method.equals("error-permission-denied")) {
                String message = params.getString("message");
                String code = params.getString("code");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            resendRequest(method);
        }
    }

    private static void resendRequest(String method) {
        if (method != null) {

        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(id, "Open");
    }

    @Override
    public void onMessage(String message) {
        try {
            dealMsg(new JSONObject(message));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(id, "Closed:" + reason);
    }

    @Override
    public void onError(Exception ex) {
        Log.i(id, "Error");
        ex.printStackTrace();
    }

    public void send(JSONObject json){
        super.send(json.toString());
    }

    boolean sendMsg(String method, JSONObject params) {
        JSONObject json = new JSONObject();

        try {
            json.put("method", method);
            json.put("params", params);
            json.put("id", id);

            send(json.toString());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getId() {
        return id;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }
}

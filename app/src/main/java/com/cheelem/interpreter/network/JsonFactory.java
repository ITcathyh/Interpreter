package com.cheelem.interpreter.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 黄宇航 on 2018/3/2.
 */

public class JsonFactory {
    public static JSONObject getInitJson(String deviceid, String fromlang,
                                         String tolang, String mode, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("device-id", deviceid);
            params.put("from-lang", fromlang);
            params.put("to-lang", tolang);
            params.put("mode", mode);
            json.put("method", "initialize");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JSONObject getInitJson(String deviceid, String fromlang,
                                         String tolang, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("device-id", deviceid);
            params.put("from-lang", fromlang);
            params.put("to-lang", tolang);
            //params.put("mode", null);
            json.put("method", "initialize");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JSONObject getPartialTransResultJson(String sessionid, String sentenceid,
                                                       String recogresult, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("session-id", sessionid);
            params.put("sentence-id", sentenceid);
            params.put("recog-result", recogresult);
            json.put("method", "partial-recog-result");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JSONObject getEntireTransResultJson(String sessionid, String sentenceid,
                                                      String recogresult, String transresult, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("session-id", sessionid);
            params.put("sentence-id", sentenceid);
            params.put("recog-result", recogresult);
            params.put("trans-result", transresult);
            json.put("method", "final-recog-result");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JSONObject getEndJson(String sessionid, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("session-id", sessionid);
            json.put("method", "terminate-session");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JSONObject getContinueJson(String deviceid, String fromlang,
                                             String tolang, String mode, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("device-id", deviceid);
            params.put("from-lang", fromlang);
            params.put("to-lang", tolang);
            params.put("mode", mode);
            json.put("method", "continue");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public static JSONObject getContinueJson(String deviceid, String fromlang,
                                             String tolang, String id) {
        JSONObject json = new JSONObject();
        JSONObject params = new JSONObject();

        try {
            params.put("device-id", deviceid);
            params.put("from-lang", fromlang);
            params.put("to-lang", tolang);
            //params.put("mode", null);
            json.put("method", "continue");
            json.put("params", params);
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

}

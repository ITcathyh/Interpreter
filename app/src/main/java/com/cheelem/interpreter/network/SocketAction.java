package com.cheelem.interpreter.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 黄宇航 on 2018/2/28.
 */

public class SocketAction {
    public static void sendMsg(final String method, final HashMap<String, Object> params, MySocketClient client) {
        if (params == null || params.size() == 0 || method == null || client == null) {
            Log.i(SocketAction.class.getSimpleName(), "SendMsg error");
            return;
        }

        new Thread(new SendThread(client, params, method)).start();
    }

    /*
     * 内部线程类
     */
    private static final class SendThread implements Runnable {
        private MySocketClient client = null;
        private HashMap<String, Object> params = null;
        private String method = null;

        private SendThread(final MySocketClient client, final HashMap<String, Object> params, String method) {
            this.client = client;
            this.params = params;
            this.method = method;
        }

        @Override
        public void run() {
            JSONObject json = new JSONObject();
            Iterator iter = params.entrySet().iterator();
            Object key, value;
            Map.Entry entry;

            try {
                while (iter.hasNext()) {
                    entry = (Map.Entry) iter.next();
                    key = entry.getKey();
                    value = entry.getValue();

                    json.put((String) key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i(this.getClass().getSimpleName(), client.getId() + " SendMsg error");
                return;
            }

            if (!client.sendMsg(method, json)) {
                Log.i(this.getClass().getSimpleName(), client.getId() + " SendMsg error");
            }
        }
    }
}

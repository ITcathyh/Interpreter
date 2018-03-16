package com.cheelem.interpreter.network;

import android.util.Log;

import org.java_websocket.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by 黄宇航 on 2018/2/28.
 */

public class ConnectAction {
    private static final String CONNECT_URI = "127.0.0.1";

    public static MySocketClient getClient(String id) {
        MySocketClient client = null;

        try {
            URI uri = new URI(CONNECT_URI);
            client = new MySocketClient(uri, id);

            client.connect();

            if (!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                Log.i(ConnectAction.class.getSimpleName(), "Connect error");
                return null;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        return client;
    }
}

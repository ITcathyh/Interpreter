package com.translator.http;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHttpParams implements HttpParams {
    protected final Map<String, String> params = new HashMap<>();

    @Override
    public HttpParams put(String key, String value) {
        params.put(key, value);
        return this;
    }

    @Override
    public String send2String(String baseUrl) throws Exception {
        String result = send(baseUrl);
        System.out.println("[RESPONSE]:" + result);
        return result;
    }

    abstract protected String send(String baseUrl) throws Exception;

}

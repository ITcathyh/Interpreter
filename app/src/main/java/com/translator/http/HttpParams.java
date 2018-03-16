package com.translator.http;

public interface HttpParams {
    public String send2String(String baseUrl) throws Exception;

    public HttpParams put(String key, String value);
}

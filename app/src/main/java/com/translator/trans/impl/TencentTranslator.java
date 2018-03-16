package com.translator.trans.impl;

import com.translator.http.HttpParams;
import com.translator.http.HttpPostParams;
import com.translator.trans.AbstractOnlineTranslator;
import com.translator.trans.LANG;
import com.translator.trans.annotation.TranslatorComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@TranslatorComponent(id = "tencent")
final public class TencentTranslator extends AbstractOnlineTranslator {

    public TencentTranslator() {
        langMap.put(LANG.EN, "1");
        langMap.put(LANG.ZH, "0");
    }

    @Override
    protected String getResponse(LANG from, LANG targ, String query) throws Exception {
        HttpParams params = new HttpPostParams()
                .put("sl", langMap.get(from))
                .put("tl", langMap.get(targ))
                .put("st", query);

        return params.send2String("http://fanyi.qq.com/api/translate");
    }

    @Override
    protected String parseString(String jsonString) {
        try {
            StringBuilder str = new StringBuilder();
            JSONObject rootObj = null;

            rootObj = new JSONObject(jsonString);

            JSONArray array = rootObj.getJSONArray("result");

            for (int i = 0; i < array.length(); i++) {
                str.append(array.getJSONObject(i).getString("dst"));
            }
            return str.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}

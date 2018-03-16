package com.translator.trans.impl;

import com.translator.http.HttpParams;
import com.translator.http.HttpPostParams;
import com.translator.trans.AbstractOnlineTranslator;
import com.translator.trans.LANG;
import com.translator.trans.annotation.TranslatorComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@TranslatorComponent(id = "baidu")
final public class BaiduTranslator extends AbstractOnlineTranslator {

    public BaiduTranslator() {
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.ZH, "zh");
    }

    @Override
    public String getResponse(LANG from, LANG targ, String query) throws Exception {

        HttpParams params = new HttpPostParams()
                .put("from", langMap.get(from))
                .put("to", langMap.get(targ))
                .put("query", query)
                .put("transtype", "translang")
                .put("simple_means_flag", "3");

        return params.send2String("http://fanyi.baidu.com/v2transapi");
    }

    @Override
    protected String parseString(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);

            JSONArray segments = jsonObject.getJSONObject("trans_result").getJSONArray("data");
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < segments.length(); i++) {
                result.append(i == 0 ? "" : "\n");
                result.append(segments.getJSONObject(i).getString("dst"));
            }
            return new String(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}

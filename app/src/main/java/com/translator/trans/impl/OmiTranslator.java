package com.translator.trans.impl;

import com.translator.http.HttpParams;
import com.translator.http.HttpPostParams;
import com.translator.trans.AbstractOnlineTranslator;
import com.translator.trans.LANG;
import com.translator.trans.annotation.TranslatorComponent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@TranslatorComponent(id = "omi")
final public class OmiTranslator extends AbstractOnlineTranslator {

    public OmiTranslator() {
        langMap.put(LANG.EN, "e");
        langMap.put(LANG.ZH, "c");
    }

    @Override
    public String getResponse(LANG from, LANG targ, String query) throws Exception {

        HttpParams params = new HttpPostParams()
                .put("languageType", langMap.get(from) + "2" + langMap.get(targ))
                .put("userDbName", "")
                .put("sentsToTrans", query);

        return params.send2String("http://www.alifanyi1688.com/transSents.do");
    }

    @Override
    protected String parseString(String jsonString) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);

            JSONArray segments = jsonObject.getJSONArray("sentsResults").getJSONArray(1);
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < segments.length(); i++) {
                result.append(segments.getString(i));
            }
            return result.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.translator.trans.impl;

import com.translator.http.HttpParams;
import com.translator.http.HttpPostParams;
import com.translator.trans.AbstractOnlineTranslator;
import com.translator.trans.LANG;
import com.translator.trans.annotation.TranslatorComponent;

import org.json.JSONException;
import org.json.JSONObject;

@TranslatorComponent(id = "jinshan")
final public class JinshanTranslator extends AbstractOnlineTranslator {

    public JinshanTranslator() {
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.ZH, "zh");
    }

    @Override
    protected String getResponse(LANG from, LANG targ, String query) throws Exception {
        HttpParams params = new HttpPostParams()
                .put("f", langMap.get(from))
                .put("t", langMap.get(targ))
                .put("w", query);

        return params.send2String("http://fy.iciba.com/ajax.php?a=fy");
    }


    @Override
    protected String parseString(String jsonString) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);

            String result = jsonObject.getJSONObject("content").getString("out");
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

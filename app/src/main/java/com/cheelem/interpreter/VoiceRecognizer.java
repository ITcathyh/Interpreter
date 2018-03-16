package com.cheelem.interpreter;

import android.app.Activity;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.translator.trans.exception.DupIdException;
import com.translator.trans.factory.TranslatorFactory;

import java.net.URISyntaxException;

/**
 * Created by LLG on 2018/2/23.
 */

public class VoiceRecognizer {
    static EventManager recognizationManager;
    static TranslatorFactory translatorFactory;

    static void initialize(Activity activity) {
        try {
            recognizationManager = EventManagerFactory.create(activity, "asr");
            translatorFactory = new TranslatorFactory();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (DupIdException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    static void registerListener(EventListener listener) {
        recognizationManager.registerListener(listener);
    }

    static EventManager getRecognizer() {
        return recognizationManager;
    }

    static TranslatorFactory getTranslator() {
        return translatorFactory;
    }
}

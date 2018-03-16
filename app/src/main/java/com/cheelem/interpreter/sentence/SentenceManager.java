package com.cheelem.interpreter.sentence;

import com.cheelem.interpreter.entity.Sentence;
import com.cheelem.interpreter.network.JsonFactory;
import com.cheelem.interpreter.network.MySocketClient;
import com.cheelem.interpreter.util.GetRandomString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄宇航 on 2018/3/7.
 */

public class SentenceManager {
    volatile private static SentenceManager instance = null;
    private List<Sentence> sentencelist = null;
    private Map<String, Sentence> sentenceMap = null;

    private SentenceManager() {
        sentenceMap = new HashMap<>();
        sentencelist = new ArrayList<>();
    }

    public static final SentenceManager getInstance() {
        if (instance == null) {
            try {
                Thread.sleep(50);

                synchronized (SentenceManager.class) {
                    if (instance == null) {
                        instance = new SentenceManager();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return instance;
    }

    public void addSentence(String text, String translate, MySocketClient client) {
        if (text != null && translate != null) {
            String id;
            Sentence sentence;

            synchronized (this) {
                id = makeSenteceId();
                sentence = new Sentence(id, text, translate);

                sentenceMap.put(id, sentence);
                sentencelist.add(sentence);
            }

            /*if (client != null) {
                client.send(JsonFactory.getEntireTransResultJson(client.getSessionid(), id, text, translate, client.getId()));
            }*/
        }
    }

    public void removeSentenceById(String id) {
        sentenceMap.remove(id);

        Iterator<Sentence> it = sentencelist.iterator();
        Sentence sentence;

        while (it.hasNext()) {
            sentence = it.next();

            if (sentence.getId().equals(id)) {
                it.remove();
                break;
            }
        }
    }

    public void removeSentenceByText(String text) {
        Iterator<Sentence> it = sentencelist.iterator();
        String id = null;
        Sentence sentence;

        while (it.hasNext()) {
            sentence = it.next();

            if (sentence.getText().equals(text)) {
                id = sentence.getId();
                it.remove();
                break;
            }
        }

        if (id != null) {
            sentenceMap.remove(id);
        }
    }

    public Sentence getSentenceById(String id) {
        return sentenceMap.get(id);
    }

    public Sentence getLastSentence() {
        return sentencelist.get(sentencelist.size() - 1);
    }

    public List<Sentence> getSentenceList() {
        return sentencelist;
    }

    private String makeSenteceId() {
        String id = GetRandomString.getRandomString(7);

        while (sentenceMap.containsKey(id)) {
            id = GetRandomString.getRandomString(7);
        }

        return id;
    }
}

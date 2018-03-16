package com.cheelem.interpreter.entity;

import org.json.JSONObject;

/**
 * Created by 黄宇航 on 2018/3/7.
 */

public class Sentence {
    private String id;
    private String text;
    private String transalate;

    public Sentence(String id, String text, String transalate) {
        this.id = id;
        this.text = text;
        this.transalate = transalate;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTransalate() {
        return transalate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        return id != null ? id.equals(sentence.id) : sentence.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

package com.translator.trans.factory;

import com.translator.trans.Translator;

public interface TFactory {
    Translator get(String id);
}

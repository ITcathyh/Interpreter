package com.translator.trans.factory;

import com.translator.trans.Translator;
import com.translator.trans.exception.DupIdException;

import java.net.URISyntaxException;

final public class TranslatorFactory extends AbstractTranslatorFactory {

    public TranslatorFactory() throws ClassNotFoundException, InstantiationException, IllegalAccessException, DupIdException, URISyntaxException {
        super();
    }

    @Override
    public Translator get(String id) {
        return translatorMap.get(id);
    }

}

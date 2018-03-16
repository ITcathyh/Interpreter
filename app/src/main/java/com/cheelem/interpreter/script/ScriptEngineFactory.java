package com.cheelem.interpreter.script;

import java.util.List;

/**
 * Created by LLG on 2018/2/25.
 */

public interface ScriptEngineFactory {
    String getEngineName();

    String getEngineVersion();

    List<String> getExtensions();

    List<String> getMimeTypes();

    List<String> getNames();

    String getLanguageName();

    String getLanguageVersion();

    Object getParameter(String var1);

    String getMethodCallSyntax(String var1, String var2, String... var3);

    String getOutputStatement(String var1);

    String getProgram(String... var1);

    ScriptEngine getScriptEngine();
}

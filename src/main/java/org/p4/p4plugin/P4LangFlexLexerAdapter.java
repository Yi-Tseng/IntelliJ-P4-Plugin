package org.p4.p4plugin;

import com.intellij.lexer.FlexAdapter;

public class P4LangFlexLexerAdapter extends FlexAdapter {


    public P4LangFlexLexerAdapter() {
        super(new P4LangFlexLexer(null));
    }
}

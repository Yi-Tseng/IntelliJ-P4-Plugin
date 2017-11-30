package org.opennetworking.p4plugin;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.SingleLazyInstanceSyntaxHighlighterFactory;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;

public class P4Lang extends Language {
    public static P4Lang INSTANCE = new P4Lang();

    protected P4Lang() {
        super("P4Lang");
    }
}

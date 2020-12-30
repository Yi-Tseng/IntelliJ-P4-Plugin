package org.p4.p4plugin.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import org.p4.p4plugin.P4Lang;

/**
 * Basic P4 keyword completion support.
 */
public class P4LangKeywordCompletionContributor extends CompletionContributor {
    public P4LangKeywordCompletionContributor() {
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(P4Lang.INSTANCE),
                new P4LangKeywordProvider());
    }
}

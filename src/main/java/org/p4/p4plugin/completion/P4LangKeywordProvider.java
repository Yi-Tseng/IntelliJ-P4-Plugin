package org.p4.p4plugin.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;

/**
 * P4 keyword completion provider.
 */
public class P4LangKeywordProvider extends CompletionProvider<CompletionParameters> {
    @Override
    protected void addCompletions(CompletionParameters parameters,
                                  ProcessingContext context,
                                  CompletionResultSet result) {

        result.addElement(LookupElementBuilder.create("abstract"));
        result.addElement(LookupElementBuilder.create("action"));
        result.addElement(LookupElementBuilder.create("actions"));
        result.addElement(LookupElementBuilder.create("apply"));
        result.addElement(LookupElementBuilder.create("bool"));
        result.addElement(LookupElementBuilder.create("bit"));
        result.addElement(LookupElementBuilder.create("const"));
        result.addElement(LookupElementBuilder.create("control"));
        result.addElement(LookupElementBuilder.create("default"));
        result.addElement(LookupElementBuilder.create("else"));
        result.addElement(LookupElementBuilder.create("entries"));
        result.addElement(LookupElementBuilder.create("enum"));
        result.addElement(LookupElementBuilder.create("error"));
        result.addElement(LookupElementBuilder.create("exit"));
        result.addElement(LookupElementBuilder.create("extern"));
        result.addElement(LookupElementBuilder.create("false"));
        result.addElement(LookupElementBuilder.create("header"));
        result.addElement(LookupElementBuilder.create("header_union"));
        result.addElement(LookupElementBuilder.create("if"));
        result.addElement(LookupElementBuilder.create("in"));
        result.addElement(LookupElementBuilder.create("inout"));
        result.addElement(LookupElementBuilder.create("int"));
        result.addElement(LookupElementBuilder.create("key"));
        result.addElement(LookupElementBuilder.create("match_kind"));
        result.addElement(LookupElementBuilder.create("out"));
        result.addElement(LookupElementBuilder.create("parser"));
        result.addElement(LookupElementBuilder.create("package"));
        result.addElement(LookupElementBuilder.create("return"));
        result.addElement(LookupElementBuilder.create("select"));
        result.addElement(LookupElementBuilder.create("state"));
        result.addElement(LookupElementBuilder.create("struct"));
        result.addElement(LookupElementBuilder.create("switch"));
        result.addElement(LookupElementBuilder.create("table"));
        result.addElement(LookupElementBuilder.create("this"));
        result.addElement(LookupElementBuilder.create("transition"));
        result.addElement(LookupElementBuilder.create("true"));
        result.addElement(LookupElementBuilder.create("tuple"));
        result.addElement(LookupElementBuilder.create("typedef"));
        result.addElement(LookupElementBuilder.create("varbit"));
        result.addElement(LookupElementBuilder.create("value_set"));
        result.addElement(LookupElementBuilder.create("void"));
    }
}

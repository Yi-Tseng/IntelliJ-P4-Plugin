package org.opennetworking.p4plugin;

import com.google.common.collect.ImmutableMap;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.Map;

public class P4LangSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final P4LangSyntaxHighlighter INSTANCE = new P4LangSyntaxHighlighter();

    public static final TextAttributesKey P4LANG_KEY = TextAttributesKey.createTextAttributesKey(
            "P4LANG.KEY",
            DefaultLanguageHighlighterColors.KEYWORD
    );

    public static final TextAttributesKey P4LANG_STRING = TextAttributesKey.createTextAttributesKey(
            "P4LANG.STRING",
            DefaultLanguageHighlighterColors.STRING
    );

    public static final TextAttributesKey P4LANG_INTEGER = TextAttributesKey.createTextAttributesKey(
            "P4LANG.INTEGER",
            DefaultLanguageHighlighterColors.NUMBER
    );

    public static final TextAttributesKey P4LANG_UNKNOWN_TOKEN = TextAttributesKey.createTextAttributesKey(
            "P4LANG.P4LANG_UNKNOWN_TOKEN",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
    );

    public static final Map<IElementType, TextAttributesKey> P4LANG_ATTR_KEYS =
            ImmutableMap.<IElementType, TextAttributesKey>builder()
                    .put(P4TokenTypes.KEY, P4LANG_KEY)
                    .put(P4TokenTypes.STRING_LITERAL, P4LANG_STRING)
                    .put(P4TokenTypes.INTEGER, P4LANG_INTEGER)
                    .put(P4TokenTypes.UNEXPECTED_TOKEN, P4LANG_UNKNOWN_TOKEN)
                    .build();


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new FlexAdapter(new P4LangFlexLexer((Reader) null));
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {
        if (iElementType == null) {
            return new TextAttributesKey[0];
        }
        return new TextAttributesKey[]{P4LANG_ATTR_KEYS.get(iElementType)};
    }
}

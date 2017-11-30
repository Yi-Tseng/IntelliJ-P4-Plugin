package org.opennetworking.p4plugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.io.Reader;
import java.util.Map;
import java.util.Set;

public class P4LangSyntaxHighlighter extends SyntaxHighlighterBase {

    public static final P4LangSyntaxHighlighter INSTANCE = new P4LangSyntaxHighlighter();

    private static final Set<IElementType> P4LANG_KEYWORDS = ImmutableSet.<IElementType>builder()
            .add(P4TokenTypes.ABSTRACT)
            .add(P4TokenTypes.ACTION)
            .add(P4TokenTypes.ACTIONS)
            .add(P4TokenTypes.APPLY)
            .add(P4TokenTypes.BOOL)
            .add(P4TokenTypes.BIT)
            .add(P4TokenTypes.CONST)
            .add(P4TokenTypes.CONTROL)
            .add(P4TokenTypes.DEFAULT)
            .add(P4TokenTypes.ELSE)
            .add(P4TokenTypes.ENTRIES)
            .add(P4TokenTypes.ENUM)
            .add(P4TokenTypes.ERROR)
            .add(P4TokenTypes.EXIT)
            .add(P4TokenTypes.EXTERN)
            .add(P4TokenTypes.FALSE)
            .add(P4TokenTypes.HEADER)
            .add(P4TokenTypes.HEADER_UNION)
            .add(P4TokenTypes.IF)
            .add(P4TokenTypes.IN)
            .add(P4TokenTypes.INOUT)
            .add(P4TokenTypes.INT)
            .add(P4TokenTypes.KEY)
            .add(P4TokenTypes.MATCH_KIND)
            .add(P4TokenTypes.OUT)
            .add(P4TokenTypes.PARSER)
            .add(P4TokenTypes.PACKAGE)
            .add(P4TokenTypes.RETURN)
            .add(P4TokenTypes.SELECT)
            .add(P4TokenTypes.STATE)
            .add(P4TokenTypes.STRUCT)
            .add(P4TokenTypes.SWITCH)
            .add(P4TokenTypes.TABLE)
            .add(P4TokenTypes.THIS)
            .add(P4TokenTypes.TRANSITION)
            .add(P4TokenTypes.TRUE)
            .add(P4TokenTypes.TUPLE)
            .add(P4TokenTypes.TYPEDEF)
            .add(P4TokenTypes.VARBIT)
            .add(P4TokenTypes.VOID)
            .build();

    public static final TextAttributesKey P4LANG_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "P4LANG.KEYWORD",
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

        if (P4LANG_KEYWORDS.contains(iElementType)) {
            return new TextAttributesKey[]{P4LANG_KEYWORD};
        }
        return new TextAttributesKey[]{P4LANG_ATTR_KEYS.get(iElementType)};
    }
}

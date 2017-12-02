package org.p4.p4plugin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.psi.P4LangTypes;

import java.util.Map;
import java.util.Set;

public class P4LangSyntaxHighlighter extends SyntaxHighlighterBase {

    static final P4LangSyntaxHighlighter INSTANCE = new P4LangSyntaxHighlighter();

    private static final Set<IElementType> P4LANG_KEYWORDS = ImmutableSet.<IElementType>builder()
            .add(P4LangTypes.ABSTRACT)
            .add(P4LangTypes.ACTION)
            .add(P4LangTypes.ACTIONS)
            .add(P4LangTypes.APPLY)
            .add(P4LangTypes.BOOL)
            .add(P4LangTypes.BIT)
            .add(P4LangTypes.CONST)
            .add(P4LangTypes.CONTROL)
            .add(P4LangTypes.DEFAULT)
            .add(P4LangTypes.ELSE)
            .add(P4LangTypes.ENTRIES)
            .add(P4LangTypes.ENUM)
            .add(P4LangTypes.ERROR)
            .add(P4LangTypes.EXIT)
            .add(P4LangTypes.EXTERN)
            .add(P4LangTypes.FALSE)
            .add(P4LangTypes.HEADER)
            .add(P4LangTypes.HEADER_UNION)
            .add(P4LangTypes.IF)
            .add(P4LangTypes.IN)
            .add(P4LangTypes.INOUT)
            .add(P4LangTypes.INT)
            .add(P4LangTypes.KEY)
            .add(P4LangTypes.MATCH_KIND)
            .add(P4LangTypes.OUT)
            .add(P4LangTypes.PARSER)
            .add(P4LangTypes.PACKAGE)
            .add(P4LangTypes.RETURN)
            .add(P4LangTypes.SELECT)
            .add(P4LangTypes.STATE)
            .add(P4LangTypes.STRUCT)
            .add(P4LangTypes.SWITCH)
            .add(P4LangTypes.TABLE)
            .add(P4LangTypes.THIS)
            .add(P4LangTypes.TRANSITION)
            .add(P4LangTypes.TRUE)
            .add(P4LangTypes.TUPLE)
            .add(P4LangTypes.TYPEDEF)
            .add(P4LangTypes.VARBIT)
            .add(P4LangTypes.VOID)
            .build();

    static final TextAttributesKey P4LANG_PRE_PROCESS = TextAttributesKey.createTextAttributesKey(
            "P4LANG.PRE_PROCESS",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
    );

    static final TextAttributesKey P4LANG_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "P4LANG.KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
    );

    static final TextAttributesKey P4LANG_STRING = TextAttributesKey.createTextAttributesKey(
            "P4LANG.STRING",
            DefaultLanguageHighlighterColors.STRING
    );

    static final TextAttributesKey P4LANG_INTEGER = TextAttributesKey.createTextAttributesKey(
            "P4LANG.INTEGER",
            DefaultLanguageHighlighterColors.NUMBER
    );

    static final TextAttributesKey P4LANG_ANNONTATION = TextAttributesKey.createTextAttributesKey(
            "P4LANG.ANNONTATION",
            DefaultLanguageHighlighterColors.LABEL
    );

    static final TextAttributesKey P4LANG_TYPE = TextAttributesKey.createTextAttributesKey(
            "P4LANG.TYPE",
            DefaultLanguageHighlighterColors.CLASS_NAME
    );

    static final TextAttributesKey P4LANG_COMMENT = TextAttributesKey.createTextAttributesKey(
            "P4LANG.COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
    );

    static final TextAttributesKey P4LANG_MATCH_KIND = TextAttributesKey.createTextAttributesKey(
            "P4LANG.MATCH_KIND",
            DefaultLanguageHighlighterColors.KEYWORD
    );

    static final TextAttributesKey P4LANG_UNKNOWN_TOKEN = TextAttributesKey.createTextAttributesKey(
            "P4LANG.P4LANG_UNKNOWN_TOKEN",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
    );

    private static final  TextAttributesKey[] EMPTY_ATTR_KEY = new TextAttributesKey[0];

    private static final Map<IElementType, TextAttributesKey> P4LANG_ATTR_KEYS =
            ImmutableMap.<IElementType, TextAttributesKey>builder()
                    .put(P4LangTypes.MATCH_KIND, P4LANG_MATCH_KIND)
                    .put(P4LangTypes.COMMENT, P4LANG_COMMENT)
                    .put(P4LangTypes.TYPE, P4LANG_TYPE)
                    .put(P4LangTypes.ANNOTATION, P4LANG_ANNONTATION)
                    .put(P4LangTypes.PRE_PROCESS, P4LANG_PRE_PROCESS)
                    .put(P4LangTypes.STRING_LITERAL, P4LANG_STRING)
                    .put(P4LangTypes.INTEGER, P4LANG_INTEGER)
                    .put(TokenType.BAD_CHARACTER, P4LANG_UNKNOWN_TOKEN)
                    .build();


    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new P4LangFlexLexerAdapter();
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {
        if (iElementType == null) {
            return EMPTY_ATTR_KEY;
        }

        if (P4LANG_KEYWORDS.contains(iElementType)) {
            return new TextAttributesKey[]{P4LANG_KEYWORD};
        }
        TextAttributesKey key = P4LANG_ATTR_KEYS.get(iElementType);
        if (key == null) {
            return EMPTY_ATTR_KEY;
        }
        return new TextAttributesKey[]{key};
    }
}

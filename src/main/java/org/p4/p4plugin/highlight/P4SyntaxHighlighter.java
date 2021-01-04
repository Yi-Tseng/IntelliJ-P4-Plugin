package org.p4.p4plugin.highlight;

import com.google.common.collect.Lists;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.p4.p4plugin.P4Lang;
import org.p4.p4plugin.parsing.P4Lexer;
import org.p4.p4plugin.psi.P4LangTokenType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class P4SyntaxHighlighter extends SyntaxHighlighterBase {

    private static final List<IElementType> KEYWORDS = Stream.of(
            P4Lexer.ABSTRACT, P4Lexer.ACTION, P4Lexer.ACTIONS, P4Lexer.APPLY, P4Lexer.BOOL, P4Lexer.BIT, P4Lexer.CONST,
            P4Lexer.CONTROL, P4Lexer.DEFAULT, P4Lexer.ELSE, P4Lexer.ENTRIES, P4Lexer.ENUM, P4Lexer.ERROR, P4Lexer.EXIT,
            P4Lexer.EXTERN, P4Lexer.FALSE, P4Lexer.HEADER, P4Lexer.HEADER_UNION, P4Lexer.IF, P4Lexer.IN, P4Lexer.INOUT,
            P4Lexer.INT, P4Lexer.KEY, P4Lexer.MATCH_KIND, P4Lexer.TYPE, P4Lexer.OUT, P4Lexer.PARSER, P4Lexer.PACKAGE,
            P4Lexer.RETURN, P4Lexer.SELECT, P4Lexer.SWITCH, P4Lexer.TABLE, P4Lexer.THIS, P4Lexer.TUPLE, P4Lexer.TYPEDEF,
            P4Lexer.VARBIT, P4Lexer.VALUESET, P4Lexer.VOID
    ).map(P4LangTokenType::getTokenElementType).collect(Collectors.toList());

    public static final TextAttributesKey P4LANG_PRE_PROCESS = TextAttributesKey.createTextAttributesKey(
            "P4LANG.PRE_PROCESS",
            DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL
    );

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

    public static final TextAttributesKey P4LANG_ANNOTATION = TextAttributesKey.createTextAttributesKey(
            "P4LANG.ANNOTATION",
            DefaultLanguageHighlighterColors.LABEL
    );

    public static final TextAttributesKey P4LANG_TYPE = TextAttributesKey.createTextAttributesKey(
            "P4LANG.TYPE",
            DefaultLanguageHighlighterColors.CLASS_NAME
    );

    public static final TextAttributesKey P4LANG_COMMENT = TextAttributesKey.createTextAttributesKey(
            "P4LANG.COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
    );

    public static final TextAttributesKey P4LANG_MATCH_KIND = TextAttributesKey.createTextAttributesKey(
            "P4LANG.MATCH_KIND",
            DefaultLanguageHighlighterColors.KEYWORD
    );

    public static final TextAttributesKey P4LANG_UNKNOWN_TOKEN = TextAttributesKey.createTextAttributesKey(
            "P4LANG.P4LANG_UNKNOWN_TOKEN",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
    );

    public P4SyntaxHighlighter() {  }

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return new ANTLRLexerAdaptor(P4Lang.INSTANCE, new P4Lexer(null));
    }

    @Override
    public @NotNull TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        TextAttributesKey key;
        if (KEYWORDS.contains(tokenType)) {
            key = P4LANG_KEYWORD;
        } else if (P4LangTokenType.getTokenElementType(P4Lexer.STRING_LITERAL).equals(tokenType)) {
            key = P4LANG_STRING;
        } else if (P4LangTokenType.getTokenElementType(P4Lexer.COMMENT).equals(tokenType)) {
            key = P4LANG_COMMENT;
        } else if (P4LangTokenType.getTokenElementType(P4Lexer.LINE_COMMENT).equals(tokenType)) {
            key = P4LANG_COMMENT;
        } else if (P4LangTokenType.getTokenElementType(P4Lexer.INTEGER).equals(tokenType)) {
            key = P4LANG_INTEGER;
        } else {
            return new TextAttributesKey[0];
        }

        return new TextAttributesKey[]{key};
    }

    public static class Factory extends SyntaxHighlighterFactory {

        @Override
        public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile) {
            return new P4SyntaxHighlighter();
        }
    }
}

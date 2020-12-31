package org.p4.p4plugin.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.P4Lang;
import org.p4.p4plugin.parsing.P4Lexer;
import org.p4.p4plugin.parsing.P4Parser;

public class P4ParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(P4Lang.INSTANCE);

    public P4ParserDefinition() {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                P4Lang.INSTANCE,
                P4Lexer.tokenNames,
                P4Parser.ruleNames
        );
    }

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return new ANTLRLexerAdaptor(P4Lang.INSTANCE, new P4Lexer(null)) {
            @Override
            public void advance() {
                // TODO: Process preprocessor token(collect define, include files, macros...)
                super.advance();
            }
        };
    }

    @Override
    public PsiParser createParser(Project project) {
        P4Parser parser = new P4Parser(null);
//        parser.addErrorListener(); // To collect error
//        parser.addParseListener(new P4ParserListener()); // To collect symbols(tables, fields...)
        return new ANTLRParserAdaptor(P4Lang.INSTANCE, parser) {
            @Override
            protected ParseTree parse(Parser parser, IElementType root) {
                return ((P4Parser) parser).start();
            }
        };
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        IElementType commentType = P4LangTokenType.getTokenElementType(P4Lexer.COMMENT);
        IElementType lineCommentType = P4LangTokenType.getTokenElementType(P4Lexer.LINE_COMMENT);
        IElementType preprocessorType = P4LangTokenType.getTokenElementType(P4Lexer.PREPROCESSSOR);
        return TokenSet.create(commentType, lineCommentType, preprocessorType);
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        IElementType stringType = P4LangTokenType.getTokenElementType(P4Lexer.STRING_LITERAL);
        return TokenSet.create(stringType);
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return new ASTWrapperPsiElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new P4LangFile(viewProvider);
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        IElementType wsType = P4LangTokenType.getTokenElementType(P4Lexer.WS);
        return TokenSet.create(wsType);
    }

    @Override
    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }
}

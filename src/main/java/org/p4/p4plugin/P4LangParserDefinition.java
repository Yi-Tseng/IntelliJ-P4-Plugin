package org.p4.p4plugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.p4.p4plugin.parser.P4LangParser;
import org.p4.p4plugin.psi.P4LangFile;
import org.p4.p4plugin.psi.P4LangTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class P4LangParserDefinition implements ParserDefinition {
    private Logger log = LoggerFactory.getLogger(getClass());
    public static final IFileElementType FILE = new IFileElementType(P4Lang.INSTANCE);

    public static final TokenSet STRING_LITERALS = TokenSet.create(P4LangTypes.STRING_LITERAL);
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);

    // Note: we treat any preprocessor statements (e.g., #include, #define, ...) as a comment
    // since the parser should not have the logic to precess them.
    // TODO: However, we still need to process some of them (e.g., find symbols from include files)
    public static final TokenSet COMMENTS = TokenSet.create(P4LangTypes.COMMENT, P4LangTypes.PRE_PROCESS);

    public P4LangParserDefinition() {
        log.info("P4LangParserDefinition Created");
    }

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new P4LangFlexLexerAdapter();
    }

    @Override
    public PsiParser createParser(Project project) {
        return new P4LangParser();
    }

    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRING_LITERALS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return P4LangTypes.Factory.createElement(node);
    }

    @Override
    public PsiFile createFile(FileViewProvider viewProvider) {
        return new P4LangFile(viewProvider);
    }
}

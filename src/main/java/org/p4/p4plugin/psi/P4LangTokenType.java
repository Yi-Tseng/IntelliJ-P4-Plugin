package org.p4.p4plugin.psi;

import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.intellij.lang.annotations.MagicConstant;
import org.p4.p4plugin.P4Lang;
import org.p4.p4plugin.parsing.P4Lexer;
import org.p4.p4plugin.parsing.P4Parser;

import java.util.List;

public class P4LangTokenType {
    static {
        PSIElementTypeFactory.defineLanguageIElementTypes(
                P4Lang.INSTANCE,
                P4Lexer.tokenNames,
                P4Parser.ruleNames
        );
    }

    private static final List<RuleIElementType> RULE_ELEMENT_TYPES =
            PSIElementTypeFactory.getRuleIElementTypes(P4Lang.INSTANCE);
    private static final List<TokenIElementType> TOKEN_ELEMENT_TYPES =
            PSIElementTypeFactory.getTokenIElementTypes(P4Lang.INSTANCE);

    public static RuleIElementType
    getRuleElementType(@MagicConstant(valuesFromClass = P4Parser.class) int ruleIndex) {
        return RULE_ELEMENT_TYPES.get(ruleIndex);
    }

    public static TokenIElementType
    getTokenElementType(@MagicConstant(valuesFromClass = P4Lexer.class) int tokenIndex) {
        return TOKEN_ELEMENT_TYPES.get(tokenIndex);
    }
}

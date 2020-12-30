package org.p4.p4plugin.psi;

import org.p4.p4plugin.parsing.P4BaseListener;
import org.p4.p4plugin.parsing.P4Parser.HeaderTypeDeclarationContext;
import org.p4.p4plugin.parsing.P4Parser.PpIncludeFileNameContext;

public class P4ParserListener extends P4BaseListener {

    @Override
    public void exitPpIncludeFileName(PpIncludeFileNameContext ctx) {
        // To collect include files
        if (ctx.STRING_LITERAL() != null) {
//            System.err.println("Include " + ctx.STRING_LITERAL().getText());
        } else if (ctx.ppIncludeFilePath() != null) {
//            System.err.println("Include " + ctx.ppIncludeFilePath().getText());
        }
    }

    @Override
    public void exitHeaderTypeDeclaration(HeaderTypeDeclarationContext ctx) {
        // To collect headers
        if (ctx.name() != null) {
//            System.err.println("Header name " + ctx.name().getText());
        }
    }
}

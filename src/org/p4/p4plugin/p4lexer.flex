
package org.p4.p4plugin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.p4.p4plugin.psi.P4LangTypes;
import com.intellij.psi.TokenType;


%%

%class P4LangFlexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%state COMMENT
%state NORMAL
%state STRING

%{

private void blockComment() {
    int zzInput;
    int state = 0;
    while(zzCurrentPos < zzEndRead) {
        zzInput = Character.codePointAt(zzBuffer, zzCurrentPos);
        zzCurrentPos += Character.charCount(zzInput);

        if (zzInput == '*') {
            state = 1;
        } else {
            if (zzInput == '/' && state == 1) {
                // end of block comment, accept
                zzMarkedPos = zzCurrentPos;
                return;
            } else {
                state = 0;
            }
        }
    }

    // didn't find end of comment
    zzAtEOF = true;
    zzMarkedPos = zzCurrentPos;
}

%}

%%


"/*"                 { blockComment(); return P4LangTypes.COMMENT; }

[ \t\r]+        { yybegin(NORMAL); return TokenType.WHITE_SPACE; }
[\n]            { yybegin(NORMAL); return TokenType.WHITE_SPACE; }
"//".*          { yybegin(NORMAL); return P4LangTypes.COMMENT; }

<COMMENT,NORMAL><<EOF>> { yybegin(YYINITIAL); }


\"(\\.|[^\"])*\" {return P4LangTypes.STRING_LITERAL; }


"#"\w+          { yybegin(NORMAL); return P4LangTypes.PRE_PROCESS; }
"abstract"      { yybegin(NORMAL); return P4LangTypes.ABSTRACT; }
"action"        { yybegin(NORMAL); return P4LangTypes.ACTION; }
"actions"       { yybegin(NORMAL); return P4LangTypes.ACTIONS; }
"apply"         { yybegin(NORMAL); return P4LangTypes.APPLY; }
"bool"          { yybegin(NORMAL); return P4LangTypes.BOOL; }
"bit"           { yybegin(NORMAL); return P4LangTypes.BIT; }
"const"         { yybegin(NORMAL); return P4LangTypes.CONST; }
"control"       { yybegin(NORMAL); return P4LangTypes.CONTROL; }
"default"       { yybegin(NORMAL); return P4LangTypes.DEFAULT; }
"else"          { yybegin(NORMAL); return P4LangTypes.ELSE; }
"entries"       { yybegin(NORMAL); return P4LangTypes.ENTRIES; }
"enum"          { yybegin(NORMAL); return P4LangTypes.ENUM; }
"error"         { yybegin(NORMAL); return P4LangTypes.ERROR; }
"exit"          { yybegin(NORMAL); return P4LangTypes.EXIT; }
"extern"        { yybegin(NORMAL); return P4LangTypes.EXTERN; }
"false"         { yybegin(NORMAL); return P4LangTypes.FALSE; }
"header"        { yybegin(NORMAL); return P4LangTypes.HEADER; }
"header_union"  { yybegin(NORMAL); return P4LangTypes.HEADER_UNION; }
"if"            { yybegin(NORMAL); return P4LangTypes.IF; }
"in"            { yybegin(NORMAL); return P4LangTypes.IN; }
"inout"         { yybegin(NORMAL); return P4LangTypes.INOUT; }
"int"           { yybegin(NORMAL); return P4LangTypes.INT; }
"key"           { yybegin(NORMAL); return P4LangTypes.KEY; }
"match_kind"    { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }
"out"           { yybegin(NORMAL); return P4LangTypes.OUT; }
"parser"        { yybegin(NORMAL); return P4LangTypes.PARSER; }
"package"       { yybegin(NORMAL); return P4LangTypes.PACKAGE; }
"return"        { yybegin(NORMAL); return P4LangTypes.RETURN; }
"select"        { yybegin(NORMAL); return P4LangTypes.SELECT; }
"state"         { yybegin(NORMAL); return P4LangTypes.STATE; }
"struct"        { yybegin(NORMAL); return P4LangTypes.STRUCT; }
"switch"        { yybegin(NORMAL); return P4LangTypes.SWITCH; }
"table"         { yybegin(NORMAL); return P4LangTypes.TABLE; }
"this"          { yybegin(NORMAL); return P4LangTypes.THIS; }
"transition"    { yybegin(NORMAL); return P4LangTypes.TRANSITION; }
"true"          { yybegin(NORMAL); return P4LangTypes.TRUE; }
"tuple"         { yybegin(NORMAL); return P4LangTypes.TUPLE; }
"typedef"       { yybegin(NORMAL); return P4LangTypes.TYPEDEF; }
"varbit"        { yybegin(NORMAL); return P4LangTypes.VARBIT; }
"void"          { yybegin(NORMAL); return P4LangTypes.VOID; }
"_"             { yybegin(NORMAL); return P4LangTypes.DONTCARE; }
[A-Za-z_][A-Za-z0-9_]* {
                  yybegin(NORMAL);
                  return P4LangTypes.IDENTIFIER;
                  /* FIXME: this might be a type*/ }
0[xX][0-9a-fA-F_]+ { yybegin(NORMAL);
                     return P4LangTypes.INTEGER; }
0[dD][0-9_]+       { yybegin(NORMAL);
                     return P4LangTypes.INTEGER; }
0[oO][0-7_]+       { yybegin(NORMAL);
                     return P4LangTypes.INTEGER; }
0[bB][01_]+        { yybegin(NORMAL);
                     return P4LangTypes.INTEGER; }
[0-9][0-9_]*       { yybegin(NORMAL);
                     return P4LangTypes.INTEGER; }

[0-9]+[ws]0[xX][0-9a-fA-F_]+ { yybegin(NORMAL);
                               return P4LangTypes.INTEGER; }
[0-9]+[ws]0[dD][0-9_]+  { yybegin(NORMAL);
                          return P4LangTypes.INTEGER; }
[0-9]+[ws]0[oO][0-7_]+  { yybegin(NORMAL);
                          return P4LangTypes.INTEGER; }
[0-9]+[ws]0[bB][01_]+   { yybegin(NORMAL);
                          return P4LangTypes.INTEGER; }
[0-9]+[ws][0-9_]+       { yybegin(NORMAL);
                          return P4LangTypes.INTEGER; }

.              { return TokenType.BAD_CHARACTER; }
[^]            { return TokenType.BAD_CHARACTER; }

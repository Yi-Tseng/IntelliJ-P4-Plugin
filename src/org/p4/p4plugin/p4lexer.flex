
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

%x COMMENT STRING
%s NORMAL TYPE_DEF INCLUDE ANNONTATION
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

[ \t\r]+        { return TokenType.WHITE_SPACE; }
[\n]            { return TokenType.WHITE_SPACE; }
"//".*          { return P4LangTypes.COMMENT; }
"/*"            { blockComment(); return P4LangTypes.COMMENT; }


<COMMENT,NORMAL><<EOF>> { yybegin(YYINITIAL); return P4LangTypes.END; }

\"              { yybegin(STRING); }
<STRING>\\\"    {  }
<STRING>\\\\    {  }
<STRING>\"      { yybegin(YYINITIAL); return P4LangTypes.STRING_LITERAL; }
<STRING>.       {  }
<STRING>\n      {  }

/* Preprocessors */
"#include"   { return P4LangTypes.PRE_PROCESS; }
"#define"    { return P4LangTypes.PRE_PROCESS; }
"#ifdef"     { return P4LangTypes.PRE_PROCESS; }
"#ifndef"    { return P4LangTypes.PRE_PROCESS; }
"#else"      { return P4LangTypes.PRE_PROCESS; }
"#endif"     { return P4LangTypes.PRE_PROCESS; }

"abstract"      { yybegin(NORMAL); return P4LangTypes.ABSTRACT; }
"action"        { yybegin(NORMAL); return P4LangTypes.ACTION; }
"actions"       { yybegin(NORMAL); return P4LangTypes.ACTIONS; }
"apply"         { yybegin(NORMAL); return P4LangTypes.APPLY; }

"const"         { yybegin(NORMAL); return P4LangTypes.CONST; }
"control"       { yybegin(NORMAL); return P4LangTypes.CONTROL; }
"default"       { yybegin(NORMAL); return P4LangTypes.DEFAULT; }
"else"          { yybegin(NORMAL); return P4LangTypes.ELSE; }
"entries"       { yybegin(NORMAL); return P4LangTypes.ENTRIES; }

"error"         { yybegin(NORMAL); return P4LangTypes.ERROR; }
"exit"          { yybegin(NORMAL); return P4LangTypes.EXIT; }
"extern"        { yybegin(NORMAL); return P4LangTypes.EXTERN; }
"false"         { yybegin(NORMAL); return P4LangTypes.FALSE; }
"if"            { yybegin(NORMAL); return P4LangTypes.IF; }

/* Type define */
"header"        { yybegin(TYPE_DEF); return P4LangTypes.HEADER; }
"header_union"  { yybegin(TYPE_DEF); return P4LangTypes.HEADER_UNION; }
"in"            { yybegin(TYPE_DEF); return P4LangTypes.IN; }
"inout"         { yybegin(TYPE_DEF); return P4LangTypes.INOUT; }
"out"           { yybegin(TYPE_DEF); return P4LangTypes.OUT; }
"int"           { yybegin(TYPE_DEF); return P4LangTypes.INT; }
"struct"        { yybegin(TYPE_DEF); return P4LangTypes.STRUCT; }
"bool"          { yybegin(TYPE_DEF); return P4LangTypes.BOOL; }
"bit"           { yybegin(TYPE_DEF); return P4LangTypes.BIT; }
"enum"          { yybegin(TYPE_DEF); return P4LangTypes.ENUM; }
"typedef"       { yybegin(TYPE_DEF); return P4LangTypes.TYPEDEF; }
"varbit"        { yybegin(TYPE_DEF); return P4LangTypes.VARBIT; }
"void"          { yybegin(TYPE_DEF); return P4LangTypes.VOID; }

/* Match kind */
"ternary"       { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }
"exact"         { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }
"range"         { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }
"lpm"           { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }
"valid"         { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }

"key"           { yybegin(NORMAL); return P4LangTypes.KEY; }
"match_kind"    { yybegin(NORMAL); return P4LangTypes.MATCH_KIND; }
"parser"        { yybegin(NORMAL); return P4LangTypes.PARSER; }
"package"       { yybegin(NORMAL); return P4LangTypes.PACKAGE; }
"return"        { yybegin(NORMAL); return P4LangTypes.RETURN; }
"select"        { yybegin(NORMAL); return P4LangTypes.SELECT; }
"state"         { yybegin(NORMAL); return P4LangTypes.STATE; }

"switch"        { yybegin(NORMAL); return P4LangTypes.SWITCH; }
"table"         { yybegin(NORMAL); return P4LangTypes.TABLE; }
"this"          { yybegin(NORMAL); return P4LangTypes.THIS; }
"transition"    { yybegin(NORMAL); return P4LangTypes.TRANSITION; }
"true"          { yybegin(NORMAL); return P4LangTypes.TRUE; }
"tuple"         { yybegin(NORMAL); return P4LangTypes.TUPLE; }
"_"             { yybegin(NORMAL); return P4LangTypes.DONTCARE; }

<ANNONTATION>[A-Za-z_][A-Za-z0-9_]* {
                  yybegin(NORMAL);
                  return P4LangTypes.ANNOTATION;
                }

<ANNONTATION>.+ {
                  yybegin(NORMAL);
                  return P4LangTypes.AT;
}

<TYPE_DEF>[A-Za-z_][A-Za-z0-9_]* {
                  yybegin(NORMAL);
                  return P4LangTypes.TYPE;
                }

<NORMAL>[A-Za-z_][A-Za-z0-9_]* {
                  yybegin(NORMAL);
                  return P4LangTypes.IDENTIFIER;
                }

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

"&&&"           { yybegin(NORMAL); return P4LangTypes.MASK; }
".."            { yybegin(NORMAL); return P4LangTypes.RANGE; }
"<<"            { yybegin(NORMAL); return P4LangTypes.SHL; }
"&&"            { yybegin(NORMAL); return P4LangTypes.AND; }
"||"            { yybegin(NORMAL); return P4LangTypes.OR; }
"=="            { yybegin(NORMAL); return P4LangTypes.EQ; }
"!="            { yybegin(NORMAL); return P4LangTypes.NE; }
">="            { yybegin(NORMAL); return P4LangTypes.GE; }
"<="            { yybegin(NORMAL); return P4LangTypes.LE; }
"++"            { yybegin(NORMAL); return P4LangTypes.PP; }

"+"            { yybegin(NORMAL); return P4LangTypes.PLUS; }
"-"            { yybegin(NORMAL); return P4LangTypes.MINUS; }
"*"            { yybegin(NORMAL); return P4LangTypes.MUL; }
"/"            { yybegin(NORMAL); return P4LangTypes.DIV; }
"%"            { yybegin(NORMAL); return P4LangTypes.MOD; }

"|"            { yybegin(NORMAL); return P4LangTypes.BIT_OR; }
"&"            { yybegin(NORMAL); return P4LangTypes.BIT_AND; }
"^"            { yybegin(NORMAL); return P4LangTypes.BIT_XOR; }
"~"            { yybegin(NORMAL); return P4LangTypes.COMPLEMENT; }

"("            { yybegin(NORMAL); return P4LangTypes.L_PAREN; }
")"            { yybegin(NORMAL); return P4LangTypes.R_PAREN; }
"["            { yybegin(NORMAL); return P4LangTypes.L_BRACKET; }
"]"            { yybegin(NORMAL); return P4LangTypes.R_BRACKET; }
"{"            { yybegin(NORMAL); return P4LangTypes.L_BRACE; }
"}"            { yybegin(NORMAL); return P4LangTypes.R_BRACE; }
"<"            { yybegin(NORMAL); return P4LangTypes.L_ANGLE; }
">"            { yybegin(NORMAL); return P4LangTypes.R_ANGLE; }

"!"            { yybegin(NORMAL); return P4LangTypes.NOT; }
":"            { yybegin(NORMAL); return P4LangTypes.COLON; }
","            { yybegin(NORMAL); return P4LangTypes.COMMA; }
"?"            { yybegin(NORMAL); return P4LangTypes.QUESTION; }
"."            { yybegin(NORMAL); return P4LangTypes.DOT; }
"="            { yybegin(NORMAL); return P4LangTypes.ASSIGN; }
";"            { yybegin(NORMAL); return P4LangTypes.SEMICOLON; }
"@"            { yybegin(ANNONTATION); }

.              { return P4LangTypes.UNEXPECTED_TOKEN; }

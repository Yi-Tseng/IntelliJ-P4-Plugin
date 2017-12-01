
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
%x LINE1 LINE2 LINE3
%s NORMAL TYPE_DEF
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


<YYINITIAL>"#line"      { yybegin(LINE1); }
<YYINITIAL>"# "         { yybegin(LINE1); }
<YYINITIAL>[ \t]*"#"    { yybegin(LINE3); }
<LINE1>[0-9]+         { yybegin(LINE2); }
<LINE2>\"[^\"]*       { yybegin(LINE3); }
<LINE1,LINE2>[ \t]      {}
<LINE1,LINE2>.        { yybegin(LINE3); }
<LINE3>.                {}
<LINE1,LINE2,LINE3>\n { yybegin(YYINITIAL); }
<LINE1,LINE2,LINE3,COMMENT,NORMAL><<EOF>> { yybegin(YYINITIAL); return P4LangTypes.END; }

\"              { yybegin(STRING); }
<STRING>\\\"    {  }
<STRING>\\\\    {  }
<STRING>\"      { yybegin(YYINITIAL); return P4LangTypes.STRING_LITERAL; }
<STRING>.       {  }
<STRING>\n      {  }

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

"if"            { yybegin(NORMAL); return P4LangTypes.IF; }

"header"        { yybegin(TYPE_DEF); return P4LangTypes.HEADER; }
"header_union"  { yybegin(TYPE_DEF); return P4LangTypes.HEADER_UNION; }
"in"            { yybegin(TYPE_DEF); return P4LangTypes.IN; }
"inout"         { yybegin(TYPE_DEF); return P4LangTypes.INOUT; }
"out"           { yybegin(TYPE_DEF); return P4LangTypes.OUT; }
"int"           { yybegin(TYPE_DEF); return P4LangTypes.INT; }
"struct"        { yybegin(TYPE_DEF); return P4LangTypes.STRUCT; }

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
"typedef"       { yybegin(NORMAL); return P4LangTypes.TYPEDEF; }
"varbit"        { yybegin(NORMAL); return P4LangTypes.VARBIT; }
"void"          { yybegin(NORMAL); return P4LangTypes.VOID; }
"_"             { yybegin(NORMAL); return P4LangTypes.DONTCARE; }

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
"@"            { yybegin(NORMAL); return P4LangTypes.AT; }

.              { return P4LangTypes.UNEXPECTED_TOKEN; }

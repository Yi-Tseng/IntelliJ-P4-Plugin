
package org.opennetworking.p4plugin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;



%%

%class P4LangFlexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%state COMMENT
%state NORMAL
%state STRING

%%



"/*"            { yybegin(COMMENT); }
<COMMENT>"*/"   { yybegin(NORMAL); return P4TokenTypes.COMMENT; }
<COMMENT>.      { yybegin(COMMENT); }
<COMMENT>[\n]   { yybegin(COMMENT); }

[ \t\r]+        { yybegin(NORMAL); return P4TokenTypes.WHITE_SPACE; }
[\n]            { yybegin(NORMAL); return P4TokenTypes.WHITE_SPACE; }
"//".*          { yybegin(NORMAL); return P4TokenTypes.COMMENT; }

<COMMENT,NORMAL><<EOF>> { yybegin(YYINITIAL); }


L?\"(\\.|[^\"])*\" {return P4TokenTypes.STRING_LITERAL; }


"#"\w+          { yybegin(NORMAL); return P4TokenTypes.PRE_PROCESS; }
"abstract"      { yybegin(NORMAL); return P4TokenTypes.ABSTRACT; }
"action"        { yybegin(NORMAL); return P4TokenTypes.ACTION; }
"actions"       { yybegin(NORMAL); return P4TokenTypes.ACTIONS; }
"apply"         { yybegin(NORMAL); return P4TokenTypes.APPLY; }
"bool"          { yybegin(NORMAL); return P4TokenTypes.BOOL; }
"bit"           { yybegin(NORMAL); return P4TokenTypes.BIT; }
"const"         { yybegin(NORMAL); return P4TokenTypes.CONST; }
"control"       { yybegin(NORMAL); return P4TokenTypes.CONTROL; }
"default"       { yybegin(NORMAL); return P4TokenTypes.DEFAULT; }
"else"          { yybegin(NORMAL); return P4TokenTypes.ELSE; }
"entries"       { yybegin(NORMAL); return P4TokenTypes.ENTRIES; }
"enum"          { yybegin(NORMAL); return P4TokenTypes.ENUM; }
"error"         { yybegin(NORMAL); return P4TokenTypes.ERROR; }
"exit"          { yybegin(NORMAL); return P4TokenTypes.EXIT; }
"extern"        { yybegin(NORMAL); return P4TokenTypes.EXTERN; }
"false"         { yybegin(NORMAL); return P4TokenTypes.FALSE; }
"header"        { yybegin(NORMAL); return P4TokenTypes.HEADER; }
"header_union"  { yybegin(NORMAL); return P4TokenTypes.HEADER_UNION; }
"if"            { yybegin(NORMAL); return P4TokenTypes.IF; }
"in"            { yybegin(NORMAL); return P4TokenTypes.IN; }
"inout"         { yybegin(NORMAL); return P4TokenTypes.INOUT; }
"int"           { yybegin(NORMAL); return P4TokenTypes.INT; }
"key"           { yybegin(NORMAL); return P4TokenTypes.KEY; }
"match_kind"    { yybegin(NORMAL); return P4TokenTypes.MATCH_KIND; }
"out"           { yybegin(NORMAL); return P4TokenTypes.OUT; }
"parser"        { yybegin(NORMAL); return P4TokenTypes.PARSER; }
"package"       { yybegin(NORMAL); return P4TokenTypes.PACKAGE; }
"return"        { yybegin(NORMAL); return P4TokenTypes.RETURN; }
"select"        { yybegin(NORMAL); return P4TokenTypes.SELECT; }
"state"         { yybegin(NORMAL); return P4TokenTypes.STATE; }
"struct"        { yybegin(NORMAL); return P4TokenTypes.STRUCT; }
"switch"        { yybegin(NORMAL); return P4TokenTypes.SWITCH; }
"table"         { yybegin(NORMAL); return P4TokenTypes.TABLE; }
"this"          { yybegin(NORMAL); return P4TokenTypes.THIS; }
"transition"    { yybegin(NORMAL); return P4TokenTypes.TRANSITION; }
"true"          { yybegin(NORMAL); return P4TokenTypes.TRUE; }
"tuple"         { yybegin(NORMAL); return P4TokenTypes.TUPLE; }
"typedef"       { yybegin(NORMAL); return P4TokenTypes.TYPEDEF; }
"varbit"        { yybegin(NORMAL); return P4TokenTypes.VARBIT; }
"void"          { yybegin(NORMAL); return P4TokenTypes.VOID; }
"_"             { yybegin(NORMAL); return P4TokenTypes.DONTCARE; }
[A-Za-z_][A-Za-z0-9_]* {
                  yybegin(NORMAL);
                  return P4TokenTypes.IDENTIFIER;
                  /* FIXME: this might be a type*/ }
0[xX][0-9a-fA-F_]+ { yybegin(NORMAL);
                     return P4TokenTypes.INTEGER; }
0[dD][0-9_]+       { yybegin(NORMAL);
                     return P4TokenTypes.INTEGER; }
0[oO][0-7_]+       { yybegin(NORMAL);
                     return P4TokenTypes.INTEGER; }
0[bB][01_]+        { yybegin(NORMAL);
                     return P4TokenTypes.INTEGER; }
[0-9][0-9_]*       { yybegin(NORMAL);
                     return P4TokenTypes.INTEGER; }

[0-9]+[ws]0[xX][0-9a-fA-F_]+ { yybegin(NORMAL);
                               return P4TokenTypes.INTEGER; }
[0-9]+[ws]0[dD][0-9_]+  { yybegin(NORMAL);
                          return P4TokenTypes.INTEGER; }
[0-9]+[ws]0[oO][0-7_]+  { yybegin(NORMAL);
                          return P4TokenTypes.INTEGER; }
[0-9]+[ws]0[bB][01_]+   { yybegin(NORMAL);
                          return P4TokenTypes.INTEGER; }
[0-9]+[ws][0-9_]+       { yybegin(NORMAL);
                          return P4TokenTypes.INTEGER; }

"&&&"           { yybegin(NORMAL); return P4TokenTypes.MASK; }
".."            { yybegin(NORMAL); return P4TokenTypes.RANGE; }
"<<"            { yybegin(NORMAL); return P4TokenTypes.SHL; }
"&&"            { yybegin(NORMAL); return P4TokenTypes.AND; }
"||"            { yybegin(NORMAL); return P4TokenTypes.OR; }
"=="            { yybegin(NORMAL); return P4TokenTypes.EQ; }
"!="            { yybegin(NORMAL); return P4TokenTypes.NE; }
">="            { yybegin(NORMAL); return P4TokenTypes.GE; }
"<="            { yybegin(NORMAL); return P4TokenTypes.LE; }
"++"            { yybegin(NORMAL); return P4TokenTypes.PP; }

"+"            { yybegin(NORMAL); return P4TokenTypes.PLUS; }
"-"            { yybegin(NORMAL); return P4TokenTypes.MINUS; }
"*"            { yybegin(NORMAL); return P4TokenTypes.MUL; }
"/"            { yybegin(NORMAL); return P4TokenTypes.DIV; }
"%"            { yybegin(NORMAL); return P4TokenTypes.MOD; }

"|"            { yybegin(NORMAL); return P4TokenTypes.BIT_OR; }
"&"            { yybegin(NORMAL); return P4TokenTypes.BIT_AND; }
"^"            { yybegin(NORMAL); return P4TokenTypes.BIT_XOR; }
"~"            { yybegin(NORMAL); return P4TokenTypes.COMPLEMENT; }

"("            { yybegin(NORMAL); return P4TokenTypes.L_PAREN; }
")"            { yybegin(NORMAL); return P4TokenTypes.R_PAREN; }
"["            { yybegin(NORMAL); return P4TokenTypes.L_BRACKET; }
"]"            { yybegin(NORMAL); return P4TokenTypes.R_BRACKET; }
"{"            { yybegin(NORMAL); return P4TokenTypes.L_BRACE; }
"}"            { yybegin(NORMAL); return P4TokenTypes.R_BRACE; }
"<"            { yybegin(NORMAL); return P4TokenTypes.L_ANGLE; }
">"            { yybegin(NORMAL); return P4TokenTypes.R_ANGLE; }

"!"            { yybegin(NORMAL); return P4TokenTypes.NOT; }
":"            { yybegin(NORMAL); return P4TokenTypes.COLON; }
","            { yybegin(NORMAL); return P4TokenTypes.COMMA; }
"?"            { yybegin(NORMAL); return P4TokenTypes.QUESTION; }
"."            { yybegin(NORMAL); return P4TokenTypes.DOT; }
"="            { yybegin(NORMAL); return P4TokenTypes.ASSIGN; }
";"            { yybegin(NORMAL); return P4TokenTypes.SEMICOLON; }
"@"            { yybegin(NORMAL); return P4TokenTypes.AT; }

.              { return P4TokenTypes.UNEXPECTED_TOKEN; }
[^]            { return P4TokenTypes.UNEXPECTED_TOKEN; }

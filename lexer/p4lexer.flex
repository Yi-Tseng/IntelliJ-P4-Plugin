
package org.p4.p4plugin;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import org.p4.p4plugin.parser.P4LangParser;
import org.p4.p4plugin.parser.P4LangParserUtil;
import org.p4.p4plugin.psi.P4LangTokenType;
import org.p4.p4plugin.psi.P4LangTypes;
import com.intellij.psi.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

%%

%class P4LangFlexLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType

%x COMMENT STRING
%x LINE1 LINE2 LINE3
%s NORMAL TYPEDEF
%{
private Logger log = LoggerFactory.getLogger(getClass());

%}

%%

[ \t\r]+        { return TokenType.WHITE_SPACE; }
[\n]            { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }
"//"([^\n])*          { yybegin(NORMAL); return P4LangTypes.COMMENT; }
"/*"            { yybegin(COMMENT); }
<COMMENT>([^*]|[*]+[^/*])*[*]+"/"  { yybegin(NORMAL); return P4LangTypes.COMMENT; }
<COMMENT>.     {  }
<COMMENT>\n     {  }

<YYINITIAL>"#line"      { yybegin(LINE1); }
<YYINITIAL>"# "         { yybegin(LINE1); }
<YYINITIAL>[ \t]*"#"    { yybegin(LINE3); }
<LINE1>[0-9]+         { yybegin(LINE2); }
<LINE2>\"[^\"]*       { yybegin(LINE3); }
<LINE1>[ \t]      {}
<LINE2>[ \t]      {}
<LINE1>.        { yybegin(LINE3); }
<LINE2>.        { yybegin(LINE3); }
<LINE3>.                {}
<LINE1>\n { yybegin(YYINITIAL); return P4LangTypes.PRE_PROCESS; }
<LINE2>\n { yybegin(YYINITIAL); return P4LangTypes.PRE_PROCESS; }
<LINE3>\n { yybegin(YYINITIAL); return P4LangTypes.PRE_PROCESS; }
<LINE1><<EOF>> { yybegin(YYINITIAL); return P4LangTypes.PRE_PROCESS; }
<LINE2><<EOF>> { yybegin(YYINITIAL); return P4LangTypes.PRE_PROCESS; }
<LINE3><<EOF>> { yybegin(YYINITIAL); return P4LangTypes.PRE_PROCESS; }
<COMMENT><<EOF>> { yybegin(YYINITIAL); return P4LangTypes.COMMENT; }
<NORMAL><<EOF>> { yybegin(YYINITIAL); }

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
"value_set"     { yybegin(NORMAL); return P4LangTypes.VALUESET; }
"void"          { yybegin(NORMAL); return P4LangTypes.VOID; }
"_"             { yybegin(NORMAL); return P4LangTypes.DONTCARE; }
[A-Za-z_][A-Za-z0-9_]* {
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
"@"            { yybegin(NORMAL); return P4LangTypes.AT;}

.              { return P4LangTypes.UNEXPECTED_TOKEN; }

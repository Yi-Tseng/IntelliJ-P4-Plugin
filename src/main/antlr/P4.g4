
// Notice: originally from p4-vscode-extension, with some modificaiton.
// https://github.com/fattaholmanan/p4-vscode-extension

grammar P4;

start : program;

program : input;

input :  /* epsilon */
	| input declaration
	| input ';'
	;

declaration
    : constantDeclaration
    | externDeclaration
    | actionDeclaration
    | parserDeclaration
    | typeDeclaration
    | controlDeclaration
    | instantiation
    | errorDeclaration
    | matchKindDeclaration
    | functionDeclaration
    ;

nonTypeName
    : type_or_id
    | APPLY
    | KEY
    | ACTIONS
    | STATE
    | ENTRIES
    | TYPE
    ;

name
    : nonTypeName
    | type_or_id
    ;

nonTableKwName
    : type_or_id
    // | TYPE_IDENTIFIER
    | APPLY
    | STATE
    | TYPE
    ;

optCONST
    : /* empty */
    | CONST
    ;

optAnnotations
    : /* empty */
    | annotations
    ;

annotations
    : annotation
    | annotations annotation
    ;

annotation
    : '@' name
    | '@' name '(' annotationBody ')'
    | PRAGMA name annotationBody
    ;

annotationBody
    : /* empty */
    | annotationBody '(' annotationBody ')'
    | annotationBody annotationToken
    ;

annotationToken
    : UNEXPECTED_TOKEN
    | ABSTRACT
    | ACTION
    | ACTIONS
    | APPLY
    | BOOL
    | BIT
    | CONST
    | CONTROL
    | DEFAULT
    | ELSE
    | ENTRIES
    | ENUM
    | ERROR
    | EXIT
    | EXTERN
    | FALSE
    | HEADER
    | HEADER_UNION
    | IF
    | IN
    | INOUT
    | INT
    | KEY
    | MATCH_KIND
    | TYPE
    | OUT
    | PARSER
    | PACKAGE
    | PRAGMA
    | RETURN
    | SELECT
    | STATE
    | STRUCT
    | SWITCH
    | TABLE
    | THIS
    | TRANSITION
    | TRUE
    | TUPLE
    | TYPEDEF
    | VARBIT
    | VALUESET
    | VOID
    | '_'

    | type_or_id
    // | TYPE_IDENTIFIER
    | STRING_LITERAL
    | INTEGER

    | '&&&'
    | '..'
    | '<<'
    | '&&'
    | '||'
    | '=='
    | '!='
    | '>='
    | '<='
    | '++'

    | '+'
    | '|+|'
    | '-'
    | '|-|'
    | '*'
    | '/'
    | '%'

    | '|'
    | '&'
    | '^'
    | '~'

    // Omit parens. These are handled in annotationBody, since they must be
    // balanced.
    // | '('
    // | ')'

    | '['
    | ']'
    | '{'
    | '}'
    | '<'
    | '>'

    | '!'
    | ':'
    | ','
    | '?'
    | '.'
    | '='
    | ';'
    | '@'
    ;

kvList
    : kvPair
    | kvList ',' kvPair
    ;

kvPair
    : name '=' expression
    ;

parameterList
    : /* empty */
    | nonEmptyParameterList
    ;

nonEmptyParameterList
    : parameter
    | nonEmptyParameterList ',' parameter
    ;

parameter
    : optAnnotations direction typeRef name
    | optAnnotations direction typeRef name '=' expression
    ;

direction
    : IN
    | OUT
    | INOUT
    | /* empty */
    ;

packageTypeDeclaration
    : optAnnotations PACKAGE name
      optTypeParameters
      '(' parameterList ')'
    ;

instantiation
	: annotations typeRef '(' argumentList ')' name ';'
	| typeRef '(' argumentList ')' name ';'
	/* experimental */
	| annotations typeRef '(' argumentList ')' name '=' objInitializer ';'
	/* experimental */
	| typeRef '(' argumentList ')' name '=' objInitializer ';'
    ;

objInitializer
    : '{' objDeclarations '}'
    ;

objDeclarations
    : /* empty */
    | objDeclarations objDeclaration
    ;

objDeclaration
    : functionDeclaration
    | instantiation
    ;

optConstructorParameters
    : /* empty */
    | '(' parameterList ')'
    ;

dotPrefix
    : '.'
    ;

parserDeclaration
    : parserTypeDeclaration optConstructorParameters '{' parserLocalElements parserStates '}'
    ;

parserLocalElements
    : /* empty */
    | parserLocalElements parserLocalElement
    ;

parserLocalElement
    : constantDeclaration
    | instantiation
    | variableDeclaration
    | valueSetDeclaration
    ;

parserTypeDeclaration
    : optAnnotations PARSER name optTypeParameters '(' parameterList ')'
    ;

parserStates
    : parserState
    | parserStates parserState
    ;

parserState
    : optAnnotations STATE name
      '{' parserStatements transitionStatement '}'
    ;

parserStatements
    : /* empty */
    | parserStatements parserStatement
    ;

parserStatement
    : assignmentOrMethodCallStatement
    | directApplication
    | variableDeclaration
    | constantDeclaration
    | parserBlockStatement
    ;

parserBlockStatement
    : optAnnotations '{' parserStatements '}'
    ;

transitionStatement
    : /* empty */
    | TRANSITION stateExpression
    ;

stateExpression
    : name ';'
    | selectExpression
    ;

selectExpression
    : SELECT '(' expressionList ')' '{' selectCaseList '}'
    ;

selectCaseList
    : /* empty */
    | selectCaseList selectCase
    ;

selectCase
    : keysetExpression ':' name ';'
    ;

keysetExpression
    : tupleKeysetExpression
    | simpleKeysetExpression
    ;

tupleKeysetExpression
    /* at least two elements in the tuple */
    : '(' simpleKeysetExpression ',' simpleExpressionList ')'
    ;

simpleExpressionList
    : simpleKeysetExpression
    | simpleExpressionList ',' simpleKeysetExpression
    ;

simpleKeysetExpression
    : expression
    | expression '&&&' expression
    | expression '..' expression
    | DEFAULT
    | '_'
    ;

valueSetDeclaration
    : optAnnotations VALUESET '<' baseType '>' '(' expression ')' name ';'
    | optAnnotations VALUESET '<' tupleType '>' '(' expression ')' name ';'
    | optAnnotations VALUESET '<' typeName '>' '(' expression ')' name ';'
    ;

controlDeclaration
    : controlTypeDeclaration optConstructorParameters
      '{' controlLocalDeclarations APPLY controlBody '}'
    ;

controlTypeDeclaration
    : optAnnotations
        CONTROL name
        optTypeParameters
        '(' parameterList ')'
	;

controlLocalDeclarations
    : /* empty */
    | controlLocalDeclarations controlLocalDeclaration
    ;

controlLocalDeclaration
    : constantDeclaration
    | actionDeclaration
    | tableDeclaration
    | instantiation
    | variableDeclaration
    ;

controlBody
    : blockStatement
    ;

externDeclaration
    : optAnnotations
        EXTERN nonTypeName
        optTypeParameters
        '{' methodPrototypes '}'
    | optAnnotations EXTERN functionPrototype ';'
    | optAnnotations EXTERN name ';'
    ;

methodPrototypes
    : /* empty */
    | methodPrototypes methodPrototype
    ;

functionPrototype
    : typeOrVoid name optTypeParameters '(' parameterList ')'
    ;

methodPrototype
    : optAnnotations functionPrototype ';'
    | optAnnotations ABSTRACT functionPrototype ';'
    | optAnnotations type_or_id '(' parameterList ')' ';'  // constructor
    ;

typeRef
    : baseType
    | typeName
    | specializedType
    | headerStackType
    | tupleType
    ;

namedType
    : typeName
    | specializedType
    ;

prefixedType
    : type_or_id
    | dotPrefix type_or_id
    ;

typeName
    : prefixedType
    ;

tupleType
    : TUPLE '<' typeArgumentList '>'
    ;

headerStackType
    : typeName '[' expression ']'
    ;

specializedType
    : typeName '<' typeArgumentList '>'
    ;

baseType
    : BOOL
    | ERROR
    | BIT
    | INT
    | BIT '<' INTEGER '>'
    | INT '<' INTEGER '>'
    | VARBIT '<' INTEGER '>'
    | BIT '<' expression '>'
    | INT '<' expression '>'
    | VARBIT '<' expression '>'
    ;

typeOrVoid
    : typeRef
    | VOID
    | type_or_id // This is necessary because template arguments may introduce the return type
    ;

optTypeParameters
    : /* empty */
    | '<' typeParameterList '>'
    ;

typeParameterList
    : name
    | typeParameterList ',' name
    ;

typeArg
    : typeRef
    | nonTypeName
        // This is necessary because template arguments may introduce the return type
    | VOID
    | '_'
    ;

typeArgumentList
    : /* empty */
    | typeArg
    | typeArgumentList ',' typeArg
    ;

realTypeArg
    : typeRef
    | VOID
    | '_'
    ;

realTypeArgumentList
    : realTypeArg
    | realTypeArgumentList ',' typeArg
    ;

typeDeclaration
    : derivedTypeDeclaration
    | typedefDeclaration ';'
    | parserTypeDeclaration ';'
    | controlTypeDeclaration ';'
    | packageTypeDeclaration ';'
    ;

derivedTypeDeclaration
    : headerTypeDeclaration
    | headerUnionDeclaration
    | structTypeDeclaration
    | enumDeclaration
    ;

headerTypeDeclaration
    : optAnnotations HEADER name '{' structFieldList '}'
    ;

structTypeDeclaration
    : optAnnotations STRUCT name '{' structFieldList '}'
    ;

headerUnionDeclaration
    : optAnnotations HEADER_UNION name '{' structFieldList '}'
    ;

structFieldList
    : /* empty */
    | structFieldList structField
    ;

structField
    : optAnnotations typeRef name ';'
    ;

enumDeclaration
    : optAnnotations ENUM name '{' identifierList '}'
    | optAnnotations ENUM BIT '<' INTEGER '>' name '{' specifiedIdentifierList '}'
    ;

specifiedIdentifierList
    : specifiedIdentifier
    | specifiedIdentifierList ',' specifiedIdentifier
    ;

specifiedIdentifier
    : name '=' initializer
    ;

errorDeclaration
    : ERROR '{' identifierList '}'
    ;

matchKindDeclaration
    : MATCH_KIND '{' identifierList '}'
    ;

identifierList
    : name
    | identifierList ',' name
    ;

typedefDeclaration
    : optAnnotations TYPEDEF typeRef name
    | optAnnotations TYPEDEF derivedTypeDeclaration name
    | optAnnotations TYPE typeRef name
    | optAnnotations TYPE derivedTypeDeclaration name
    ;

assignmentOrMethodCallStatement
    // These rules are overly permissive, but they avoid some conflicts
    : lvalue '(' argumentList ')' ';'
    | lvalue '<' typeArgumentList '>' '(' argumentList ')' ';'
    | lvalue '=' expression ';'
    ;

emptyStatement
    : ';'
    ;

exitStatement
    : EXIT ';'
    ;

returnStatement
    : RETURN ';'
    | RETURN expression ';'
    ;

conditionalStatement
    : IF '(' expression ')' statement 				//              %prec THEN
    | IF '(' expression ')' statement ELSE statement // 			%prec THEN
    ;

directApplication
    : typeName '.' APPLY '(' argumentList ')' ';'
    ;

statement
    : directApplication
    | assignmentOrMethodCallStatement
    | conditionalStatement
    | emptyStatement
    | blockStatement
    | returnStatement
    | exitStatement
    | switchStatement
    ;

blockStatement
    : optAnnotations '{' statOrDeclList '}'
    ;

statOrDeclList
    : /* empty */
    | statOrDeclList statementOrDeclaration
    ;

switchStatement
    : SWITCH '(' expression ')' '{' switchCases '}'
    ;

switchCases
    : /* empty */
    | switchCases switchCase
    ;

switchCase
    : switchLabel ':' blockStatement
    | switchLabel ':'
    ;

switchLabel
    : name
    | DEFAULT
    ;

statementOrDeclaration
    : variableDeclaration
    | constantDeclaration
    | statement
    | instantiation
    ;

tableDeclaration
    : optAnnotations
        TABLE name '{' tablePropertyList '}'
    ;

tablePropertyList
    : tableProperty
    | tablePropertyList tableProperty
    ;

tableProperty
    : KEY '=' '{' keyElementList '}'
    | ACTIONS '=' '{' actionList '}'
    | optAnnotations optCONST ENTRIES '=' '{' entriesList '}'
    | optAnnotations optCONST nonTableKwName '=' initializer ';'
    ;

keyElementList
    : /* empty */
    | keyElementList keyElement
    ;

keyElement
    : expression ':' name optAnnotations ';'
    ;

actionList
    : /* empty */
    | actionList actionRef ';'
    ;

actionRef
    : optAnnotations name
    | optAnnotations name '(' argumentList ')'
    ;

entry
    : keysetExpression ':' actionBinding optAnnotations ';'
    ;

actionBinding
    : lvalue '(' argumentList ')'
    | lvalue '<' typeArgumentList '>' '(' argumentList ')'
	;

entriesList
    : entry
    | entriesList entry
    ;

actionDeclaration
    : optAnnotations ACTION name '(' parameterList ')' blockStatement
    ;

variableDeclaration
    : annotations typeRef name optInitializer ';'
    | typeRef name optInitializer ';'
    ;

constantDeclaration
    : optAnnotations CONST typeRef name '=' initializer ';'
    ;

optInitializer
    : /* empty */
    | '=' initializer
    ;

initializer
    : expression
    ;

functionDeclaration
    : functionPrototype blockStatement
    ;

argumentList
    : /* empty */
    | nonEmptyArgList
    ;

nonEmptyArgList
    : argument
    | nonEmptyArgList ',' argument
    ;

argument
    : expression
    | name '=' expression
    | '_'
    ;

expressionList
    : /* empty */
    | expression
    | expressionList ',' expression
    ;

prefixedNonTypeName
    : nonTypeName
    | dotPrefix nonTypeName
    ;

lvalue
    : prefixedNonTypeName
    | THIS
    | lvalue '.' name
    | lvalue '[' expression ']'
    | lvalue '[' expression ':' expression ']'
    ;

expression
    : INTEGER
    | STRING_LITERAL
    | TRUE
    | FALSE
    | THIS
    | nonTypeName
    | dotPrefix nonTypeName
    | expression '[' expression ']'
    | expression '[' expression ':' expression ']'
    | '{' expressionList '}'
    | '(' expression ')'
    | '!' expression //%prec PREFIX
    | '~' expression //%prec PREFIX
    | '-' expression //%prec PREFIX
    | '+' expression //%prec PREFIX
    | typeName '.' name
    | ERROR '.' name
    | expression '.' name
    | expression '*' expression
    | expression '/' expression
    | expression '%' expression
    | expression '+' expression
    | expression '-' expression
    | expression '|+|' expression
    | expression '|-|' expression
    | expression '<<' expression
    | expression '>' '>' expression
    | expression '<=' expression
    | expression '>=' expression
    | expression '<' expression
    | expression '>' expression
    | expression '!=' expression
    | expression '==' expression
    | expression '&' expression
    | expression '^' expression
    | expression '|' expression
    | expression '++' expression
    | expression '&&' expression
    | expression '||' expression
    | expression '?' expression ':' expression
    | expression '<' realTypeArgumentList '>' '(' argumentList ')'
    // FIXME: the previous rule has the wrong precedence, and parses with
    // precedence weaker than casts.  There is no easy way to fix this in bison.
    | expression '(' argumentList ')'
    | namedType '(' argumentList ')'
	| '(' typeRef ')' expression // %prec PREFIX
    ;

type_or_id
	: IDENTIFIER
	| TYPE_IDENTIFIER
;

parserStateCondition
	: expression
	| expression '==' keysetExpression
	| expression '==' '(' keysetExpression ')'
	| keysetExpression '==' expression
	| '(' keysetExpression ')' '==' expression
	;

// Old rules
//preprocessorLine
//	: PREPROC_INCLUDE ppIncludeFileName
//	| PREPROC_DEFINE
//	| PREPROC_DEFINE expression
//	| PREPROC_DEFINE expression expression
//	| PREPROC_UNDEF IDENTIFIER
//	| PREPROC_IFDEF IDENTIFIER
//	| PREPROC_IFNDEF IDENTIFIER
//	| PREPROC_IF expression
//	| PREPROC_ELSEIF expression
//	| PREPROC_ELSE
//	| PREPROC_ENDIF
//	| PREPROC_LINE
//	| PREPROC_CC_LINE
//	;
//
//ppIncludeFileName
//	: STRING_LITERAL
//	| '<' ppIncludeFilePath '>'
//	;
//
//ppIncludeFilePath
//    : name
//    | ppIncludeFilePath '.' name
//    | './' ppIncludeFilePath
//    | '../' ppIncludeFilePath
//    | '/' ppIncludeFilePath
//    ;

/* ******* */
/*  LEXER  */
/* ******* */
PRAGMA						: '@pragma';

ABSTRACT					: 'abstract';
ACTION						: 'action';
ACTIONS						: 'actions';
APPLY						: 'apply';
BOOL						: 'bool';
BIT							: 'bit';
CONST						: 'const';
CONTROL						: 'control';
DEFAULT						: 'default';
ELSE						: 'else';
ENTRIES						: 'entries';
ENUM						: 'enum';
ERROR						: 'error';
EXIT						: 'exit';
EXTERN						: 'extern';
FALSE						: 'false';
HEADER						: 'header';
HEADER_UNION				: 'header_union';
IF							: 'if';
IN							: 'in';
INOUT						: 'inout';
INT							: 'int';
KEY							: 'key';
MATCH_KIND					: 'match_kind';
TYPE						: 'type';
OUT							: 'out';
PARSER						: 'parser';
PACKAGE						: 'package';
RETURN						: 'return';
SELECT						: 'select';
STATE						: 'state';
MEGA_STATE					: 'mega_state';
STRUCT						: 'struct';
SWITCH						: 'switch';
TABLE						: 'table';
THIS						: 'this';
TRANSITION					: 'transition';
TRUE						: 'true';
TUPLE						: 'tuple';
TYPEDEF						: 'typedef';
VARBIT						: 'varbit';
VALUESET					: 'value_set';
VOID						: 'void';
DONTCARE					: '_';

MASK						: '&&&';
RANGE						: '..';
SHL							: '<<';
AND							: '&&';
OR							: '||';
EQ							: '==';
NE							: '!=';
GE							: '>=';
LE							: '<=';
PP							: '++';
PLUS						: '+';
PLUS_SAT					: '|+|';
MINUS						: '-';
MINUS_SAT					: '|-|';
MUL							: '*';
DIV							: '/';
MOD							: '%';
BIT_OR						: '|';
BIT_AND						: '&';
BIT_XOR						: '^';
COMPLEMENT					: '~';
L_PAREN						: '(';
R_PAREN						: ')';
L_BRACKET					: '[';
R_BRACKET					: ']';
L_BRACE						: '{';
R_BRACE						: '}';
L_ANGLE						: '<';
R_ANGLE						: '>';
NOT							: '!';
COLON						: ':';
COMMA						: ',';
QUESTION					: '?';
DOT							: '.';
ASSIGN						: '=';
SEMICOLON					: ';';
AT							: '@';
UNEXPECTED_TOKEN			: '<*>.|\n';

WS 							: [ \t\r\n]+ -> channel(HIDDEN) ;
COMMENT 					: '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT 				: '//' ~[\r\n]* -> channel(HIDDEN) ;
fragment ESCAPED_QUOTE 		: '\\"';
STRING_LITERAL 				: '"' ( ESCAPED_QUOTE | ~('\n'|'\r') )*? '"';

// All preprocessor statements will be ignored in the lexer and the parser
PREPROCESSSOR             : '#' ~[\r\n]* -> channel(HIDDEN);

// Old rules
//PREPROC_INCLUDE			: '#include';
//PREPROC_DEFINE			: '#define';
//PREPROC_UNDEF				: '#undef';
//PREPROC_IFDEF				: '#ifdef';
//PREPROC_IFNDEF			: '#ifndef';
//PREPROC_ELSEIF			: '#elseif';
//PREPROC_ENDIF				: '#endif';
//PREPROC_LINE				: '#line';
//PREPROC_IF				: '#if';
//PREPROC_ELSE				: '#else';

IDENTIFIER					: [A-Za-z_][A-Za-z0-9_]*;
TYPE_IDENTIFIER				: [A-Za-z_][A-Za-z0-9_]*;

INTEGER						: HEX_INTEGER
							| DEC_INTEGER
							| OCT_INTEGER
							| BI_INTEGER
							| HEX_INTEGER_WITH
							| DEC_INTEGER_WITH
							| OCT_INTEGER_WITH
							| BI_INTEGER_WITH ;

fragment HEX_INTEGER 				: '0'[xX][0-9a-fA-F_]+ ;
fragment DEC_INTEGER				: '0'[dD][0-9_]+ | [0-9][0-9_]* ;
fragment OCT_INTEGER				: '0'[oO][0-7_]+ ;
fragment BI_INTEGER 				: '0'[bB][01_]+ ;
fragment HEX_INTEGER_WITH			: [0-9]+[ws]'0'[xX][0-9a-fA-F_]+ ;
fragment DEC_INTEGER_WITH			: [0-9]+[ws]'0'[dD][0-9_]+ | [0-9]+[ws][0-9_]+;
fragment OCT_INTEGER_WITH			: [0-9]+[ws]'0'[oO][0-7_]+ ;
fragment BI_INTEGER_WITH 			: [0-9]+[ws]'0'[bB][01_]+ ;

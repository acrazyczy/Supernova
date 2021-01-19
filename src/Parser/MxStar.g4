grammar MxStar;

program: (funcDef | classDef | varDef)* EOF;

funcDef: varType? Identifier '(' typeArgList? ')' suite;

classDef: Class Identifier '{' (varDef | funcDef)*'};';

Class: 'class';

varType: typeName ('['']')*;

varDef: varType Identifier ((',' Identifier)+ | ('=' expression))? ';';

typeArgList: varType Identifier (',' varType Identifier)*;

typeName: Bool | Int | Void | String | Identifier;

suite: '{' statement* '}';

statement: suite | varDef | controlStmt | compoundStmt | expression ';' | ';';

atom: Identifier trailer?;

atomicExpression: constant | This | atom;

trailer: '(' argList? ')';

argList: (expression ',')* expression;

expression: atomicExpression #atomicExpr
	| '(' expression ')' #innerExpr
	| expression op = ('++' | '--') #suffixExpr
	| <assoc=right> New generator #dynamicMemoryAllocation
	| expression '[' expression ']' #subscript
	| expression '.' atom #memberAccess
	| <assoc=right> op = ('++' | '--') expression #prefixExpr
	| <assoc=right> op = ('+' | '-') expression #prefixExpr
	| <assoc=right> op = ('!' | '~') expression #prefixExpr
	| expression op = ('*' | '/' | '%') expression #arithmeticExpr
	| expression op = ('+' | '-') expression #arithmeticExpr
	| expression op = ('<<' | '>>') expression #bitwiseShiftExpr
	| expression op = ('<' | '<=' | '>' | '>=') expression #relationalExpr
	| expression op = ('==' | '!=') expression #relationalExpr
	| expression op = ('&' | '^' | '|') expression #bitwiseExpr
	| expression op = ('&&' | '||') expression #logicExpr
	| <assoc=right> expression op = '=' expression #assigmentExpr;

New: 'new';

controlStmt: returnStmt | breakStmt | continueStmt;

returnStmt: Return expression? ';';

Return: 'return';

breakStmt: Break ';';

Break: 'break';

continueStmt: Continue ';';

Continue: 'continue';

compoundStmt: ifStmt | whileStmt | forStmt;

ifStmt: If '(' expression ')' trueStmt = statement (Else falseStmt = statement)?;

If: 'if';

Else: 'else';

whileStmt: While '(' expression ')' statement;

While: 'while';

forStmt: For '(' initExpr = expression? ';' condExpr = expression? ';' incrExpr = expression? ')' statement;

For: 'for';

This: 'this';

Bool: 'bool';

Int: 'int';

Void: 'void';

String: 'string';

constant: logicConstant | IntegerConstant | StringConstant | Null;

logicConstant: True | False;

IntegerConstant: DecimalInteger;

fragment DecimalInteger: [1-9] [0-9]* | '0';

StringConstant: '"' Char* '"';

fragment Char: ~["\\\n\r] | '\\n' | '\\\\' | '\\"';

True: 'true';

False: 'false';

Null: 'null';

generator: typeName ('[' expression ']')+ ('[' ']')* #arrayGenerator
	| typeName ('('')')? #classGenerator;

WhiteSpace: [ \t]+ -> skip;

NewLine: ('\r''\n'?|'\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\r\n]* -> skip;

LeftParen : '(';
RightParen : ')';
LeftBracket : '[';
RightBracket : ']';
LeftBrace : '{';
RightBrace : '}';

Lt : '<';
Leq : '<=';
Gt : '>';
Geq : '>=';
Lsh : '<<';
Rsh : '>>';

Plus : '+';
PlusPlus : '++';
Minus : '-';
MinusMinus : '--';
Star : '*';
Div : '/';
Mod : '%';

And : '&';
Or : '|';
AndAnd : '&&';
OrOr : '||';
Caret : '^';
Not : '!';
Tilde : '~';

Question : '?';
Colon : ':';
Semi : ';';
Comma : ',';

Assign : '=';
Equal : '==';
NotEqual : '!=';

Dot : '.';

Identifier : [a-zA-Z] [a-zA-Z_0-9]*;
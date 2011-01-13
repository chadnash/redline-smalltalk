/*
Redline Smalltalk is licensed under the MIT License

Redline Smalltalk Copyright (c) 2010 James C. Ladd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
grammar Smalltalk;

options {
	language = Java;
}

@header {
	package st.redline.smalltalk.interpreter;
}

@lexer::header {
	package st.redline.smalltalk.interpreter;
}

program returns [Program n]
	:	sequence EOF {$n = new Program($sequence.n);}
	;	

sequence returns [Sequence n]
	:	statements {$n = new Sequence($statements.n);}
	;

statements returns [Statements n]
	:	statementList '.'? {$n = new Statements($statementList.n);}
	|	statementList '.'  {$n = new Statements($statementList.n);} '^' expression {$n.answer($expression.n);} '.'?
	|	'^' expression {$n = new Statements($expression.n);} '.'?
	;
	
statementList returns [StatementList n]
	:	{ $n = new StatementList(); }	
		e1 = expression {$n.add($e1.n);} ( '.' e2 = expression {$n.add($e2.n);} )*
	;
	
expression returns [Expression n]
	:	cascade {$n = new Expression($cascade.n);}
	;

cascade returns [Cascade n]
	:	messageSend {$n = new Cascade($messageSend.n);}
	;

messageSend returns [MessageSend n]
	:	keywordMessageSend {$n = new MessageSend($keywordMessageSend.n);}
	|	unaryMessageSend {$n = new MessageSend($unaryMessageSend.n);}
	;

keywordMessageSend returns [KeywordMessageSend n]
	:	primary keywordMessage {$n = new KeywordMessageSend($primary.n, $keywordMessage.n);}
	;
	
keywordMessage returns [KeywordMessage n]
	:	k1 = KEYWORD a1 = keywordArgument {$n = new KeywordMessage($k1.text, $k1.line, $a1.n);} ( k2 = KEYWORD a2 = keywordArgument {$n.add($k2.text, $k2.line, $a2.n);} )*
	;

keywordArgument returns [KeywordArgument n]
	:	primary {$n = new KeywordArgument($primary.n);}
	;

unaryMessageSend returns [UnaryMessageSend n]
	:	primary {$n = new UnaryMessageSend($primary.n);} ( unaryMessage {$n.add($unaryMessage.n);} )+
	;

unaryMessage returns [UnaryMessage n]
	:	NAME {$n = new UnaryMessage($NAME.text, $NAME.line);}
	;

primary returns [Primary n]
	:	variable {$n = $variable.n;}
	|	literal {$n = $literal.n;}
	;
	
variable returns [Variable n]
	:	NAME {$n = new Variable($NAME.text, $NAME.line);}
	;

literal returns [Literal n]
	:	'#' NAME {$n = new Symbol($NAME.text, $NAME.line);}
	|	STRING {$n = new StString($STRING.text, $STRING.line);}
	;
		
NAME: ('a'..'z' | 'A'..'Z')('a'..'z' | 'A'..'Z' | '0'..'9')*;
KEYWORD: NAME ':';
WHITESPACE: (' ' | '\t' | '\n' | '\r' | '\f' )+ {$channel = HIDDEN;};
COMMENT: '"' .* '"' {$channel = HIDDEN;};
STRING: '\'' .* '\'';
 
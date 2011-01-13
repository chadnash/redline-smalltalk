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
package st.redline.smalltalk.interpreter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import st.redline.smalltalk.Smalltalk;
import st.redline.smalltalk.SourceFile;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AnalyserTest {

	private static final String PACKAGE_INTERNAL_NAME = "st/redline/smalltalk";
	private static final String CLASS_NAME = "Test";
	private static final String UNARY_MESSAGE = "new";
	private static final String KEYWORD_SELECTOR = "at:put:";
	private static final String STRING = "'foo'";
	private static final String SYMBOL = "foo";
	private static final int LINE_NUMBER= 42;
	private static final List<String> KEYWORD_MESSAGE_LIST = new ArrayList<String>();
	static {
		KEYWORD_MESSAGE_LIST.add("at:");
		KEYWORD_MESSAGE_LIST.add("put:");
	}

	@Mock Smalltalk smalltalk;
	@Mock Generator generator;
	@Mock Program program;
	@Mock Sequence sequence;
	@Mock Statements statements;
	@Mock SourceFile sourceFile;
	@Mock StatementList statementList;
	@Mock Cascade cascade;
	@Mock MessageSend messageSend;
	@Mock Expression expression;
	@Mock UnaryMessageSend unaryMessageSend;
	@Mock UnaryMessage unaryMessage;
	@Mock KeywordMessageSend keywordMessageSend;
	@Mock KeywordMessage keywordMessage;
	@Mock KeywordMessagePart keywordMessagePart;
	@Mock KeywordArgument keywordArgument;
	@Mock StString string;
	@Mock Symbol symbol;
	@Mock Variable primary;
	@Mock Variable variable;
	private Analyser analyser;

	@Before public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		analyser = new Analyser(smalltalk, generator);
		when(program.sequence()).thenReturn(sequence);
		when(sequence.statements()).thenReturn(statements);
		when(statements.statementList()).thenReturn(statementList);
		when(expression.cascade()).thenReturn(cascade);
		when(cascade.messageSend()).thenReturn(messageSend);
		when(messageSend.unaryMessageSend()).thenReturn(unaryMessageSend);
		when(smalltalk.currentFile()).thenReturn(sourceFile);
		when(sourceFile.nameWithoutExtension()).thenReturn(CLASS_NAME);
		when(sourceFile.parentPathWithoutUserPath()).thenReturn(PACKAGE_INTERNAL_NAME);
	}

	@Test public void shouldGenerateProgramFromProgramNode() {
		analyser.visit(program);
		verify(generator).openClass(CLASS_NAME, PACKAGE_INTERNAL_NAME);
		verify(generator).closeClass();
	}

	@Test public void shouldGenerateClassLookupWhenPrimaryVariableIsClassName() {
		when(variable.isClassReference()).thenReturn(true);
		when(variable.name()).thenReturn(CLASS_NAME);
		when(variable.line()).thenReturn(LINE_NUMBER);
		analyser.visit(variable);
		verify(generator).classLookup(CLASS_NAME, LINE_NUMBER);
	}

	@Test public void shouldGenerateUnarySendFromUnaryMessage() {
		when(unaryMessage.selector()).thenReturn(UNARY_MESSAGE);
		when(unaryMessage.line()).thenReturn(LINE_NUMBER);
		analyser.visit(unaryMessage);
		verify(generator).unarySend(UNARY_MESSAGE, LINE_NUMBER);
	}

	@Test public void shouldGenerateKeywordSendFromKeywordMessage() {
		when(keywordMessage.keywords()).thenReturn(KEYWORD_MESSAGE_LIST);
		when(keywordMessage.line()).thenReturn(LINE_NUMBER);
		analyser.visit(keywordMessage);
		verify(keywordMessage).eachAccept(analyser);
		verify(generator).keywordSend(KEYWORD_SELECTOR, 2, LINE_NUMBER);
	}

	@Test public void shouldGeneratePopWhenExpressionResultNotAnswered() {
		when(expression.isAnswered()).thenReturn(false);
		analyser.visit(expression);
		verify(generator).stackPop();
	}

	@Test public void shouldGeneratePrimitiveConversionForString() {
		when(string.string()).thenReturn(STRING);
		when(string.line()).thenReturn(LINE_NUMBER);
		analyser.visit(string);
		verify(generator).primitiveStringConversion(STRING, LINE_NUMBER);
	}

	@Test public void shouldGeneratePrimitiveConversionForSymbol() {
		when(symbol.symbol()).thenReturn(SYMBOL);
		when(symbol.line()).thenReturn(LINE_NUMBER);
		analyser.visit(symbol);
		verify(generator).primitiveSymbolConversion(SYMBOL, LINE_NUMBER);
	}

	@Test public void shouldVisitChildOfProgramNode() {
		analyser.visit(program);
		verify(sequence).accept(analyser);
	}

	@Test public void shouldVisitChildOfSequenceNode() {
		analyser.visit(sequence);
		verify(statements).accept(analyser);
	}

	@Test public void shouldVisitChildOfStatementsNode() {
		analyser.visit(statements);
		verify(statementList).accept(analyser);
	}

	@Test public void shouldVisitEachNodeOfStatementListNode() {
		analyser.visit(statementList);
		verify(statementList).eachAccept(analyser);
	}

	@Test public void shouldVisitExpressionNode() {
		analyser.visit(expression);
		verify(cascade).accept(analyser);
	}

	@Test public void shouldVisitChildOfCascadeNode() {
		analyser.visit(cascade);
		verify(messageSend).accept(analyser);
	}

	@Test public void shouldVisitUnaryMessageSendWhenMessageSendIsUnaryType() {
		when(messageSend.isUnaryMessageSend()).thenReturn(true);
		when(messageSend.unaryMessageSend()).thenReturn(unaryMessageSend);
		analyser.visit(messageSend);
		verify(unaryMessageSend).accept(analyser);
	}

	@Test public void shouldVisitKeywordMessageSendWhenMessageSendIsKeywordType() {
		when(messageSend.isKeywordMessageSend()).thenReturn(true);
		when(messageSend.keywordMessageSend()).thenReturn(keywordMessageSend);
		analyser.visit(messageSend);
		verify(keywordMessageSend).accept(analyser);
	}

	@Test public void shouldVisitKeywordMessageSendParts() {
		when(keywordMessageSend.primary()).thenReturn(primary);
		when(keywordMessageSend.keywordMessage()).thenReturn(keywordMessage);
		analyser.visit(keywordMessageSend);
		verify(primary).accept(analyser);
		verify(keywordMessage).accept(analyser);
	}

	@Test public void shouldVisitEachNodeOfKeywordMessageListNode() {
		analyser.visit(keywordMessage);
		verify(keywordMessage).eachAccept(analyser);
	}

	@Test public void shouldVisitKeywordMessagePart() {
		when(keywordMessagePart.keywordArgument()).thenReturn(keywordArgument);
		analyser.visit(keywordMessagePart);
		verify(keywordArgument).accept(analyser);
	}

	@Test public void shouldVisitUnaryMessageSendParts() {
		when(unaryMessageSend.primary()).thenReturn(primary);
		analyser.visit(unaryMessageSend);
		verify(primary).accept(analyser);
		verify(unaryMessageSend).eachAccept(analyser);
	}
}
/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.compiler;

public interface Expression extends VisitableNode {
	void leaveResultOnStack();
	void duplicateResultOnStack();
	boolean isAnswerExpression();
}

/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core.reflector;

public interface InspectorVisitor {
    void visitBegin( String className);
    void visitEnd( String className);
    void visitConstructorBegin( String className, String constructorName, int parameterCount);
    void visitConstructorEnd( String className, String constructorName, int parameterCount);
    void visitConstructorsBegin( String className);
    void visitConstructorsEnd( String className);
    void visitParameterTypesBegin(int length);
    void visitParameterTypesEnd(int length);
    void visitParameterType(String parameterType, int index);
    void visitMethodsBegin( String name);
    void visitMethodsEnd( String name);
    void visitMethodBegin( String className, String methodName, int parameterCount, String returnType);
    void visitMethodEnd(String className, String methodName, int parameterCount, String returnType);
}

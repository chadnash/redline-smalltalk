/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core.reflector;

public class NoOpInspector implements InspectorVisitor {

    public void visitBegin( String className) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitEnd( String className) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitConstructorBegin( String className, String constructorName, int parameterCount) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitConstructorEnd( String className, String constructorName, int parameterCount) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitConstructorsBegin( String className) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitConstructorsEnd( String className) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitParameterTypesBegin(int length) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitParameterTypesEnd(int length) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitParameterType(String parameterType, int index) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitMethodsBegin( String name) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitMethodsEnd( String name) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitMethodBegin( String className, String methodName, int parameterCount, String returnType) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitMethodEnd( String className, String methodName, int parameterCount, String returnType) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }
}

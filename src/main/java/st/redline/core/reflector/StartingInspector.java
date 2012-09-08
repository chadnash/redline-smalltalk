/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core.reflector;

import st.redline.core.DynamicJavaClassAdaptor;

public class StartingInspector extends NoOpInspector {

    private final Reflector reflector;

    public StartingInspector(Reflector reflector) {
        this.reflector = reflector;
    }

    public void visitBegin( String fullClassName) {
    	String wrappingClassName = DynamicJavaClassAdaptor.fullyQualifiedClassNameForJavaClassWrapperClassNamed(fullClassName);
        reflector.append("\"@: "+ wrappingClassName + "\"\n") // ensure never attempt to use a restricted pacage like java.util and also ensure I never have a namespace clash
                 .append("Object < #" + className(fullClassName) + ".\n");
    }

    private String className(String fullClassName) {
    	String wrappingClassName = DynamicJavaClassAdaptor.fullyQualifiedClassNameForJavaClassWrapperClassNamed(fullClassName);
        return wrappingClassName.substring(wrappingClassName.lastIndexOf('.') + 1) ;
    }

    public void visitEnd( String fullClassName) {
        reflector.append("\n")
                 .append(className(fullClassName))
                 .append(" initialize.\n");
    }

    public void visitConstructorsBegin( String className) {
        this.reflector.useConstructorVisitor();
        this.reflector.visitConstructorsBegin( className);
    }

    public void visitConstructorsEnd( String className) {
        this.reflector.useConstructorVisitor();
        this.reflector.visitConstructorsEnd( className);
    }

    public void visitConstructorBegin( String className, String constructorName, int parameterCount) {
        this.reflector.useConstructorVisitor();
        this.reflector.visitConstructorBegin( className, constructorName, parameterCount);
    }

    public void visitMethodsBegin( String className) {
        this.reflector.useMethodVisitor();
        this.reflector.visitMethodsBegin( className);
    }

    public void visitMethodBegin( String className, String constructorName, int parameterCount, String returnType) {
        this.reflector.visitMethodBegin( className, constructorName, parameterCount, returnType);
    }

    public void visitMethodsEnd( String className) {
        this.reflector.useMethodVisitor();
        this.reflector.visitMethodsEnd( className);
    }
}

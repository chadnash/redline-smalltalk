/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core.reflector;

import java.util.HashMap;
import java.util.Map;

import st.redline.core.DynamicJavaClassAdaptor;

public class ConstructorInspector implements InspectorVisitor {

    protected static final Map<String, String> PRIMITIVE_TO_SIGNATURE_TYPE = new HashMap<String, String>();
    static {
        PRIMITIVE_TO_SIGNATURE_TYPE.put("long", "J");
        PRIMITIVE_TO_SIGNATURE_TYPE.put("int", "I");
        PRIMITIVE_TO_SIGNATURE_TYPE.put("char", "C");
        PRIMITIVE_TO_SIGNATURE_TYPE.put("byte", "B");
    }

    protected final Reflector reflector;
    protected String className;
    protected String javaClassName;
    protected String javaConstructorName;
    protected StringBuilder javaArgumentSignature = new StringBuilder();
    protected StringBuilder methodSymbol = new StringBuilder();
    protected String[] javaArgumentTypes;
    protected String classNameAdaptor;
    protected int constructorsCount;

    public ConstructorInspector(Reflector reflector) {
        this.reflector = reflector;
    }

    public void visitBegin( String className) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitEnd( String className) {
        throw new IllegalStateException("This inspector should not be getting this.");
    }

    public void visitConstructorsBegin( String className) {
        constructorsCount = 0;
    }

    public void visitConstructorsEnd( String className) {
    	String wrappingClassName = DynamicJavaClassAdaptor.fullyQualifiedClassNameForJavaClassWrapperClassNamed(className);
        classNameAdaptor = wrappingClassName.substring(wrappingClassName.lastIndexOf('.') + 1);
        reflector.append("\n")
                .append(classNameAdaptor)
                .append(" class atSelector: #with: put: [ :args || selector |\n")
                .append("  selector := self selectorFor: args withPrefix: 'with'.\n")
                .append("  JVM aload: 1;\n")
                .append("      arg: 0;\n")
                .append("      temp: 0;\n")
                .append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'perform' matching: '(Lst/redline/core/PrimObject;Lst/redline/core/PrimObject;)Lst/redline/core/PrimObject;'.\n")
                .append("].\n")
                .append("\n")
                .append(classNameAdaptor)
                .append(" class atSelector: #selectorFor:withPrefix: put: [ :args :prefix |\n")
                .append("  <primitive: 227>\n")
                .append("].\n");
        reflector.usePreviousVisitor();
    }

    public void visitConstructorBegin( String className, String constructorName, int parameterCount) {
        this.className = className;
    	String wrappingClassName = DynamicJavaClassAdaptor.fullyQualifiedClassNameForJavaClassWrapperClassNamed(className);
        this.classNameAdaptor = wrappingClassName.substring(wrappingClassName.lastIndexOf('.') + 1);
        this.javaClassName = className.replace(".", "/");
        this.javaConstructorName = constructorName.replace(".", "/");
        this.javaArgumentTypes = new String[parameterCount];
        this.methodSymbol.append("with");
        System.out.println("visitConstructorBegin: " + className + " " + " " + parameterCount + " ");
        reflector.append("\n")
                 .append(classNameAdaptor)
                 .append(" class atSelector: #");
    }

    public void visitConstructorEnd( String className, String constructorName, int parameterCount) {
        reflector.append("      dup;\n");
        loadConstructorArguments(parameterCount);
        reflector.append("      invokeSpecial: '")
            .append(javaConstructorName)
            .append("' method: '<init>' matching: '(")
            .append(javaArgumentSignature.toString())
            .append(")V';\n")
            .append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;'.\n")
            .append("  ^ obj.\n")
            .append("].\n");
        reflector.usePreviousVisitor();
    }

    private void loadConstructorArguments(int parameterCount) {
        for (int i = 0; i < parameterCount; i++) {
            reflector.append("      arg: 0 at: ")
                     .append(i + 1)
                     .append(";\n")
                     .append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '()Ljava/lang/Object;';\n");
            appendArgumentConversion(javaArgumentTypes[i]);
        }
    }

    private void appendArgumentConversion(String type) {
        if (type.startsWith("L")) {
            reflector.append("      checkcast: '")
                     .append(type.substring(1, type.length() - 1))
                     .append("';\n");
        } else {
            // type is primitive so map from Redline internal type to java type.
            if (type.equals("I")) {
                reflector.append("      checkcast: 'java/math/BigDecimal';\n")
                         .append("      invokeVirtual: 'java/math/BigDecimal' method: 'intValue' matching: '()I';\n");
            } else if (type.equals("J")) {
                reflector.append("      checkcast: 'java/math/BigDecimal';\n")
                         .append("      invokeVirtual: 'java/math/BigDecimal' method: 'longValue' matching: '()J';\n");
            } else {
                throw new IllegalStateException("Need to cater for conversion of type '" + type + "'.");
            }
        }
    }

    public void visitParameterTypesBegin(int length) {
    }

    public void visitParameterTypesEnd(int length) {
        boolean isNew = length == 0;
        reflector.append(isNew ? "new" : methodSymbol.toString() + ":")
                .append(" put: [")
                .append(length == 0 ? " " : " :args |")
                .append("| obj |\n  obj := ")
                .append(isNew ? "super new.\n" : "self new.\n")
                .append("  JVM temp: 0;\n      new: '")
                .append(javaClassName)
                .append("';\n");
    }

    public void visitParameterType(String parameterType, int index) {
//        System.out.println("Parameter: " + parameterType + " @ " + index);
        String javaType = javaSignature(parameterType);
        javaArgumentTypes[index] = javaType;
        javaArgumentSignature.append(javaType);
        methodSymbol.append(methodSymbolType(parameterType));
        System.out.println("chad2>"+methodSymbol.toString());
    }

    public void visitMethodsBegin( String name) {
 System.out.println("chad7>this"+this);    	
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

    private Object methodSymbolType(String parameterType) {
    	
    	if (parameterType.startsWith("[L")) {
    		  if (parameterType.indexOf('.') != -1)
    			  return "ArrayOf" + parameterType.substring(parameterType.lastIndexOf('.') + 1).replace(";", "");
    		  else
    			  return "ArrayOf" +PRIMITIVE_TO_SIGNATURE_TYPE.get(parameterType); 
		}
        if (parameterType.indexOf('.') != -1)
            return parameterType.substring(parameterType.lastIndexOf('.') + 1);
        return PRIMITIVE_TO_SIGNATURE_TYPE.get(parameterType);
    }

    private String javaSignature(String parameterType) {
        if (parameterType.indexOf('.') != -1)
            return "L" + parameterType.replace(".", "/") + ";";
        return PRIMITIVE_TO_SIGNATURE_TYPE.get(parameterType);
    }
}

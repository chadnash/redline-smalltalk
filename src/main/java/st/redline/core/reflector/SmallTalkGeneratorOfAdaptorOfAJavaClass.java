package st.redline.core.reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import st.redline.core.DynamicJavaClassAdaptor;

public class SmallTalkGeneratorOfAdaptorOfAJavaClass 
{	
	public static void  main(String[] args)
	{
		System.out.println(new SmallTalkGeneratorOfAdaptorOfAJavaClass("java.util.ArrayList").adaptorSource());
	}
	String fullyQualifiedJavaClassName;
	public SmallTalkGeneratorOfAdaptorOfAJavaClass(String thefullyQualifiedJavaClassName)
	{
		fullyQualifiedJavaClassName = thefullyQualifiedJavaClassName;
	}
	
	String fullyQualifiedAdaptorClassName()
	{
		return "smallTalkClassesThatAdaptJavaClasses." + fullyQualifiedJavaClassName + "Adaptor"; // ensure never attempt to use a restricted package like java.util and also ensure I never have a namespace clash
	}
	String unQualifiedAdaptorClassName()
	{
		return stringAfterLastOrEmptyString(fullyQualifiedAdaptorClassName(),".");
	}
	String fullyQualifiedJvmClassNameOfJavaClassToBeAdapted()
	{
		return fullyQualifiedJavaClassName.replace(".", "/");
	}
	Class<?> javaClassToAdapt() 
	{
		try {
			return Class.forName(fullyQualifiedJavaClassName);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(fullyQualifiedJavaClassName + " fail to be recognized as a valid java class");
		}
	}
	public String adaptorSource()
	{	
		StringBuffer sourceBuf = new StringBuffer();
		Class<?> javaClassToAdapt  = javaClassToAdapt();
		sourceBuf.append("\"@: "+  fullyQualifiedAdaptorClassName() + "\"\n"); 
		
		sourceBuf.append(importStatementSmallTalkSourceForReturnValueClasses());
		
        sourceBuf.append("Object < #" + unQualifiedAdaptorClassName() + ".\n");
        
		for(Constructor<?> constructor : javaClassToAdapt.getDeclaredConstructors() )
		{
			sourceBuf.append(smalltalkSourceForMethodThatAdaptsConstructor(constructor));
			sourceBuf.append("\n");
		}
		
		sourceBuf.append(smalltalkSourceForConstructorHelperMethods());
		
		for(Method javaMethod : javaClassToAdapt.getMethods() )
		{
			if (!IGNORED_METHODS.containsKey(javaMethod.getName()))
			{
				sourceBuf.append(smalltalkSourceForMethodThatAdaptsJavaMethod(javaMethod));
				sourceBuf.append("\n");
			}
		}
		
		sourceBuf.append("\n" + unQualifiedAdaptorClassName() +" initialize.\n");
		return sourceBuf.toString();
	}
	
	private String importStatements() 
	{	
		StringBuffer sourceBuf = new StringBuffer();
		
		for(Class<?> javaClass: javaClassesToImport())
		{
			String localName= localNameForJavaClass(javaClass)
			sourceBuf.append("import " +javaClass.getName() + " as " +localName +"\n" ); 
		}
		
        return sourceBuf.toString();
	}
	private static Map<String,String>  LOCAL_SMALLTALK_NAME_BY_JAVA_CLASS_NAME = new HashMap<String,String>();
	String localNameForJavaClass(Class<?> javaClass)
	{
		String localClassname = javaClass.getName().replace(".", "-");
		LOCAL_SMALLTALK_NAME_BY_JAVA_CLASS_NAME.put(javaClass.getName(),localClassname);
		return localClassname;
	}
	private String smalltalkSourceForMethodThatAdaptsConstructor(Constructor<?> constructor) 
	{	
		StringBuffer sourceBuf = new StringBuffer();
		
        boolean constructorHasNoArgs = constructor.getParameterTypes().length==0;
        sourceBuf.append(unQualifiedAdaptorClassName()+ " class atSelector: #"+ methodNameForMethodAdaptingIndividualConstructor(constructor) + " put: [");
	    sourceBuf.append(constructorHasNoArgs ? " " : " :args |")
	        .append("| obj |\n  obj := ")
	        .append(constructorHasNoArgs ? "super new.\n" : "self new.\n");
	    sourceBuf.append("  JVM atTemp: 0;\n");
	    sourceBuf.append("      new: '"+ fullyQualifiedJvmClassNameOfJavaClassToBeAdapted()+"';\n");
        sourceBuf.append("      dup;\n");
        sourceBuf.append(smalltalkSourceToCovertArgumentsAndPutOnStack(constructor.getParameterTypes()));
        sourceBuf.append("      invokeSpecial: '"+fullyQualifiedJvmClassNameOfJavaClassToBeAdapted()+"' method: '<init>' matching: '(" + javaArgsSignature(constructor.getParameterTypes())+")V';\n")
            .append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;'.\n")
            .append("  ^ obj.\n")
            .append("].\n");
        return sourceBuf.toString();
	}
	private String smalltalkSourceForMethodThatAdaptsJavaMethod(Method javaMethod)
	{	
		StringBuffer sourceBuf = new StringBuffer();
		boolean methodHasNoArgs = javaMethod.getParameterTypes().length==0;
		sourceBuf.append(unQualifiedAdaptorClassName()+ " atSelector: #"+ methodNameForMethodAdaptingIndividualMethod(javaMethod) + " put: [");
		sourceBuf.append(methodHasNoArgs ? " " : " :args |");
        
		sourceBuf.append("| rtn |\n");
		?sourceBuf.append("  rtn := "+ localSmallTalkClassNameForImportedJavaClass(javaMethod.getReturnType())+" new \n");
        
		sourceBuf.append("  JVM atTemp: 0;\n"); // put rtn on the stack so javaValue can be sent to it 
		sourceBuf.append("      aload: 1;\n");
	    sourceBuf.append(smalltalkSourceToCovertArgumentsAndPutOnStack(javaMethod.getParameterTypes()));
        sourceBuf.append("      invokeVirtual: '"+ fullyQualifiedJvmClassNameOfJavaClassToBeAdapted() +"' method: "+ javaMethod.getName() + " matching: '(" + javaArgsSignature(javaMethod.getParameterTypes())+")V';\n");
        sourceBuf.append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '(Ljava/lang/Object;)Lst/redline/core/PrimObject;'.\n");
        sourceBuf.append("      putTemp: 0;\n");// rtn = stack
        	
		sourceBuf.append("].\n");
		
		 return sourceBuf.toString();
	}
	

   

    public void visitParameterTypesBegin(int length) {
    }

   
	  
	private String smalltalkSourceForConstructorHelperMethods() 
	{
		StringBuffer sourceBuf = new StringBuffer();
		
		sourceBuf
                .append(unQualifiedAdaptorClassName())
                .append(" class atSelector: #with: put: [ :args || selector |\n")
                .append("  selector := self selectorFor: args withPrefix: 'with'.\n")
                .append("  JVM aload: 1;\n")
                .append("      arg: 0;\n")
                .append("      atTemp: 0;\n")
                .append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'perform' matching: '(Lst/redline/core/PrimObject;Lst/redline/core/PrimObject;)Lst/redline/core/PrimObject;'.\n")
                .append("].\n")
                .append("\n")
                .append(unQualifiedAdaptorClassName())
                .append(" class atSelector: #selectorFor:withPrefix: put: [ :args :prefix |\n")
                .append("  <primitive: 227>\n")
                .append("].\n");
		 return sourceBuf.toString();
	}
	private String smalltalkSourceToCovertArgumentsAndPutOnStack(Class<?>[] argTypes) 
	{
		StringBuffer sourceBuf = new StringBuffer();
        for (int i = 0; i < argTypes.length; i++) {
        	sourceBuf.append(smalltalkSourceToGiveArgumentTheAppropiateJavaValue(i,argTypes[i]));
        }
        return sourceBuf.toString();
	}
	private String smalltalkSourceToGiveArgumentTheAppropiateJavaValue(int argIndex,Class<?> argType) 
	{
		StringBuffer sourceBuf = new StringBuffer();
		sourceBuf.append("      arg: 0 at: "+ (argIndex + 1) + ";\n")
        	.append("      invokeVirtual: 'st/redline/core/PrimObject' method: 'javaValue' matching: '()Ljava/lang/Object;';\n");
		sourceBuf.append(smalltalkSourceToConvertAJavaVariable(argType));
		return sourceBuf.toString();
	}
	
	 private String smalltalkSourceToConvertAJavaVariable(Class<?> argType) {
		 	String type = javaSignatureXXX(argType); 
		 	StringBuffer sourceBuf = new StringBuffer();
			
			
	        if (type.startsWith("L")) {
	        	sourceBuf.append("      checkcast: '")
	                     .append(type.substring(1, type.length() - 1))
	                     .append("';\n");
	        } else {
	            // type is primitive so map from Redline internal type to java type.
	            if (type.equals("I")) {
	            	sourceBuf.append("      checkcast: 'java/math/BigDecimal';\n")
	                         .append("      invokeVirtual: 'java/math/BigDecimal' method: 'intValue' matching: '()I';\n");
	            } else if (type.equals("J")) {
	            	sourceBuf.append("      checkcast: 'java/math/BigDecimal';\n")
	                         .append("      invokeVirtual: 'java/math/BigDecimal' method: 'longValue' matching: '()J';\n");
	            } else {
	                throw new IllegalStateException("Need to cater for conversion of type '" + type + "'.");
	            }
	        }
	        return sourceBuf.toString();
	   }
	 
	 String methodNameForMethodAdaptingIndividualMethod(Method m)
	  {	
		  String methodName=m.getName();
		  for(Class<?> parameterType : m.getParameterTypes())
		  {
			  methodName = methodName + methodSymbolComponentForType(parameterType);
		  }
		  return methodName =methodName+":";
	  }
	  String methodNameForMethodAdaptingIndividualConstructor(Constructor<?> c)
	  {	
		  if (c.getParameterTypes().length==0) { return "new";}
		  String methodName="with";
		  for(Class<?> parameterType : c.getParameterTypes())
		  {
			  methodName = methodName + methodSymbolComponentForType(parameterType);
		  }
		  return methodName =methodName+":";
	  }
	
	  private static final Map<String, String> IGNORED_METHODS = new HashMap<String, String>();
	    static {
	        IGNORED_METHODS.put("getClass", "getClass");
	    }
	    
	   protected static final Map<String, String> PRIMITIVE_TO_SIGNATURE_TYPE = new HashMap<String, String>();
	    static {
	        PRIMITIVE_TO_SIGNATURE_TYPE.put("long", "J");
	        PRIMITIVE_TO_SIGNATURE_TYPE.put("int", "I");
	        PRIMITIVE_TO_SIGNATURE_TYPE.put("char", "C");
	        PRIMITIVE_TO_SIGNATURE_TYPE.put("byte", "B");
	    }
	    
	    private String javaArgsSignature(Class<?>[] parameterTypes)
	    {
	    	String javaArgsSignature="";
			for(Class<?> parameterType : parameterTypes)
			  {
				 javaArgsSignature = javaArgsSignature + javaSignatureXXX(parameterType);
			  }
			return javaArgsSignature;
	    }
	    private String javaSignatureXXX(Class<?> parameterTypeC) {
	    	String parameterType = parameterTypeC.getName();
	        if (parameterType.indexOf('.') != -1)
	            return "L" + parameterType.replace(".", "/") + ";";
	        return PRIMITIVE_TO_SIGNATURE_TYPE.get(parameterType);
	    }
	    private String methodSymbolComponentForType(Class<?> parameterTypeC) {
	    	String parameterType =  parameterTypeC.toString();
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
	    
	    private static  String stringAfterLastOrEmptyString(String source,String delimiter) {
	    	int indexOfBeginOfDelimiter = source.lastIndexOf(delimiter);
	    	if(indexOfBeginOfDelimiter==-1) { return "";}
			return source.substring(indexOfBeginOfDelimiter+delimiter.length());
		}


}

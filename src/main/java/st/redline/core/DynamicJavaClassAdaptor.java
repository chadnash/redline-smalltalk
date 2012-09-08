package st.redline.core;

import st.redline.core.reflector.Reflector;

public class DynamicJavaClassAdaptor {

    private final String className;

    DynamicJavaClassAdaptor(String className) {
        this.className = className;
    }

    PrimObject build() {
        String source = generateSmalltalkFor();
        System.out.println("hi chad");
        System.out.println(source);
        return Evaluator.evaluate(source);
    }

    private String generateSmalltalkFor() {
        try {
            return new Reflector(className).reflect().result();
        } catch (ClassNotFoundException e) {
            throw new RedlineException(e);
        }
    }
    public static String fullyQualifiedClassNameForJavaClassWrapperClassNamed(String fullyQualifiedJavaClassName)
    {	
    	System.out.println("chad5>fullyQualifiedJavaClassName="+fullyQualifiedJavaClassName);
    	return "smallTalkClassesThatAdaptJavaClasses." + fullyQualifiedJavaClassName + "Adaptor";
    }
}

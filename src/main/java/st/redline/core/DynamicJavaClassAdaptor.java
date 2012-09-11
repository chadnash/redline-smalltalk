package st.redline.core;

import st.redline.core.reflector.Reflector;
import st.redline.core.reflector.SmallTalkGeneratorOfAdaptorOfAJavaClass;

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
    	String s1 = new SmallTalkGeneratorOfAdaptorOfAJavaClass(className).adaptorSource();
    	String s2 = "";
        try {
            s2= new Reflector(className).reflect().result();
        } catch (ClassNotFoundException e) {
            throw new RedlineException(e);
        }
        System.out.println("**************S1");
        System.out.println(s1);
        System.out.println("**************S2");
        System.out.println(s2);
        return s1;
    }
    public static String fullyQualifiedClassNameForJavaClassWrapperClassNamed(String fullyQualifiedJavaClassName) // remove when old code is gone - chad
    {	
    	System.out.println("chad5>fullyQualifiedJavaClassName="+fullyQualifiedJavaClassName);
    	return "smallTalkClassesThatAdaptJavaClasses." + fullyQualifiedJavaClassName + "Adaptor";
    }
}

package st.redline.core;

//import st.redline.core.reflector.Reflector;

public class DynamicJavaClassAdaptor {

    private final String className;

    DynamicJavaClassAdaptor(String className) {
        this.className = className;
    }

    PrimObject build() {
        String source = generateSmalltalkFor();
        //System.out.println("IN DynamicJavaClassAdaptor.build");
        //System.out.println(source);
        return Evaluator.evaluate(source);
    }

    private String generateSmalltalkFor() {
    	//System.out.println("generateSmalltalkFor classname="+className);
    	String s1 = new SmallTalkGeneratorOfAdaptorOfAJavaClass(className).adaptorSource();
        return s1;
    }
    public static String fullyQualifiedClassNameForJavaClassWrapperClassNamed(String fullyQualifiedJavaClassName) // remove when old code is gone - chad
    {	
    	System.out.println("chad5>fullyQualifiedJavaClassName="+fullyQualifiedJavaClassName);
    	return "smallTalkClassesThatAdaptJavaClasses." + fullyQualifiedJavaClassName + "Adaptor";
    }
}

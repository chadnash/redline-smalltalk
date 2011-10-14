/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline;

import java.math.BigDecimal;
import java.util.*;

// To create a new class, you need an instance of Metaclass, which you build up and then 'new'.
// The newed object IS the class that gets registered.

public class ProtoObject {

	public static final Primitives primitives = new Primitives();

	protected static final Map<String, ProtoObject> classRegistry = classRegistryMapInstance();

	public static ProtoObject METACLASS_INSTANCE;  // <- sole instance of class Metaclass.
	public static ProtoObject TRUE;
	public static ProtoObject FALSE;
	public static ProtoObject NIL;

	private ProtoObject cls;
	private ProtoObject superclass;
	private Object javaValue;
	private Map<String, ProtoMethod> methods;
	private Map<String, String> packages;
	private Map<String, ProtoObject> variables;
	private Map<String, ProtoObject> instanceVariables;

	public ProtoObject() {
		initialize();
	}

	public ProtoObject(ProtoObject cls) {
		this.cls = cls;
		initialize();
	}

	protected static ProtoObject resolveObject(ProtoObject receiver, String name) throws ClassNotFoundException {
		return receiver.resolveObject(name);
	}

	private void initialize() {
		initializeInstanceVariables(cls(), variablesMapInstance());
	}

	protected void initializeInstanceVariables(ProtoObject cls, Map<String, ProtoObject> variables) {
		if (cls != null) {
			if (cls.instanceVariables() != null) {
				for (Map.Entry<String, ProtoObject> entry : cls.instanceVariables().entrySet())
					variables.put(entry.getKey(), NIL);
				variables(variables);
			}
			if (cls.superclass() != null)
				initializeInstanceVariables(cls.superclass(), variables);
		}
	}

	public void bootstrap() {
		new Bootstrapper(this).bootstrap();
	}

	public ProtoObject(BigDecimal bigDecimal) {
		javaValue(bigDecimal);
	}

	protected ProtoObject registerAs(String name) {
		classRegistry.put(name, this);
		return this;
	}

	protected ProtoObject resolveObject(String name) throws ClassNotFoundException {
		ProtoObject object = resolveObject0(name);
		if (object != null)
			return object;
		throw new ClassNotFoundException(name);
	}

	private ProtoObject resolveObject0(String name) throws ClassNotFoundException {
		if (classRegistry.containsKey(name))
			return classRegistry.get(name);

		if (Character.isUpperCase(name.charAt(0))) {
			String fullyQualifiedName = packageAt(name);
			if (fullyQualifiedName != null)
				return resolveObject(fullyQualifiedName);
		}

		// It is expected the loading of an object results in the registering a Smalltalk class in the class registry.
		// *NOTE* if class is not registered the will be a NullPointerException as we return 'null' here.
		ProtoObject object = loadObject(name);
		if (object != null) {
			if (classRegistry.containsKey(name))
				return classRegistry.get(name);
			// We loaded a class and created an instance, it may be a script so return it.
			return object;
		}
		return null;
	}

	protected ProtoObject loadObject(String name) {
		try {
			return (ProtoObject) Class.forName(name, true, classLoader()).newInstance();
		} catch (Exception e) {
			throw RedlineException.withCause(e);
		}
	}

	private ClassLoader classLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	protected ProtoMethod methodAt(String name) {
		if (methods != null && methods.containsKey(name))
			return methods.get(name);
		ProtoObject superclass = superclass();
		while (superclass != null) {
			if (superclass.methods != null && superclass.methods.containsKey(name))
				return superclass.methods.get(name);
			superclass = superclass.superclass();
		}
		return null;
	}

	protected ProtoObject methodAtPut(String name, ProtoMethod method) {
		if (methods == null)
			methods = methodsMapInstance();
		methods.put(name, method);
		return this;
	}

	public String packageAt(String name) {
		if (packages != null)
			return packages.get(name);
		return null;
	}

	public ProtoObject packageAtPut(String name, String javaPackageName) {
		if (packages == null)
			packages = packageMapInstance();
		packages.put(name, javaPackageName.replace("/", "."));
		return this;
	}

	protected ProtoObject javaValue(Object javaValue) {
		this.javaValue = javaValue;
		return this;
	}

	public Object javaValue() {
		return javaValue;
	}

	protected ProtoObject cls(ProtoObject cls) {
		this.cls = cls;
		return this;
	}

	public ProtoObject cls() {
		return cls;
	}

	protected ProtoObject superclass(ProtoObject superclass) {
		this.superclass = superclass;
		if (cls() != null)
			cls().superclass(superclass.cls());
		return this;
	}

	protected ProtoObject superclass() {
		return superclass;
	}

	protected ProtoObject variableAt(String name) {
		return variables != null ? variables.get(name) : null;
	}

	protected ProtoObject variableAtPut(String name, ProtoObject value) {
		return variables.put(name, value);
	}

	protected ProtoObject variables(Map<String, ProtoObject> variables) {
		this.variables = variables;
		return this;
	}

	protected Map<String, ProtoObject> variables() {
		return variables;
	}

	public boolean hasVariableNamed(String variable) {
		return (variables() != null && variables().containsKey(variable))
				|| (superclass() != null && superclass().hasVariableNamed(variable));
	}

	protected ProtoObject instanceVariables(Map<String, ProtoObject> instanceVariables) {
		this.instanceVariables = instanceVariables;
		return this;
	}

	protected Map<String, ProtoObject> instanceVariables() {
		return instanceVariables;
	}

	public boolean hasInstanceVariableNamed(String variable) {
		return (instanceVariables() != null && instanceVariables().containsKey(variable))
				|| (superclass() != null && superclass().hasInstanceVariableNamed(variable));
	}

	public static Map<String, ProtoObject> classRegistryMapInstance() {
		return new Hashtable<String, ProtoObject>();
	}

	public static Map<String, ProtoObject> variablesMapInstance() {
		return new Hashtable<String, ProtoObject>();
	}

	public static Map<String, ProtoMethod> methodsMapInstance() {
		return new Hashtable<String, ProtoMethod>();
	}

	public static Map<String, String> packageMapInstance() {
		return new Hashtable<String, String>();
	}
}

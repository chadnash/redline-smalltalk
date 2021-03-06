/* Redline Smalltalk, Copyright (c) James C. Ladd. All rights reserved. See LICENSE in the root of this distribution */
package st.redline.core;

import st.redline.bootstrap.*;

import java.io.File;
import java.util.Map;

public class Bootstrapper {

    PrimObjectMetaclass primObjectMetaclass;

    Bootstrapper(PrimObjectMetaclass primObjectMetaclass) {
        this.primObjectMetaclass = primObjectMetaclass;
    }

    public void bootstrap() {
        markBootstrapping(true);
        mapPackages(PrimObjectMetaclass.IMPORTS);
        createAndRegisterProtoObject();
        registerBootstrappedSingletons();
        createClasses();
        makeClassSuperclassOfObjectsClass();
        makeClassDescriptionSuperclassOfMetaclassClass();
        markBootstrapping(false);
        instantiateNonBootstrappedSingletons();
    }

    private void createClasses() {
        primObjectMetaclass.resolveObject("st.redline.core.Symbol");
    }

    void makeClassDescriptionSuperclassOfMetaclassClass() {
        primObjectMetaclass.superclass(primObjectMetaclass.resolveObject("st.redline.core.ClassDescription"));
    }

    void makeClassSuperclassOfObjectsClass() {
        PrimObjectClass objectClass = (PrimObjectClass) primObjectMetaclass.resolveObject("st.redline.core.Object").cls();
        objectClass.superclass(primObjectMetaclass.resolveObject("st.redline.core.Class"));
    }

    void instantiateNonBootstrappedSingletons() {
        PrimObject.NIL.cls(primObjectMetaclass.resolveObject("st.redline.core.UndefinedObject"));
        PrimObject.TRUE = primObjectMetaclass.resolveObject("st.redline.core.True").perform("new");
        PrimObject.FALSE = primObjectMetaclass.resolveObject("st.redline.core.False").perform("new");
    }

    void registerBootstrappedSingletons() {
        PrimObject.CLASSES.put("st.redline.core.Metaclass", primObjectMetaclass);
        primObjectMetaclass.methods().put("atSelector:put:", new AtSelectorPutMethod());
        primObjectMetaclass.methods().put("instanceVariableNames:", new InstanceVariableNamesMethod());
        primObjectMetaclass.methods().put("import:", new ImportMethod());
        primObjectMetaclass.methods().put("import:as:", new ImportAsMethod());
        PrimObjectMetaclass undefinedObjectMetaClass = PrimObjectMetaclass.basicSubclassOf(primObjectMetaclass);
        PrimObjectMetaclass undefinedObjectClass = undefinedObjectMetaClass.basicCreate("UndefinedObject", PrimObject.PRIM_NIL, "", "", "", "");
        PrimObject.NIL = new PrimObject();
        PrimObject.NIL.cls(undefinedObjectClass);
    }

    void markBootstrapping(boolean bootstrapping) {
        PrimObject.BOOTSTRAPPING = bootstrapping;
    }

    void createAndRegisterProtoObject() {
        PrimObjectMetaclass protoObjectMetaclass = PrimObjectMetaclass.basicSubclassOf(primObjectMetaclass);
        protoObjectMetaclass.methods().put("<", new CreateSubclassMethod());
        protoObjectMetaclass.methods().put("atSelector:put:", new AtSelectorPutMethod());
        protoObjectMetaclass.methods().put("class", new AccessClassMethod());
        protoObjectMetaclass.methods().put("initialize", new InitializeMethod());
        PrimObjectMetaclass protoObjectClass = protoObjectMetaclass.basicCreate("ProtoObject", PrimObject.PRIM_NIL, "", "", "", "");
        protoObjectClass.methods().put("initialize", new InitializeMethod());
        PrimObject.CLASSES.put("st.redline.core.ProtoObject", protoObjectClass);
    }

    void mapPackages(Map<String, String> imports) {
        imports.put("ProtoObject", "st.redline.core.ProtoObject");
        for (String sourceFile : SourceFileFinder.findIn("st" + File.separator + "redline" + File.separator + "core")) {
            String packageName = ClassPathUtilities.filenameWithExtensionToPackageName(sourceFile);
            String name = ClassPathUtilities.filenameToClassName(sourceFile);
            imports.put(name, packageName + "." + name);
        }
    }
}
